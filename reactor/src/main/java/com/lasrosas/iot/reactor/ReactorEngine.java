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

public class ReactorEngine {
	public static final Logger log = LoggerFactory.getLogger(ReactorEngine.class);

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

	public ReactorEngine(TwinReactors twinReactors, InfluxdbSession influxdb, MqttSession mqtt) {
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
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}

		var parts = topic.split("/");
		var twinTechid = Long.parseLong(parts[1]);
		var twin = twinRepo.getOne(twinTechid);

		var reactor = twinReactors.getReactor(twin);
		var json = new String(msg.getPayload());

		if(reactor == null) {
			log.info("No reactor.");
			log.debug(json);
			return;
		}

		var jsono = gson.fromJson(json, JsonArray.class);

		log.debug("Handle message:");
		log.debug(json);

		// Mapping between incoming data and reactor receiver is now static
		// Later, may become dynamic and configuratble.
		var receiverValues = reactor.mapReceiverValues(jsono);

		log.debug((receiverValues == null?0: receiverValues.size()) + " messages maped");

		if(receiverValues == null || receiverValues.size() == 0) 
			return;

		var transmiterValues = reactor.react(twin, receiverValues);
		log.debug(transmiterValues.size() + " messages transmited");

		// Send data to mysql, influxdb and publish to mqtt
		for(var value: transmiterValues) {
			insertPoint(twin, value.getTime(), WaterTankFilling.SCHEMA, value.getValue());
		}
	}

	private TimeSeriePoint insertPoint(DigitalTwin twin, LocalDateTime time, String schema, JsonObject jsono) {
		
		if(log.isDebugEnabled()) {
			log.debug("Add point to the twin " + twin.getName());
			log.debug(gson.toJson(jsono));
		}

		if( schema == null ) 
			throw new NotFoundException("schema in the sensor normalized message.");

		var tst = tstRepo.findBySchema(schema);
		if( tst == null ) {
			tst = new TimeSerieType(schema);
			log.debug("New time serie type for schema " + schema);
			tstRepo.save(tst);
		}

		var tsr = tsrRepo.findByTwinAndType(twin, tst);
		if( tsr == null ) {
			tsr = new TimeSerie(twin, tst);
			log.debug("New time serie for schema " + schema);
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

		log.debug("Publish to mqtt topic " + topic);
		log.debug(json);

		var message = new MqttMessage(json.getBytes());
		mqtt.publish(topic, message);

		return tsp;
	}
}
