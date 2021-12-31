package com.lasrosas.iot.core.flux;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lasrosas.iot.core.ingestor.gateway.api.GatewayService;

public class OrderConverter extends  DefaultPahoMessageConverter {
	public static final Logger log = LoggerFactory.getLogger(OrderConverter.class);

	private GatewayService service;

	@Autowired
	private Gson gson;

	
	public OrderConverter(GatewayService service) {
		super();
		this.service = service;
	}

	@Override
	public MqttMessage fromMessage(Message<?> message, Class<?> targetClass) {
		throw new RuntimeException("Not supported");
	}

	@Override
	public Message<?> toMessage(Object payload, MessageHeaders headers) {
		throw new RuntimeException("Not supported");
	}

	@Override
	public Message<?> toMessage(String topic, MqttMessage mqttMessage) {
		try {
			var message = super.toMessage(topic, mqttMessage);

			// Convert to RAK7249 messages
			String json = (String)message.getPayload();
			var jsono = gson.fromJson(json, JsonObject.class);
			var orderClassName = jsono.get("orderClassName").getAsString();
			var thgNaturalId = jsono.get("thingNaturalId").getAsString();

			Class<?> orderClass = Class.forName(orderClassName);
			var valueJson = jsono.get("value").getAsJsonObject();
			var order = gson.fromJson(valueJson,orderClass);

			return MessageBuilder.createMessage(order, message.getHeaders());
		} catch(Exception e) {
			log.error("Cannot convert json to RAK7249 message", e);
			return null;
		}
	}
}
