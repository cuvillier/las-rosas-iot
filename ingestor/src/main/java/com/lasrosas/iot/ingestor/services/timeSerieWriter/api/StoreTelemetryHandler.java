package com.lasrosas.iot.ingestor.services.timeSerieWriter.api;

import org.springframework.integration.handler.AbstractMessageHandler;
import org.springframework.messaging.Message;

import com.lasrosas.iot.shared.telemetry.Telemetry;

public class StoreTelemetryHandler extends AbstractMessageHandler {
	private WriteInfluxDB service;

	public StoreTelemetryHandler(WriteInfluxDB service) {
		this.service = service;
	}

	@Override
	protected void handleMessageInternal(Message<?> message) {
		var payload = (Telemetry)message.getPayload();
		var measurement = payload.getClass().getName().replaceAll("\\.", "_");
		service.writePoint(measurement, payload);
	}
}
