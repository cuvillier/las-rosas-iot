package com.lasrosas.iot.core.flux;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;

import com.lasrosas.iot.core.ingestor.rak7249.api.Rak7249Service;

public class MqttRak7249Converter extends  DefaultPahoMessageConverter {
	public static final Logger log = LoggerFactory.getLogger(MqttRak7249Converter.class);

	@Autowired
	private Rak7249Service service;

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
			var payload = service.fromJson(topic, json);
			return MessageBuilder.createMessage(payload, message.getHeaders());
		} catch(Exception e) {
			log.error("Cannot convert json to RAK7249 message", e);
			return null;
		}
	}

}
