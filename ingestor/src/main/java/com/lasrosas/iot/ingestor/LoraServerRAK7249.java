package com.lasrosas.iot.ingestor;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.lasrosas.iot.database.entities.thg.ThingLora;
import com.lasrosas.iot.database.repo.GatewayRepo;
import com.lasrosas.iot.database.repo.ThingLoraRepo;
import com.lasrosas.iot.database.repo.ThingTypeRepo;
import com.lasrosas.iot.mqtt.MqttSession;
import com.lasrosas.iot.shared.utils.NotFoundException;

public class LoraServerRAK7249 extends LoraServer {
	public static Logger log = LoggerFactory.getLogger(LoraIngestor.class);
	public static Logger payloadLog = LoggerFactory.getLogger("payload-rak7249");

	private MqttSession mqtt;

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

	public LoraServerRAK7249(MqttSession mqtt) {
		super("rak7249");

		this.mqtt = mqtt;
	}

	public void start(Consumer<JsonObject> consumer) {
		try {
			mqtt.subscribe("application/+/device/+/+", (topic, msg) -> {
				handleMessage(topic, msg, consumer);
			});
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
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

			JsonObject payload;
			try {
				payload = gson.fromJson(payloadJson, JsonObject.class);
			} catch(JsonSyntaxException e) {
				throw new RuntimeException("Message with an invalid Json format", e);
			}

			switch(messageType) {
			case JOIN:
				handleJoin(payload);
				return;

			case UPLOAD:
				var loraMessage = handleUpload(payload);
				consumer.accept(loraMessage);
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
			loraMessage.addProperty("snr", rxInfo.get("loRaSNR").getAsFloat());
			loraMessage.addProperty("rssi", rxInfo.get("rssi").getAsInt());
		}

		JsonObject txInfo = rakMessage.getAsJsonObject("txInfo");

		if (txInfo != null) {
			loraMessage.addProperty("frequency", txInfo.get("frequency").getAsLong());
		}

		System.out.println("----------------------------------------------------");
		System.out.println(gson.toJson(loraMessage));

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
