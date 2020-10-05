package com.lasrosas.iot.ingestor;

import java.util.function.Consumer;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.google.gson.JsonObject;

public class LoraServerRAK7249Mqtt {

	private LoraServerRAK7249 loraServerRAK7249;

	@Value("${LoraServerRAK7249Mqtt.clientId:LoraIngestorAppDev}")
	private String clientId;

	@Value("${rak7249.mqtt.server:localhost}")
	private String mqttServer;

	@Value("${rak7249.mqtt.port:1883}")
	private int mqttPort;

	private IMqttClient mqtt;
	private boolean automaticReconnect = true;
	private boolean cleanSession = false;
	private int connectionTimeout = 10;

	public LoraServerRAK7249Mqtt() {}
	
	public LoraServerRAK7249Mqtt(LoraServerRAK7249 loraServerRAK7249) {
		super();
		this.loraServerRAK7249 = loraServerRAK7249;
	}


	public LoraServerRAK7249 getLoraServerRAK7249() {
		return loraServerRAK7249;
	}

	public void start(Consumer<JsonObject> consumer) {
		try {
			this.mqtt = new MqttClient("tcp://" + mqttServer + ":" + mqttPort, clientId);

			MqttConnectOptions options = new MqttConnectOptions();
			options.setAutomaticReconnect(this.automaticReconnect);
			options.setCleanSession(this.cleanSession);
			options.setConnectionTimeout(this.connectionTimeout);
			this.mqtt.connect(options);

			mqtt.subscribe("application/+/device/+/+", (topic, msg) -> {
				// Go through the spring proxy to start db transaction
				loraServerRAK7249.handleMessage(topic, msg, consumer);
			});
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
