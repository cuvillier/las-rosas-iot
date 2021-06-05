package com.lasrosas.iot.ingestor.parser;

import java.util.HashMap;
import java.util.Map;

public class PayloadParsers {

	private Map<String, PayloadParser> parsers = new HashMap<>();

	public PayloadParsers(PayloadParser ... parsersArgs) {
		for(var parser : parsersArgs)
			add(parser);
	}

	private void add(PayloadParser parser) {
		parsers.put(parser.getManufacturer() + "/" + parser.getModel(), parser);
	}

	public PayloadParser getParser(String manufacturer, String model) {
		return parsers.get(manufacturer + "/" + model);
	}
}
