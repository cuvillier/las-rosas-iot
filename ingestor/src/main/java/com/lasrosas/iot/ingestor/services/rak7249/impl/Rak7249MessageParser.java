package com.lasrosas.iot.ingestor.services.rak7249.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import com.google.gson.Gson;
import com.lasrosas.iot.ingestor.services.rak7249.api.Rak7249Message;
import com.lasrosas.iot.ingestor.services.rak7249.api.Rak7249MessageAck;
import com.lasrosas.iot.ingestor.services.rak7249.api.Rak7249MessageJoin;
import com.lasrosas.iot.ingestor.services.rak7249.api.Rak7249MessageRx;

public class Rak7249MessageParser {

	@Autowired
	private Gson gson;

	public Rak7249Message parse(String topic, Message<?> message) {

		if(topic.endsWith("/join")) 
			return parseJoin(topic, message);
		if(topic.endsWith("/rx")) 
			return parseRX(topic, message);
		if(topic.endsWith("/ack")) 
			return parseAck(topic, message);

		throw new RuntimeException("Unkknown topic " + topic + ". Fix the code here.");
	}

	public Rak7249MessageRx parseRX(String topic, Message<?> message) {
		var json = (String)message.getPayload();
		var result = gson.fromJson(json, Rak7249MessageRx.class);
		copy(result, topic, message.getHeaders());
		return result;
	}

	public Rak7249MessageJoin parseJoin(String topic, Message<?> message) {
		var json = (String)message.getPayload();
		var result = gson.fromJson(json, Rak7249MessageJoin.class);
		copy(result, topic, message.getHeaders());
		return result;
	}

	public Rak7249MessageAck parseAck(String topic, Message<?> message) {
		var json = (String)message.getPayload();
		var result = gson.fromJson(json, Rak7249MessageAck.class);
		copy(result, topic, message.getHeaders());
		return result;
	}

	private void copy(Rak7249Message message, String topic, MessageHeaders headers) {
		message.setTopic(topic);
		for(var entry : headers.entrySet()) {
			message.addheader(entry.getKey(), entry.getValue());
		}
	}
}
