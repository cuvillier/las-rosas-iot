package com.lasrosas.iot.core.ingestor.sensors.api;

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
	@SuppressWarnings("unchecked")
	@Override
	protected Object splitMessage(Message<?> imessage) {

		if(imessage.getPayload() instanceof ThingDataMessage) 
			return service.telemetries((Message<ThingDataMessage>)imessage);

		return null;
	}
}
