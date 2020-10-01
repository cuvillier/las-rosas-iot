package com.lasrosas.iot.ingestor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lasrosas.iot.database.entities.thg.ThingLora;
import com.lasrosas.iot.database.entities.thg.ThingProxy;
import com.lasrosas.iot.database.entities.tsr.TimeSerie;
import com.lasrosas.iot.database.entities.tsr.TimeSeriePoint;
import com.lasrosas.iot.database.entities.tsr.TimeSerieType;
import com.lasrosas.iot.database.repo.ThingLoraRepo;
import com.lasrosas.iot.database.repo.TimeSeriePointRepo;
import com.lasrosas.iot.database.repo.TimeSerieRepo;
import com.lasrosas.iot.database.repo.TimeSerieTypeRepo;
import com.lasrosas.iot.ingestor.parser.PayloadParsers;
import com.lasrosas.iot.shared.utils.GsonUtils;
import com.lasrosas.iot.shared.utils.LocalTopic;
import com.lasrosas.iot.shared.utils.NotFoundException;

public class LoraIngestor {
	public static Logger log = LoggerFactory.getLogger(LoraIngestor.class);

	private LoraServerRAK7249Mqtt rak7249Mqtt;

	private InfluxdbWriter influxdbWriter;

	private PayloadParsers sensors;

	@Autowired
	private Gson gson;
	
	@Autowired
	private ThingLoraRepo thgLoraRepo;
	
	@Autowired
	private TimeSerieRepo tsrRepo;

	@Autowired
	private TimeSerieTypeRepo tstRepo;

	@Autowired
	private TimeSeriePointRepo tspRepo;
	
	@Autowired
	private LocalTopic<TimeSeriePoint> newPointTopic;

	public LoraIngestor(LoraServerRAK7249Mqtt rak7249Mqtt, PayloadParsers sensors, InfluxdbWriter influxdbWriter) {
		this.rak7249Mqtt = rak7249Mqtt;
		this.sensors = sensors;
		this.influxdbWriter = influxdbWriter;
	}

	public void start() throws Exception {

		newPointTopic.subcribe(influxdbWriter::send);

		newPointTopic.subcribe(this::sendToTwin);

		this.rak7249Mqtt.start(c -> {
			handleLoraMessage(rak7249Mqtt.getLoraServerRAK7249(), c);
		});
	}

	private void sendToTwin(TimeSeriePoint point) {

		var thing = point.getTimeSerie().getThing();

		var dtwin = thing.getTwin();
		if( dtwin != null ) dtwin.onNewPoint(point);
	}

	private String getAsString(JsonObject json, String memberName, boolean mandatory) {

		var member = json.get(memberName);
		if( member == null ) {
			if( mandatory ) 
				throw new NotFoundException("Member " + memberName + " in json " + gson.toJson(json));
			return null;
		}

		return member.getAsString();
	}

	@Transactional
	public void handleLoraMessage(LoraServer loraServer, JsonObject loraMessage) {

		try {
			var deveui = getAsString(loraMessage, "deveui", true);
			var timestamp = Long.parseLong(getAsString(loraMessage, "timestamp", true));
			LocalDateTime time = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), TimeZone.getDefault().toZoneId());  

			var thing = thgLoraRepo.getByDeveui(deveui);
			if(thing == null) throw new NotFoundException("thing deveui=" + deveui);

			var thingType = thing.getType();

			var payloadParser = sensors.getParser(thingType.getManufacturer(), thingType.getModel());
			if(payloadParser == null) throw new NotFoundException("Sensor type manufacturer="+thingType.getManufacturer() + ", model="+ thingType.getModel());

			String schema = thingType.getManufacturer() +"." + thingType.getModel() + ".lora";
			String sensor = null;
			
			if(thing.isLogLoraMessages())
				insertPoint(thing, time, schema, sensor, loraMessage);

			// Convert base64, hex to byte[]
			var data = decodeData(loraMessage);

			var decodedMessages = payloadParser.decode(data);

			for (var decodedMessage : decodedMessages) {

				if(thing.isLogDecodedMessages())
					insertPoint(thing, time, decodedMessage, false);

				var normalizedMessages = payloadParser.normalize(decodedMessage);

				for (var normalizedMessage : normalizedMessages) {
					insertPoint(thing, time, normalizedMessage, true);
				}
			}
		} catch (Exception e) {
			log.error("Cannot handle message", e);
		}
	}

	private byte[] decodeData(JsonObject loraMessage) {
		var encoding = loraMessage.get("dataEncoding").getAsString();
		if( !encoding.equalsIgnoreCase("base64") ) throw new RuntimeException("Unknown data encoding encoding=" + encoding);

		var encodedData = loraMessage.get("data").getAsString();
		var decodedData = Base64.getDecoder().decode(encodedData);

		return decodedData;
	}

	private void insertPoint(ThingLora thing, LocalDateTime time, MessageHolder holder, boolean proxify) {
		var json = gson.toJsonTree(holder.getMessage()).getAsJsonObject();
		insertPoint(thing, time, holder.getSchema(), holder.getSensor(), json);

		if( proxify) {
			var proxy = thing.getProxy();

			// Create the proxy if needed
			if( proxy == null ) {
				proxy = new ThingProxy();
				proxy.setThing(thing);
				thing.setProxy(proxy);
			}

			// Merge the proxy values
			proxy.setLastSeen(time);

			var proxyJsonValue = proxy.getValues();
			var proxyValue = gson.fromJson(proxyJsonValue, JsonObject.class);
			if( proxyValue == null) proxyValue = new JsonObject();

			String subjsonName;
			if( holder.getSensor() != null )
				subjsonName = holder.getSensor() + "-" + holder.getSchema();
			else
				subjsonName = holder.getSchema();

			JsonObject subjson = proxyValue.getAsJsonObject(subjsonName);
			if( subjson == null ) {
				subjson = new JsonObject();
				proxyValue.add(subjsonName, subjson);
			}

			var changes = GsonUtils.mergeJsonObjects(json, subjson, time);
			if(changes != 0) 
				proxy.setValues(gson.toJson(proxyValue));

		}
	}

	private void insertPoint(ThingLora thing, LocalDateTime time, String schema, String sensor, JsonObject message) {

		if( schema == null ) 
			throw new NotFoundException("schema in the sensor normalized message.");

		var tst = tstRepo.findBySchema(schema);
		if( tst == null ) {
			tst = new TimeSerieType(schema);
			tstRepo.save(tst);
		}

		var tsr = tsrRepo.findByThingAndTypeAndSensor(thing, tst, sensor);
		if( tsr == null ) {
			tsr = new TimeSerie(thing, tst, sensor);
			tsrRepo.save(tsr);
		}

		String json = gson.toJson(message);

		var tsp = new TimeSeriePoint(tsr, time, json);
		tspRepo.save(tsp);

		newPointTopic.publish(tsp);
	}
}
