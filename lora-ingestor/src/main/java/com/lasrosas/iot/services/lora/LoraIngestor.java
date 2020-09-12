package com.lasrosas.iot.services.lora;

import java.nio.charset.StandardCharsets;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lasrosas.iot.services.db.repo.ThingLoraRepo;
import com.lasrosas.iot.services.db.repo.TimeSerieRepo;
import com.lasrosas.iot.services.lora.sensors.LoraSensors;
import com.lasrosas.iot.services.utils.NotFoundException;

public class LoraIngestor {
	public static Logger log = LoggerFactory.getLogger(LoraIngestor.class);

	private LoraServerRAK7249 rak7249;
	private LoraSensors sensors;
	private IMqttClient mqtt;

	@Autowired
	private Gson gson;
	
	@Autowired
	private ThingLoraRepo thgLoraRepo;
	
	@Autowired
	private TimeSerieRepo tsrRepo;

	public LoraIngestor(LoraServerRAK7249 rak7249, LoraSensors sensors) {
		this.rak7249 = rak7249;
		this.sensors = sensors;
	}

	public void start() throws Exception {

		this.rak7249.start(c -> {
			handleMessage(rak7249, c);
		});
	}

	@Transactional
	private void handleMessage(LoraServer loraServer, JsonObject loraMessage) {

		try {
			var deveui = loraMessage.get("deveui").getAsString();
			if(deveui == null) throw new NotFoundException("deveui in loraMessage");

			var thing = thgLoraRepo.getByDeveui(deveui);
			loraMessage.addProperty("thingId", thing.getTechid());

			var thingType = thing.getType();

			var sensor = sensors.getSensor(thingType.getManufacturer(), thingType.getModel());
			if(sensor == null) throw new NotFoundException("Sensor type manufacturer="+thingType.getManufacturer() + ", model="+ thingType.getModel());

			var decodedMessages = sensor.decode(loraMessage);
			for (var decodedMessage : decodedMessages) {
				var normalizedMessages = sensor.normalize(decodedMessage);

				for (var normalizedMessage : normalizedMessages) {
					String schema = normalizedMessage.get("schema").getAsString();
					String json = gson.toJson(normalizedMessage);
					String topic = "things/lora/" + deveui + "/" + schema;

					var outMessage = new MqttMessage(json.getBytes(StandardCharsets.UTF_8));
					outMessage.setQos(1);
					outMessage.setRetained(true);

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
