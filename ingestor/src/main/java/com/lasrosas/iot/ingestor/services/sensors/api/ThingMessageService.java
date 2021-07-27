package com.lasrosas.iot.ingestor.services.sensors.api;

import org.springframework.integration.splitter.AbstractMessageSplitter;
import org.springframework.messaging.Message;

import com.lasrosas.iot.ingestor.services.lora.api.LoraMessage;
import com.lasrosas.iot.ingestor.services.sensors.impl.PayloadParsers;

public class ThingMessageService extends AbstractMessageSplitter {
	private PayloadParsers parsers;

	public ThingMessageService(PayloadParsers parsers) {
		this.parsers = parsers;
	}

	/*
	 * Decode Message from a Lora sensor, and normalize the data model.
	 * return a collection of ThingMessages
	 */
	@Override
	protected Object splitMessage(Message<?> message) {
		var payload = message.getPayload();

		if(!(payload instanceof LoraMessage)) return null;
		var loraMessage = (LoraMessage)payload;

		return parsers.parse(loraMessage);
	}
}
