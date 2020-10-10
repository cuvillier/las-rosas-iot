package com.lasrosas.iot.ingestor;

import java.util.List;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lasrosas.iot.database.entities.tsr.TimeSeriePoint;
import com.lasrosas.iot.mqtt.MqttSession;

public class SendMessageToTwin {
	public static final Logger log = LoggerFactory.getLogger(SendMessageToTwin.class);

	@Autowired
	private Gson gson;
	
	private MqttSession mqtt;

	public SendMessageToTwin(MqttSession mqtt) {
		this.mqtt = mqtt;
	}

	public void send(List<TimeSeriePoint> points) {
		try {
			var jsonArray = new JsonArray(points.size());

			var thing = points.get(0).getTimeSerie().getThing();

			var dtwin = thing.getTwin();
			if( dtwin == null ) return;

			String topic = "digital-twin/" + dtwin.getTechid() + "/from-sensor";
	
			for(var point: points) {

				var jsono = new JsonObject();
				jsono.addProperty("action", "newTimeSeriePoints");
				jsono.addProperty("tspTechid", point.getTechid());
				jsonArray.add(jsono);
			}

			var messageJson = gson.toJson(jsonArray);

			var message = new MqttMessage(messageJson.getBytes());
			
			log.info("Publish topic=" + topic + " message=" + new String(message.getPayload()));
			this.mqtt.publish(topic, message);
			

		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
