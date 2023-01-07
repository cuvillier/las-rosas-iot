package com.lasrosas.iot.core.ingestor.timeSerieWriter.api;

import java.util.Optional;

import org.springframework.messaging.Message;

import com.lasrosas.iot.core.database.entities.tsr.TimeSeriePoint;

public interface WriteSQL {
	Optional<TimeSeriePoint> writePoint(Message<?>  imessage);

	void setStoreProxyTime(boolean b);

	void updateProxy(TimeSeriePoint point);
}
