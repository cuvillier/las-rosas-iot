package com.lasrosas.iot.reactor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lasrosas.iot.database.entities.dtw.DigitalTwin;
import com.lasrosas.iot.database.entities.dtw.TwinReactorReceiver;
import com.lasrosas.iot.database.entities.tsr.TimeSerie;
import com.lasrosas.iot.database.entities.tsr.TimeSeriePoint;
import com.lasrosas.iot.database.entities.tsr.TimeSerieType;
import com.lasrosas.iot.database.repo.DigitalTwinRepo;
import com.lasrosas.iot.database.repo.ThingRepo;
import com.lasrosas.iot.database.repo.TimeSeriePointRepo;
import com.lasrosas.iot.database.repo.TimeSerieRepo;
import com.lasrosas.iot.database.repo.TimeSerieTypeRepo;
import com.lasrosas.iot.influxdb.InfluxdbSession;
import com.lasrosas.iot.mqtt.session.MqttSession;
import com.lasrosas.iot.reactore.reactor.ReactorReceiverValue;
import com.lasrosas.iot.reactore.reactor.TwinReactor;
import com.lasrosas.iot.shared.ontology.IotMessage;
import com.lasrosas.iot.shared.utils.NotFoundException;

public class ReactorEngine {
	public static final Logger log = LoggerFactory.getLogger(ReactorEngine.class);

	@Autowired
	private ApplicationContext beanContext;

	@Autowired
	private Gson gson;
	
	@Autowired
	private DigitalTwinRepo twinRepo;

	@Autowired
	private TimeSerieTypeRepo tstRepo;

	@Autowired
	private TimeSerieRepo tsrRepo;

	@Autowired
	private ThingRepo thgRepo;

	@Autowired
	private TimeSeriePointRepo tspRepo;

	@Autowired
	private InfluxdbSession influxdb;

	@Autowired
	private MqttSession mqtt;

	public ReactorEngine(InfluxdbSession influxdb, MqttSession mqtt) {
		this.influxdb = influxdb;
		this.mqtt = mqtt;
	}

	public void start() {
		try {
			mqtt.subscribe("thing/+/+/+", this::handleMessage);
			mqtt.subscribe("digital-twin/+/+/+", this::handleMessage);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static class TwinReactorKey {
		private final DigitalTwin twin;
		private final String reactorBean;

		public TwinReactorKey(DigitalTwin twin, String reactorBean) {
			super();
			this.twin = twin;
			this.reactorBean = reactorBean;
		}

		public DigitalTwin getTwin() {
			return twin;
		}

		public String getReactorBean() {
			return reactorBean;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((reactorBean == null) ? 0 : reactorBean.hashCode());
			result = prime * result + ((twin == null) ? 0 : twin.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TwinReactorKey other = (TwinReactorKey) obj;
			if (reactorBean == null) {
				if (other.reactorBean != null)
					return false;
			} else if (!reactorBean.equals(other.reactorBean))
				return false;
			if (twin == null) {
				if (other.twin != null)
					return false;
			} else if (!twin.equals(other.twin))
				return false;
			return true;
		}

	}

	@Transactional
	public void handleMessage(String topic, MqttMessage msg) {

		var parts = topic.split("/");

		var senderGroup = parts[0];
		// parts[1] is he sender type. No need here
		var senderId = Long.parseLong(parts[2]);

		List<TwinReactorReceiver> receivers = new ArrayList<>();

		// Cast up list elements from sub classes to TwinReactorReceiver
		if( senderGroup.equals("thing") ) {
			thgRepo.getOne(senderId).getReceivers().stream().forEach(e -> receivers.add(e));
		} else if( senderGroup.equals("digital-twin")) {
			twinRepo.getOne(senderId).getReceivers().stream().forEach(e -> receivers.add(e));
		} else {
			log.warn("Unknown senderGroup : " + senderGroup);
			return;
		}

		if(receivers.isEmpty() )
			return;

		var message = gson.fromJson(new String(msg.getPayload()), IotMessage.class);

		/*
		 * A sensor data may be used by multiple twins and multiple reactors.
		 * Reactors are called for each (twin, reactor) tuple if the schema match.
		 * This loop build the Twin / Reactor index.
		 */
		Map<TwinReactorKey, List<ReactorReceiverValue>> valuesByTwinAndReactor = new HashMap<>();
		for(var receiver: receivers) {
			var receiverType = receiver.getType();

			for(var point: message.getPoints()) {

				if( point.getSchema().equals(receiverType.getSchema()) ) {
					var key = new TwinReactorKey(receiver.getTwin(), receiverType.getReactorType().getBean());
	
					var valuesForKey = valuesByTwinAndReactor.get(key);
					if(valuesForKey == null) {
						valuesForKey = new ArrayList<>();
						valuesByTwinAndReactor.put(key, valuesForKey);
					}

					var value = gson.fromJson(point.getValue(), JsonObject.class);
					valuesForKey.add(new ReactorReceiverValue(receiver, value));
				}
			}
		}

		// Call the react method on each (twin, reactor) tuple
		for(var twinReactorKey: valuesByTwinAndReactor.keySet()) {
			var receiverValues = valuesByTwinAndReactor.get(twinReactorKey);

			var reactor = beanContext.getBean(TwinReactor.class, twinReactorKey.getReactorBean());
			var result = reactor.react(twinReactorKey.getTwin(), receiverValues);

			for(var point: result) {
				insertPoint(twinReactorKey.getTwin(), message.getTime(), point.getSchema(), point.getJson());
			}
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
