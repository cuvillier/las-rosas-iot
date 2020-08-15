package com.lasrosas.iot.services.lora;

import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import com.lasrosas.iot.services.db.entities.thg.Thing;
import com.lasrosas.iot.services.lora.mqtt.MqttIngestor;
import com.lasrosas.iot.services.lora.sensors.LoraSensors;
import com.lasrosas.iot.services.lora.server.LoraServer;
import com.lasrosas.iot.services.mqtt.MqttReader;
import com.lasrosas.iot.services.thingAPI.ThingAPI;

public class LoraIngestor implements Consumer<Mqtt5Publish> {
	public static Logger log = LoggerFactory.getLogger(MqttIngestor.class);

	private MqttReader mqtt;
	private LoraServer loraServer;
	private LoraSensors loraSensors;
	private String topic;
	private ThingAPI thingAPI;

	public LoraIngestor(MqttReader mqtt, LoraServer loraServer, LoraSensors loraSensors, String topic) {

		this.loraServer = loraServer;
		this.loraSensors = loraSensors;
		this.mqtt = mqtt;
		this.topic = topic;

		mqtt.connect();
		this.mqtt.subscribe(this.topic, this);
	}

	@Override
	public void accept(Mqtt5Publish t) {

		String payload = new String(t.getPayloadAsBytes());
		log.info("Receive payload " + payload);

		JsonObject loraMessage = loraServer.normalize(payload);
		var deveui = loraMessage.get("deveui").getAsString();
		var provider = loraMessage.get("provider").getAsString();

		Thing thing = thingAPI.getThingByDevEUI(deveui, provider);

		List<JsonObject> decodedMessages = loraSensors.decode(loraMessage);
		List<JsonObject> normalizedMessages = loraSensors.normalize(decodedMessages);
	}

}
