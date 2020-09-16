package com.lasrosas.iot.services.lora.parser;

import java.util.HashMap;
import java.util.Map;

import com.lasrosas.iot.services.lora.parser.impl.adenuis.AdenuisARF8180BAParser;
import com.lasrosas.iot.services.lora.parser.impl.elsys.ElsysERSParser;

public class PayloadParsers {
	private Map<String, PayloadParser> parsers = new HashMap<>();

	public PayloadParsers() {
		add(new AdenuisARF8180BAParser());
		add(new ElsysERSParser());
	}

	private void add(PayloadParser parser) {
		parsers.put(parser.getManufacturer() + "/" + parser.getModel(), parser);
	}

	public PayloadParser getParser(String manufacturer, String model) {
		return parsers.get(manufacturer + "/" + model);
	}
}
