package com.lasrosas.iot.ingestor.services.sensors.api;

import org.springframework.integration.splitter.AbstractMessageSplitter;
import org.springframework.messaging.Message;

public class TelemetrySpliter extends AbstractMessageSplitter {

	private SensorService service;

	public TelemetrySpliter(SensorService service) {
		this.service = service;
	}

	/*
	 * Decode Message from a Lora sensor, and normalize the data model.
	 * return a collection of ThingMessages
	 */
	@Override
	protected Object splitMessage(Message<?> imessage) {
		var payload = imessage.getPayload();

		if(!(payload instanceof ThingDataMessage)) return null;

		var message = (ThingDataMessage)payload;
		return service.telemetries(message);
	}
}
