package com.lasrosas.iot.ingestor.services.sensors.api;

import java.util.Collection;

import org.springframework.messaging.Message;

import com.lasrosas.iot.shared.telemetry.Telemetry;

public interface SensorService  {
	Message<? extends ThingDataMessage> decodeUplink(Message<ThingEncodedMessage> message);
	Collection<Message<Telemetry>> telemetries(Message<ThingDataMessage> rawData);
}
