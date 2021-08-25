package com.lasrosas.iot.ingestor.services.sensors.impl;

import java.util.List;

import org.springframework.messaging.Message;

import com.lasrosas.iot.ingestor.services.sensors.api.ThingDataMessage;
import com.lasrosas.iot.ingestor.services.sensors.api.ThingEncodedMessage;
import com.lasrosas.iot.shared.telemetry.Telemetry;

public interface PayloadParser {
	String getManufacturer();
	String getModel();
	Message<? extends ThingDataMessage> decodeUplink(Message<ThingEncodedMessage> imessage);
	byte[] encodeDownlink(Message<?> frame);
	List<Message<Telemetry>> telemetries(Message<ThingDataMessage> message);
}
