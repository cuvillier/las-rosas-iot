package com.lasrosas.iot.core.ingestor.timeSerieWriter.api;

import org.springframework.integration.handler.AbstractMessageHandler;
import org.springframework.messaging.Message;

public class StoreTelemetryHandler extends AbstractMessageHandler {
	private WriteInfluxDB influxDB;
	private WriteSQL sql;

	public StoreTelemetryHandler(WriteInfluxDB influxDB, WriteSQL sql) {
		this.influxDB = influxDB;
		this.sql = sql;
	}

	@Override
	protected void handleMessageInternal(Message<?> imessage) {
		if(influxDB != null) influxDB.writePoint(imessage);
		if(sql != null) sql.writePoint(imessage);
	}
}
