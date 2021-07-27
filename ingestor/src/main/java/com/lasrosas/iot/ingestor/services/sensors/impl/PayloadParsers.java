package com.lasrosas.iot.ingestor.services.sensors.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;
import com.lasrosas.iot.ingestor.services.lora.api.LoraMessage;
import com.lasrosas.iot.ingestor.shared.ThingMessage;
import com.lasrosas.iot.shared.utils.NotFoundException;

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

	public List<ThingMessage> parse(LoraMessage loraMessage) {
		
	}
}
