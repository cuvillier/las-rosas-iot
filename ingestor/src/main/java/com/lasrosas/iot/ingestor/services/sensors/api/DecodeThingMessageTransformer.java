package com.lasrosas.iot.ingestor.services.sensors.api;

import org.springframework.integration.transformer.AbstractTransformer;
import org.springframework.messaging.Message;

public class DecodeThingMessageTransformer extends AbstractTransformer {

	private SensorService service;

	public DecodeThingMessageTransformer(SensorService service) {
		this.service = service;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Object doTransform(Message<?> imessage) {

		if(imessage.getPayload() instanceof ThingEncodedMessage) 
			return service.decodeUplink((Message<ThingEncodedMessage>)imessage);

		return null;
	}
}
