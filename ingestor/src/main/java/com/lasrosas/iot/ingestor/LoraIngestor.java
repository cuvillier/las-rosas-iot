package com.lasrosas.iot.ingestor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lasrosas.iot.database.entities.alrm.Alarm;
import com.lasrosas.iot.database.entities.alrm.AlarmType;
import com.lasrosas.iot.database.entities.alrm.ThingAlarm;
import com.lasrosas.iot.database.entities.thg.Thing;
import com.lasrosas.iot.database.entities.thg.ThingLora;
import com.lasrosas.iot.database.entities.thg.ThingProxy;
import com.lasrosas.iot.database.entities.tsr.TimeSerie;
import com.lasrosas.iot.database.entities.tsr.TimeSeriePoint;
import com.lasrosas.iot.database.entities.tsr.TimeSerieType;
import com.lasrosas.iot.database.repo.AlarmTypeRepo;
import com.lasrosas.iot.database.repo.ThingAlarmRepo;
import com.lasrosas.iot.database.repo.ThingLoraRepo;
import com.lasrosas.iot.database.repo.TimeSeriePointRepo;
import com.lasrosas.iot.database.repo.TimeSerieRepo;
import com.lasrosas.iot.database.repo.TimeSerieTypeRepo;
import com.lasrosas.iot.ingestor.parser.PayloadParsers;
import com.lasrosas.iot.shared.ontology.BatteryLevel;
import com.lasrosas.iot.shared.utils.GsonUtils;
import com.lasrosas.iot.shared.utils.LocalTopic;
import com.lasrosas.iot.shared.utils.NotFoundException;

public class LoraIngestor {
	public static Logger log = LoggerFactory.getLogger(LoraIngestor.class);

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
	private ThingAlarmRepo alrRepo;

	@Autowired
	private AlarmTypeRepo altRepo;

	public LoraIngestor(PayloadParsers sensors) {
		this.sensors = sensors;
	}

	private String getAsString(JsonObject json, String memberName, boolean mandatory) {

		var member = json.get(memberName);
		if (member == null) {
			if (mandatory)
				throw new NotFoundException("Member " + memberName + " in json " + gson.toJson(json));
			return null;
		}

		return member.getAsString();
	}

	public static class LoraMessageIngestionResult {
		private List<TimeSeriePoint> notification;
	}

	public List<TimeSeriePoint> handleLoraMessage(LoraServer loraServer, JsonObject loraMessage) {

		var result = new ArrayList<TimeSeriePoint>();

		var deveui = getAsString(loraMessage, "deveui", true);
		var timestamp = Long.parseLong(getAsString(loraMessage, "timestamp", true));
		LocalDateTime time = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp),
				TimeZone.getDefault().toZoneId());

		var thing = thgLoraRepo.getByDeveui(deveui);
		if (thing == null)
			throw new NotFoundException("thing deveui=" + deveui);

		var thingType = thing.getType();

		var payloadParser = sensors.getParser(thingType.getManufacturer(), thingType.getModel());
		if (payloadParser == null)
			throw new NotFoundException(
					"Sensor type manufacturer=" + thingType.getManufacturer() + ", model=" + thingType.getModel());

		String schema = thingType.getManufacturer() + "." + thingType.getModel() + ".lora";
		String sensor = null;

		if (thing.isLogLoraMessages())
			result.add(insertPoint(thing, time, schema, sensor, loraMessage));

		// Convert base64, hex to byte[]
		var data = decodeData(loraMessage);

		var decodedMessages = payloadParser.decodeUplink(data);

		for (var decodedMessage : decodedMessages) {

			if (thing.isLogDecodedMessages())
				result.add(insertPoint(thing, time, decodedMessage, false));

			var normalizedMessages = payloadParser.normalize(decodedMessage);

			for (var normalizedMessage : normalizedMessages) {
				var point = insertPoint(thing, time, normalizedMessage, true);

				if (normalizedMessage.getMessage() instanceof BatteryLevel) {
					handleBatteryLevel(thing, time, (BatteryLevel) normalizedMessage.getMessage());
				}

				result.add(point);
			}
		}
		return result;
	}

	private void handleBatteryLevel(Thing thing, LocalDateTime time, BatteryLevel batteryLevel) {
		boolean newAlarm = false;

		var thingType = thing.getType();

		if (batteryLevel.getAlarm() != null) {
			newAlarm = batteryLevel.getAlarm();

		} else if (batteryLevel.getPercentage() != null) {
			var percentage = batteryLevel.getPercentage();

			if (thingType.getBatteryMinPercentage() != null && percentage < thingType.getBatteryMinPercentage()) {
				newAlarm = true;
			}
		}

		var alarmType = altRepo.getByName(AlarmType.THING_BATTERY_ALARM);
		var alarm = alrRepo.getByTypeAndThingAndStateNot(alarmType, thing, Alarm.State.Closed);

		if (alarm == null && newAlarm) {

			// Activate battery alarm
			alarm = new ThingAlarm(thing, alarmType, time);
			alrRepo.save(alarm);

		} else if (alarm != null && !newAlarm) {

			// Close the battery alarm
			alarm.close(time);
		}
	}

	private byte[] decodeData(JsonObject loraMessage) {
		var encoding = loraMessage.get("dataEncoding").getAsString();
		if (!encoding.equalsIgnoreCase("base64"))
			throw new RuntimeException("Unknown data encoding encoding=" + encoding);

		var encodedData = loraMessage.get("data").getAsString();
		var decodedData = Base64.getDecoder().decode(encodedData);

		return decodedData;
	}

	private TimeSeriePoint insertPoint(ThingLora thing, LocalDateTime time, ThingMessageHolder holder,
			boolean proxify) {
		var json = gson.toJsonTree(holder.getMessage()).getAsJsonObject();
		var point = insertPoint(thing, time, holder.getSchema(), holder.getSensor(), json);

		if (proxify) {
			var proxy = thing.getProxy();

			// Create the proxy if needed
			if (proxy == null) {
				proxy = new ThingProxy();
				proxy.setThing(thing);
				thing.setProxy(proxy);
			}

			// Merge the proxy values
			proxy.setLastSeen(time);

			var proxyJsonValue = proxy.getValues();
			var proxyValue = gson.fromJson(proxyJsonValue, JsonObject.class);
			if (proxyValue == null)
				proxyValue = new JsonObject();

			String subjsonName;
			if (holder.getSensor() != null)
				subjsonName = holder.getSensor() + "-" + holder.getSchema();
			else
				subjsonName = holder.getSchema();

			JsonObject subjson = proxyValue.getAsJsonObject(subjsonName);
			if (subjson == null) {
				subjson = new JsonObject();
				proxyValue.add(subjsonName, subjson);
			}

			var changes = GsonUtils.mergeJsonObjects(json, subjson, time);
			if (changes != 0)
				proxy.setValues(gson.toJson(proxyValue));
		}

		return point;
	}

	private TimeSeriePoint insertPoint(ThingLora thing, LocalDateTime time, String schema, String sensor,
			JsonObject message) {

		if (schema == null)
			throw new NotFoundException("schema in the sensor normalized message.");

		var tst = tstRepo.findBySchema(schema);
		if (tst == null) {
			tst = new TimeSerieType(schema);
			tstRepo.save(tst);
		}

		var tsr = tsrRepo.findByThingAndTypeAndSensor(thing, tst, sensor);
		if (tsr == null) {
			tsr = new TimeSerie(thing, tst, sensor);
			tsrRepo.save(tsr);
		}

		String json = gson.toJson(message);

		var tsp = new TimeSeriePoint(tsr, time, json);
		tspRepo.save(tsp);
		return tsp;
	}
}
