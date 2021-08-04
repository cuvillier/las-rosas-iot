package com.lasrosas.iot.ingestor.services.sensors.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.lasrosas.iot.database.repo.ThingRepo;
import com.lasrosas.iot.ingestor.services.sensors.api.SensorService;
import com.lasrosas.iot.ingestor.services.sensors.api.ThingDataMessage;
import com.lasrosas.iot.ingestor.services.sensors.api.ThingEncodedMessage;
import com.lasrosas.iot.shared.telemetry.Telemetry;
import com.lasrosas.iot.shared.utils.NotFoundException;

public class SensorServiceImpl implements SensorService {

	@Autowired
	private ThingRepo thgRepo;

	private Map<String, PayloadParser> parsers = new HashMap<>();

	public SensorServiceImpl(PayloadParser ... parsersArgs) {
		for(var parser : parsersArgs)
			add(parser);
	}

	private void add(PayloadParser parser) {
		parsers.put(parser.getManufacturer() + "/" + parser.getModel(), parser);
	}

	public PayloadParser getParser(String manufacturer, String model) {
		return parsers.get(manufacturer + "/" + model);
	}

	public ThingDataMessage decodeUplink(ThingEncodedMessage message) {
		var thing = thgRepo.getOne(message.getThingid());
		var parser = getParser(thing.getType().getManufacturer(), thing.getType().getModel());
		if(parser == null) throw new NotFoundException("Parser for sensor manufacturer=" + thing.getType().getManufacturer() + ", model="+ thing.getType().getModel());
		var result = parser.decodeUplink(message.decodeData());

		result.setThingid(message.getThingid());
		result.setTime(message.getTime());

		return result;
	}

	@Override
	public Collection<Telemetry> telemetries(ThingDataMessage message) {
		var thing = thgRepo.getOne(message.getThingid());
		var parser = getParser(thing.getType().getManufacturer(), thing.getType().getModel());
		return parser.telemetries(message);
	}
}
