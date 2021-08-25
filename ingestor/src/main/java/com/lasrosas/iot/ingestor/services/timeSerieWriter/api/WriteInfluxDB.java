package com.lasrosas.iot.ingestor.services.timeSerieWriter.api;

import org.springframework.messaging.Message;

public interface WriteInfluxDB {
	void writePoint(Message<?>  imessage);
}
