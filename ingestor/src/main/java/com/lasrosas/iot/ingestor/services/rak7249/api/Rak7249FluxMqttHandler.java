package com.lasrosas.iot.ingestor.services.rak7249.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

/*
 * Spring Integration handler wrapping the Rak7249Service.
 */
public class Rak7249FluxMqttHandler {

	@Autowired
	private Rak7249Service service;

	public Rak7249FluxMqttHandler(Rak7249Service service) {
		this.service = service;
	}

	public Rak7249Message handle(String topic, Message<?> imessage) {
		return service.fromJson(topic, (String)imessage.getPayload());
	}
}
