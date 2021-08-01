package com.lasrosas.iot.ingestor.services.sensors.api;

import org.springframework.integration.transformer.AbstractTransformer;
import org.springframework.messaging.Message;

public class DecodeThingMessageTransformer extends AbstractTransformer {

	private SensorService service;

	public DecodeThingMessageTransformer(SensorService service) {
		this.service = service;
	}

	@Override
	protected Object doTransform(Message<?> imessage) {
		var payload = imessage.getPayload();

		if(!(payload instanceof ThingEncodedMessage)) return null;

		var message = (ThingEncodedMessage)payload;
		return service.decodeUplink(message);
	}
}
