package com.lasrosas.iot.ingestor.parser.impl.elsys;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lasrosas.iot.ingestor.ThingMessageHolder;
import com.lasrosas.iot.ingestor.parser.PayloadParser;

public class ElsysMB7389Parser implements PayloadParser {

	@Autowired
	private ElsysGenericParser parser;

	@Override
	public List<ThingMessageHolder> decode(byte[] data) {
		return parser.decode(data);
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
