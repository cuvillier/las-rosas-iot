package com.lasrosas.iot.core.ingestor.timeSerieWriter.api;

import org.springframework.messaging.Message;

public interface WriteSQL {
	void writePoint(Message<?>  imessage);
}
