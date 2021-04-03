package com.lasrosas.iot.mqtt.rules;

import java.lang.reflect.ParameterizedType;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.lasrosas.iot.mqtt.session.MqttSession;

public class DataChangePublisher<T> {

	@Autowired
	private Gson gson;

	private MqttSession mqtt;
	private Class<T> tclass;
	private String topic;

	@SuppressWarnings("unchecked")
	public DataChangePublisher(MqttSession mqtt) {
		this.mqtt = mqtt;
		this.tclass = (Class<T>)
                ((ParameterizedType)getClass()
                .getGenericSuperclass())
                .getActualTypeArguments()[0];
		this.topic = "data/" + tclass.getSimpleName() + "/dataChange";

	}

	public void publish(DataChange dataChange) {
		var event = new DataChangeEvent(dataChange);
		var json = gson.toJson(event);
		var message = new MqttMessage(json.getBytes());
		mqtt.publish(topic, message);
	}
}
