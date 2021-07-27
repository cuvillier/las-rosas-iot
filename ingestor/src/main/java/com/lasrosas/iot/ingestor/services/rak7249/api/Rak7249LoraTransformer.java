package com.lasrosas.iot.ingestor.services.rak7249.api;

import org.springframework.integration.transformer.AbstractTransformer;
import org.springframework.messaging.Message;

public class Rak7249LoraTransformer extends AbstractTransformer {
	private Rak7249MessagesService service;

	public Rak7249LoraTransformer(Rak7249MessagesService service) {
		super();
		this.service = service;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Object doTransform(Message<?> message) {
		var payload = message.getPayload();
		if(payload instanceof Rak7249MessageJoin)
			return service.handleJoin((Message<Rak7249MessageJoin>)payload);

		if(payload instanceof Rak7249MessageAck)
			return service.handleAck((Message<Rak7249MessageAck>)payload);

		if(payload instanceof Rak7249MessageRx)
			return service.handleRx((Message<Rak7249MessageRx>)payload);

		throw new RuntimeException("Unknown message type: " + payload.getClass().getName());
	}
}
