package com.lasrosas.iot.mqtt.rules;

import java.lang.reflect.ParameterizedType;

import javax.annotation.PostConstruct;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.lasrosas.iot.mqtt.session.MqttSession;
import com.lasrosas.iot.mqtt.session.MqttSession.MqttListener;

public class DataChangeEngineMqttAdaptador<T> implements MqttListener {
	public static final Logger log = LoggerFactory.getLogger(DataChangeEngineMqttAdaptador.class);

	@Autowired
	private Gson gson;

	private DataChangeEngine<T> dataChangeEngine;
	private MqttSession mqtt;
	private Class<T> tclass;
	private String topic;

	@SuppressWarnings("unchecked")
	public DataChangeEngineMqttAdaptador(MqttSession mqtt, DataChangeEngine<T> dataChangeEngine) {
		this.dataChangeEngine = dataChangeEngine;
		this.mqtt = mqtt;
		this.tclass = (Class<T>)
                ((ParameterizedType)getClass()
                .getGenericSuperclass())
                .getActualTypeArguments()[0];
		this.topic = "data/" + tclass.getSimpleName() + "/dataChange";
	}

	@PostConstruct
	public void init() {
		mqtt.subscribe(topic, this);		
	}

	@Override
	public void handle(String topic, MqttMessage message) {
		var json = new String(message.getPayload());
		var dataChange = gson.fromJson(json, DataChange.class);
		dataChangeEngine.handleDataChange(dataChange);
	}

	/*
		var twinTechid = Long.parseLong(parts[1]);
		var twin = twinRepo.getOne(twinTechid);

		var reactor = twinReactors.getReactor(twin);

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
*/
}
