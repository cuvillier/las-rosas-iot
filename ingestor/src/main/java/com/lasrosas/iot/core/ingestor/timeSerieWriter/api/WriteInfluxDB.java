package com.lasrosas.iot.core.ingestor.timeSerieWriter.api;

import org.springframework.messaging.Message;

import com.lasrosas.iot.core.database.entities.tsr.TimeSerie;

public interface WriteInfluxDB {

	// Must be called after WriteSQL
	void writePoint(TimeSerie tsr, Message<?>  imessage);
}
