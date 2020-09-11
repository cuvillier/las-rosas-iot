package com.lasrosas.iot.services.lora;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class LoraServerRAK7249 extends LoraServer {
	private String publisherId = "loraIngestor";
	private String mqttServer = "localhost";
	private int mqttPort = 1883;
	private Gson gson;
	private IMqttClient mqtt;
	private boolean automaticReconnect = true;
	private boolean cleanSession = true;
	private int connectionTimeout = 10;
	

	public LoraServerRAK7249(Gson gson) {
		this.gson = gson;
	}

	public void start(Consumer<JsonObject> consumer) {
		try {
			this.mqtt = new MqttClient("tcp://" + mqttServer + ":" + mqttPort, publisherId);
	
			MqttConnectOptions options = new MqttConnectOptions();
			options.setAutomaticReconnect(this.automaticReconnect);
			options.setCleanSession(this.cleanSession);
			options.setConnectionTimeout(this.connectionTimeout);
			this.mqtt.connect(options);

			mqtt.subscribe("lora/+/+", (topic, msg) -> {

				byte [] payloadBytes = msg.getPayload();
				String payloadJson = new String(payloadBytes, StandardCharsets.UTF_8);
				var payload = gson.fromJson(payloadJson, JsonObject.class);
				
				consumer.accept(payload);
			});

		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
