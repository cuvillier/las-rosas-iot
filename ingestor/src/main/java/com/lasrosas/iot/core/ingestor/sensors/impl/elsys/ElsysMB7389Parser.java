package com.lasrosas.iot.core.ingestor.sensors.impl.elsys;

import java.util.List;

import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.messaging.Message;

import com.lasrosas.iot.core.ingestor.sensors.api.ThingDataMessage;
import com.lasrosas.iot.core.ingestor.sensors.api.ThingEncodedMessage;
import com.lasrosas.iot.core.ingestor.sensors.impl.PayloadParser;
import com.lasrosas.iot.core.shared.telemetry.Telemetry;

public class ElsysMB7389Parser implements PayloadParser {

	private ElsysGenericParser parser = new ElsysGenericParser();

	@Override
	public Message<? extends ThingDataMessage> decodeUplink(Message<ThingEncodedMessage> imessage) {
		return parser.decodeUplink(imessage);
	}

	@Override
	public byte[] encodeDownlink(Message<?> imessage) {
		throw new NotYetImplementedException();
	}

	@Override
	public List<Message<Telemetry>> telemetries(Message<ThingDataMessage> imessage) {
		return parser.telemetries(imessage);
	}

	@Override
	public String getManufacturer() {
		return "Elsys";
	}

	@Override
	public String getModel() {
		return "MB7389";
	}
}
