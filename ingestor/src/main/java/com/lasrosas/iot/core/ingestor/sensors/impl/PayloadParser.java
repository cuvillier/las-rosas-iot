package com.lasrosas.iot.core.ingestor.sensors.impl;

import java.util.List;

import org.springframework.messaging.Message;

import com.lasrosas.iot.core.ingestor.sensors.api.ThingDataMessage;
import com.lasrosas.iot.core.ingestor.sensors.api.ThingEncodedMessage;
import com.lasrosas.iot.core.shared.telemetry.Telemetry;

public interface PayloadParser {
	String getManufacturer();
	String getModel();
	Message<? extends ThingDataMessage> decodeUplink(Message<ThingEncodedMessage> imessage);
	byte[] encodeDownlink(Message<?> frame);
	List<Message<Telemetry>> telemetries(Message<ThingDataMessage> message);
}
