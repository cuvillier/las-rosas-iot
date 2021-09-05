package com.lasrosas.iot.core.ingestor.timeSerieWriter.api;

import org.springframework.messaging.Message;

import com.lasrosas.iot.core.database.entities.tsr.TimeSeriePoint;

public interface WriteSQL {
	TimeSeriePoint writePoint(Message<?>  imessage);
}
