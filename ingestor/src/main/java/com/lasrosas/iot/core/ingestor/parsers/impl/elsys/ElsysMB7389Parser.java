package com.lasrosas.iot.core.ingestor.parsers.impl.elsys;

import java.util.List;

import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.messaging.Message;

import com.lasrosas.iot.core.ingestor.parsers.api.ThingDataMessage;
import com.lasrosas.iot.core.ingestor.parsers.api.ThingEncodedMessage;
import com.lasrosas.iot.core.ingestor.parsers.impl.PayloadParser;
import com.lasrosas.iot.core.shared.telemetry.Order;
import com.lasrosas.iot.core.shared.telemetry.Telemetry;

public class ElsysMB7389Parser implements PayloadParser {

	private ElsysGenericParser parser ;

	public ElsysMB7389Parser(ElsysGenericParser parser ) {
		this.parser = parser;
	}

	@Override
	public Message<? extends ThingDataMessage> decodeUplink(Message<ThingEncodedMessage> imessage) {
		return parser.decodeUplink(imessage);
	}

	@Override
	public byte[] encodeOrder(Order order) {
		throw new NotYetImplementedException();
	}

	@Override
	public List<Message<? extends Telemetry>> telemetries(Message<ThingDataMessage> imessage) {
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
