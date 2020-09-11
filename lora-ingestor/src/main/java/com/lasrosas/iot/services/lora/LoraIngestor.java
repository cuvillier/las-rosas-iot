package com.lasrosas.iot.services.lora;

import java.nio.charset.StandardCharsets;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lasrosas.iot.services.db.repo.GatewayRepo;
import com.lasrosas.iot.services.db.repo.ThingRepo;
import com.lasrosas.iot.services.db.repo.TimeSerieRepo;
import com.lasrosas.iot.services.lora.sensors.LoraSensors;
import com.lasrosas.iot.services.thingAPI.ThingAPI;

public class LoraIngestor {
	public static Logger log = LoggerFactory.getLogger(LoraIngestor.class);

	private LoraServerRAK7249 rak7249;
	private LoraSensors sensors;
	private IMqttClient mqtt;
	private String mqttServer = "localhost";
	private int mqttPort = 1883;
	private boolean automaticReconnect = true;
	private boolean cleanSession = true;
	private int connectionTimeout = 10;
	private String publisherId = "loraIngestor";
	private Gson gson;
	private GatewayRepo gtwRepo;
	private ThingRepo thgRepo;
	private TimeSerieRepo tsrRepo;

	public LoraIngestor(LoraServerRAK7249 rak7249, LoraSensors sensors, ThingRepo thgRepo, TimeSerieRepo tsrRepo, GatewayRepo gtwRepo, Gson gson) {
		this.gson = gson;
		this.gtwRepo = gtwRepo;
		this.rak7249 = rak7249;
		this.sensors = sensors;
	}

	public void start() throws Exception {
		this.mqtt = new MqttClient("tcp://" + mqttServer + ":" + mqttPort, publisherId);

		MqttConnectOptions options = new MqttConnectOptions();
		options.setAutomaticReconnect(this.automaticReconnect);
		options.setCleanSession(this.cleanSession);
		options.setConnectionTimeout(this.connectionTimeout);
		this.mqtt.connect(options);

		mqtt.connect();

		this.rak7249.start(c -> {
			handleMessage(rak7249, c);
		});
	}

	private void handleMessage(LoraServer loraServer, JsonObject inMessage) {

		try {

			var gatewayId = loraServer.getGatewayId();
			var gateway = gtwRepo.findByNaturalId(gatewayId);

			var loraMessage = loraServer.normalize(inMessage);
			loraMessage.addProperty("gatewayId", gatewayId);

			var deveui = loraMessage.get("deveui").getAsString();

			var thing = thgRepo.getByGatewayAndDeveui(gateway, deveui);
			inMessage.addProperty("thignId", thing.getTechid());

			var thingType = thing.getType();

			var sensor = sensors.getSensor(thingType.getManufacturer(), thingType.getModel());
			var decodedMessages = sensor.decode(inMessage);
			for (var decodedMessage : decodedMessages) {
				var normalizedMessages = sensor.normalize(decodedMessage);

				for (var normalizedMessage : normalizedMessages) {
					String schema = normalizedMessage.get("schema").getAsString();
					String json = gson.toJson(normalizedMessage);
					String topic = "message/" + gatewayId + "/" + deveui + "/" + schema;

					var outMessage = new MqttMessage(json.getBytes(StandardCharsets.UTF_8));

					mqtt.publish(topic, outMessage);
				}
			}
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
