package com.lasrosas.iot.reactor;

import java.time.LocalDateTime;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lasrosas.iot.database.entities.dtw.DigitalTwin;
import com.lasrosas.iot.database.entities.tsr.TimeSerie;
import com.lasrosas.iot.database.entities.tsr.TimeSeriePoint;
import com.lasrosas.iot.database.entities.tsr.TimeSerieType;
import com.lasrosas.iot.database.repo.DigitalTwinRepo;
import com.lasrosas.iot.database.repo.TimeSeriePointRepo;
import com.lasrosas.iot.database.repo.TimeSerieRepo;
import com.lasrosas.iot.database.repo.TimeSerieTypeRepo;
import com.lasrosas.iot.influxdb.InfluxdbSession;
import com.lasrosas.iot.mqtt.MqttSession;
import com.lasrosas.iot.reactore.reactores.TwinReactors;
import com.lasrosas.iot.shared.ontology.WaterTankFilling;
import com.lasrosas.iot.shared.utils.NotFoundException;

public class Reactor {
	public static final Logger log = LoggerFactory.getLogger(Reactor.class);

	@Autowired
	private Gson gson;
	
	@Autowired
	private DigitalTwinRepo twinRepo;
	

	@Autowired
	private TimeSerieTypeRepo tstRepo;

	@Autowired
	private TimeSerieRepo tsrRepo;

	@Autowired
	private TimeSeriePointRepo tspRepo;

	@Autowired
	private InfluxdbSession influxdb;

	@Autowired
	private MqttSession mqtt;

	private TwinReactors twinReactors;

	public Reactor(TwinReactors twinReactors, InfluxdbSession influxdb, MqttSession mqtt) {
		this.twinReactors = twinReactors;
		this.influxdb = influxdb;
		this.mqtt = mqtt;
	}

	public void start() {
		try {
			mqtt.subscribe("digital-twin/+/from-sensor", (topic, msg) -> {
				handleMessage(topic, msg);
			});

		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Transactional
	public void handleMessage(String topic, MqttMessage msg) {
		var parts = topic.split("/");
		var twinTechid = Long.parseLong(parts[1]);
		var twin = twinRepo.getOne(twinTechid);

		var reactor = twinReactors.getReactor(twin);

		if(reactor == null) return;

		var json = new String(msg.getPayload());
		var jsono = gson.fromJson(json, JsonArray.class);

		// Mapping between incoming data and reactor receiver is now static
		// Later, may become dynamic and configuratble.
		var receiverValues = reactor.mapReceiverValues(jsono);
		
		if(receiverValues == null || receiverValues.size() == 0) 
			return;

		var transmiterValues = reactor.react(twin, receiverValues);

		// Send data to mysql, influxdb and publish to mqtt
		for(var value: transmiterValues) {
			insertPoint(twin, value.getTime(), WaterTankFilling.SCHEMA, value.getValue());
		}
	}

	private TimeSeriePoint insertPoint(DigitalTwin twin, LocalDateTime time, String schema, JsonObject jsono) {

		if( schema == null ) 
			throw new NotFoundException("schema in the sensor normalized message.");

		var tst = tstRepo.findBySchema(schema);
		if( tst == null ) {
			tst = new TimeSerieType(schema);
			tstRepo.save(tst);
		}

		var tsr = tsrRepo.findByTwinAndType(twin, tst);
		if( tsr == null ) {
			tsr = new TimeSerie(twin, tst);
			tsrRepo.save(tsr);
		}

		String json = gson.toJson(jsono);

		var tsp = new TimeSeriePoint(tsr, time, json);
		tspRepo.save(tsp);

		// Write to InfluxDB
		influxdb.write(tsp);

		// Publish to Mqtt
		var topic = "digital-twin/" + twin.getTechid() + "/changes/" + schema;
		jsono.addProperty("time", time.toString());
		json = gson.toJson(jsono);
		var message = new MqttMessage(json.getBytes());
		mqtt.publish(topic, message);

		return tsp;
	}
}
