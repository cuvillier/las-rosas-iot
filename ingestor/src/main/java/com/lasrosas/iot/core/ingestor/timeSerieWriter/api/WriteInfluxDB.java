package com.lasrosas.iot.core.ingestor.timeSerieWriter.api;

import org.springframework.messaging.Message;

public interface WriteInfluxDB {
	void writePoint(Message<?>  imessage);
}
