package com.lasrosas.iot.core.ingestor.parsers.impl;

import java.util.List;

import org.springframework.messaging.Message;

import com.lasrosas.iot.core.ingestor.parsers.api.ThingDataMessage;
import com.lasrosas.iot.core.ingestor.parsers.api.ThingEncodedMessage;
import com.lasrosas.iot.core.shared.telemetry.Order;
import com.lasrosas.iot.core.shared.telemetry.Telemetry;

public interface PayloadParser {
	String getManufacturer();
	String getModel();
	Message<? extends ThingDataMessage> decodeUplink(Message<ThingEncodedMessage> imessage);
	List<Message<? extends Telemetry>> telemetries(Message<ThingDataMessage> message);
	byte[] encodeOrder(Order order);
	default boolean notifyJoin() {return false;}
}
