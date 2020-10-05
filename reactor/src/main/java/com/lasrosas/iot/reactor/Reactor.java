package com.lasrosas.iot.reactor;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lasrosas.iot.reactore.reactores.TwinReactors;

public class Reactor {

	@Autowired
	private Gson gson;

	private TwinReactors twinReactors;

	private String clientId = "com.lasrosas.iot.reactor.dev";
	private String mqttServer = "localhost";
	private int mqttPort = 1883;

	private IMqttClient mqtt;
	private boolean automaticReconnect = true;
	private boolean cleanSession = false;
	private int connectionTimeout = 10;

	public Reactor(TwinReactors twinReactors) {
		this.twinReactors = twinReactors;
	}

	public void start() {
		try {
			this.mqtt = new MqttClient("tcp://" + mqttServer + ":" + mqttPort, clientId);

			MqttConnectOptions options = new MqttConnectOptions();
			options.setAutomaticReconnect(this.automaticReconnect);
			options.setCleanSession(this.cleanSession);
			options.setConnectionTimeout(this.connectionTimeout);
			this.mqtt.connect(options);

			mqtt.subscribe("/digital-twin/+/from-sensor", (topic, msg) -> {
				handleMessage(topic, msg);
			});

		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void handleMessage(String topic, MqttMessage msg) {
		var parts = topic.split("/");
		var twinTechid = Long.parseLong(parts[1]);

		var json = new String(msg.getPayload());
		var jsono = gson.fromJson(json, JsonObject.class);

		twinReactors.handleSensorMessage(twinTechid, jsono);
	}
}
