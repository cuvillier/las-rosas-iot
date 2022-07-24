package com.lasrosas.iot.core.ingestor.parsers.api;

import org.springframework.integration.transformer.AbstractTransformer;
import org.springframework.messaging.Message;

public class UplinkDecoderTransformer extends AbstractTransformer {

	private SensorService service;

	public UplinkDecoderTransformer(SensorService service) {
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
