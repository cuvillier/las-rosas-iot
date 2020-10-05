package com.lasrosas.iot.ingestor;

import java.util.List;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lasrosas.iot.database.entities.tsr.TimeSeriePoint;

public class SendMessageToTwin {
	public static final Logger log = LoggerFactory.getLogger(SendMessageToTwin.class);

	@Autowired
	private Gson gson;

	@Value("${SendMessageToTwin.clientId:SendMessageToTwin}")
	private String clientId;

	@Value("${localmqtt.server:localhost}")
	private String mqttServer;

	@Value("${localmqtt.port:1883}")
	private int mqttPort;

	private IMqttClient mqtt;
	private boolean automaticReconnect = true;
	private boolean cleanSession = false;
	private int connectionTimeout = 10;

	public void send(List<TimeSeriePoint> points) {
		try {
			var jsonArray = new JsonArray(points.size());

			var thing = points.get(0).getTimeSerie().getThing();
			
			var dtwin = thing.getTwin();
			if( dtwin == null ) return;

			String topic = "digital-twin/" + dtwin.getTechid() + "/from-sensor";
	
			if( this.mqtt == null) connect();

			for(var point: points) {

				var jsono = new JsonObject();
				jsono.addProperty("action", "newTimeSeriePoints");
				jsono.addProperty("TimeSeriePoint.techid", point.getTechid());
				
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

	private void connect() {
		try {
			var localMqtt = new MqttClient("tcp://" + mqttServer + ":" + mqttPort, clientId);

			MqttConnectOptions options = new MqttConnectOptions();
			options.setAutomaticReconnect(this.automaticReconnect);
			options.setCleanSession(this.cleanSession);
			options.setConnectionTimeout(this.connectionTimeout);
			localMqtt.connect(options);
			
			this.mqtt = localMqtt;

		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
