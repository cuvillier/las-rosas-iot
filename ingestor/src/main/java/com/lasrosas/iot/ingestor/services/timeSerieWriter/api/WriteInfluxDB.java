package com.lasrosas.iot.ingestor.services.timeSerieWriter.api;

import com.lasrosas.iot.shared.telemetry.Telemetry;

public interface WriteInfluxDB {
	void writePoint(String measurement, Telemetry telemetry);
}
