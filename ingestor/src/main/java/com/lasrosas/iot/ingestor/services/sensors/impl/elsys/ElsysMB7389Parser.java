package com.lasrosas.iot.ingestor.services.sensors.impl.elsys;

import java.util.List;

import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.lasrosas.iot.ingestor.ThingMessageHolder;
import com.lasrosas.iot.ingestor.services.sensors.impl.PayloadParser;

public class ElsysMB7389Parser implements PayloadParser {

	@Autowired
	private ElsysGenericParser parser;

	@Override
	public List<ThingMessageHolder> decodeUplink(byte[] data) {
		return parser.decode(data);
	}

	@Override
	public byte[] encodeDownlink(Object frame) {
		throw new NotYetImplementedException();
	}

	@Override
	public List<ThingMessageHolder> normalize(ThingMessageHolder decodedMessage) {
		return parser.normalize(decodedMessage);
	}

	public String getManufacturer() {
		return "Elsys";
	}

	public String getModel() {
		return "MB7389";
	}
}
