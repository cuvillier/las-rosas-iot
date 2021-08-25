package com.lasrosas.iot.ingestor.services.timeSerieWriter.api;

import org.springframework.integration.handler.AbstractMessageHandler;
import org.springframework.messaging.Message;

public class StoreTelemetryHandler extends AbstractMessageHandler {
	private WriteInfluxDB service;

	public StoreTelemetryHandler(WriteInfluxDB service) {
		this.service = service;
	}

	@Override
	protected void handleMessageInternal(Message<?> imessage) {
		service.writePoint(imessage);
	}
}
