package com.lasrosas.iot.ingestor.services.timeSerieWriter.api;

import org.springframework.messaging.Message;

public interface WriteSQL {
	void writePoint(Message<?>  imessage);
}
