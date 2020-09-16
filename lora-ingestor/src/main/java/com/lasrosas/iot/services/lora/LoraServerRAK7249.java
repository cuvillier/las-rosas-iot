package com.lasrosas.iot.services.lora;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lasrosas.iot.services.db.entities.thg.ThingLora;
import com.lasrosas.iot.services.db.repo.GatewayRepo;
import com.lasrosas.iot.services.db.repo.ThingLoraRepo;
import com.lasrosas.iot.services.db.repo.ThingTypeRepo;
import com.lasrosas.iot.services.utils.NotFoundException;

public class LoraServerRAK7249 extends LoraServer {
	public static Logger log = LoggerFactory.getLogger(LoraIngestor.class);
	public static Logger payloadLog = LoggerFactory.getLogger("payload-rak7249");

	@Autowired
	private ThingLoraRepo thgLorRepo;

	@Autowired
	private ThingTypeRepo thingTypeRepo;

	@Autowired
	private GatewayRepo gatewayRepo;

	@Autowired
	private Gson gson;

	private enum MessageType {
		JOIN, UPLOAD
	}

	public LoraServerRAK7249() {
		super("rak7249");
	}

	@Transactional
	public void handleMessage(String topic, MqttMessage msg, Consumer<JsonObject> consumer) {
		String payloadJson = null;

		try {
			byte [] payloadBytes = msg.getPayload();
			payloadJson = new String(payloadBytes, StandardCharsets.UTF_8);

			payloadLog.info("{");
			payloadLog.info(" topic: \"" + topic + "\"");
			payloadLog.info("  payload: " + payloadJson);
			payloadLog.info("}");

			MessageType messageType;
			if(topic.endsWith("/join")) 
				messageType = MessageType.JOIN;
			else if(topic.endsWith("/rx")) 
				messageType = MessageType.UPLOAD;
			else {
				log.warn("Unknown topic: " + topic);
				log.warn("Message skiped: " + payloadJson);
				return;
			}

			var payload = gson.fromJson(payloadJson, JsonObject.class);

			switch(messageType) {
			case JOIN:
				handleJoin(payload);
				return;

			case UPLOAD:
				var loraMessage = handleUpload(payload);
				consumer.accept((loraMessage));
				break;
			}
		} catch(Exception e) {
			log.error("Cannot handle message from topic " + topic, e);
			if(payloadJson != null) log.error("Payload= " + payloadJson);
		}

	}

	private JsonObject handleUpload(JsonObject rakMessage) {
		String deveui = rakMessage.get("devEUI").getAsString();

		var thgLora = thgLorRepo.getByDeveui(deveui);
		if (thgLora == null)
			throw new NotFoundException("Thing Lora deveui=" + deveui);

		thgLora.getGateway().getNaturalId();

		// Map rakMessage to standard loraMessage
		JsonObject loraMessage = new JsonObject();
		loraMessage.addProperty("timestamp", rakMessage.get("timestamp").getAsLong());
		loraMessage.addProperty("gatewayId", getGatewayId());
		loraMessage.addProperty("deveui", rakMessage.get("devEUI").getAsString());
		loraMessage.addProperty("data", rakMessage.get("data").getAsString());
		loraMessage.addProperty("dataEncoding", rakMessage.get("data_encode").getAsString());
		loraMessage.addProperty("cnt", rakMessage.get("fCnt").getAsInt());
		loraMessage.addProperty("port", rakMessage.get("fPort").getAsInt());

		var rxInfoArray = rakMessage.getAsJsonArray("rxInfo");
		var rxInfo = rxInfoArray.get(0).getAsJsonObject();

		if (rxInfo != null) {
			loraMessage.addProperty("snr", rxInfo.get("loRaSNR").getAsString());
			loraMessage.addProperty("rssi", rxInfo.get("rssi").getAsString());
		}

		JsonObject txInfo = rakMessage.getAsJsonObject("txInfo");

		if (txInfo != null) {
			loraMessage.addProperty("frequency", txInfo.get("frequency").getAsLong());
		}

		return loraMessage;
	}

	private void handleJoin(JsonObject payload) {
		String deveui = payload.get("devEUI").getAsString();
		String deviceName = payload.get("deviceName").getAsString();

		var thingLora = thgLorRepo.getByDeveui(deveui);
		var splitedName = deviceName.split("/");

		/*
		 * Create the device during the join.
		 */
		if (thingLora == null) {
			if (splitedName.length != 3) {
				throw new RuntimeException(
						"Invalid Lora device name, should be manufacturer/model/deveui: " + deviceName);
			}

			String manufacturer = splitedName[0];
			String model = splitedName[1];
			var tty = thingTypeRepo.getByManufacturerAndModel(manufacturer, model);
			if (tty == null)
				throw new NotFoundException("Thing type for device name=" + deviceName);

			var gateway = gatewayRepo.findByNaturalId(getGatewayId());
			if (gateway == null)
				throw new NotFoundException("Gateway " + getGatewayId());
			thingLora = new ThingLora(gateway, tty, deveui);
			thgLorRepo.save(thingLora);
		} else {
			log.info("Thing found with deveui=" + deveui);
		}
	}
}
