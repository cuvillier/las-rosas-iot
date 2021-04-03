package com.lasrosas.iot.ingestor;

import java.util.List;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lasrosas.iot.database.entities.tsr.TimeSeriePoint;
import com.lasrosas.iot.mqtt.session.MqttSession;

public class SendMessageToTwin {
	public static final Logger log = LoggerFactory.getLogger(SendMessageToTwin.class);

	@Autowired
	private Gson gson;

	private MqttSession mqtt;

	public SendMessageToTwin(MqttSession mqtt) {
		this.mqtt = mqtt;
	}

	public void send(List<TimeSeriePoint> points, long txid) {
		try {
			if(points.size() == 0) return;

			var pointsJO = new JsonArray(points.size());

			var thing = points.get(0).getTimeSerie().getThing();

			String topic = "sensors/" + thing.getType().getManufacturer() + "/" +  thing.getType().getModel() + "/" + thing.getTechid() + "/measurements";

			for(var point: points) {

				var pointJO = new JsonObject();
				pointJO.addProperty("txid", txid);
				pointJO.addProperty("techid", point.getTechid());
				pointJO.addProperty("schema", point.getTimeSerie().getType().getSchema());
				pointJO.add("value", point.getValue(gson));
				pointsJO.add(pointJO);
			}

			var messageJO = new JsonObject();
			messageJO.addProperty("txid", txid);
			messageJO.addProperty("publishTime", System.currentTimeMillis());
			messageJO.add("points", pointsJO);

			var messageJson = gson.toJson(messageJO);

			var messageMQTT = new MqttMessage(messageJson.getBytes());
			log.info("Publish topic=" + topic + " message=" + new String(messageMQTT.getPayload()));
			this.mqtt.publish(topic, messageMQTT);

		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
