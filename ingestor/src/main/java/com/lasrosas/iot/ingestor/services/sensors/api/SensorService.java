package com.lasrosas.iot.ingestor.services.sensors.api;

import java.util.Collection;

import com.lasrosas.iot.shared.telemetry.Telemetry;

public interface SensorService  {
	ThingDataMessage decodeUplink(ThingEncodedMessage message);
	Collection<Telemetry> telemetries(ThingDataMessage rawData);
}
