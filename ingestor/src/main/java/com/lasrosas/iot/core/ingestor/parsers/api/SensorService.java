package com.lasrosas.iot.core.ingestor.parsers.api;

import java.util.Collection;

import org.springframework.messaging.Message;

import com.lasrosas.iot.core.shared.telemetry.Order;
import com.lasrosas.iot.core.shared.telemetry.Telemetry;

public interface SensorService  {
	Message<? extends ThingDataMessage> decodeUplink(Message<ThingEncodedMessage> message);
	Collection<Message<? extends Telemetry>> telemetries(Message<ThingDataMessage> rawData);
	byte [] encodeOrder(Message<? extends Order> imessage);
	boolean isNotifyJoin(String manufacturer, String model);
}
