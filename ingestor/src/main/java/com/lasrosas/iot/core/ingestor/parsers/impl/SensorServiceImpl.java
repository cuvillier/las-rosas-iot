package com.lasrosas.iot.core.ingestor.parsers.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lasrosas.iot.core.database.repo.ThingRepo;
import com.lasrosas.iot.core.ingestor.parsers.api.SensorService;
import com.lasrosas.iot.core.ingestor.parsers.api.ThingDataMessage;
import com.lasrosas.iot.core.ingestor.parsers.api.ThingEncodedMessage;
import com.lasrosas.iot.core.shared.telemetry.BatteryLevel;
import com.lasrosas.iot.core.shared.telemetry.Order;
import com.lasrosas.iot.core.shared.telemetry.Telemetry;
import com.lasrosas.iot.core.shared.utils.LasRosasHeaders;
import com.lasrosas.iot.core.shared.utils.NotFoundException;

public class SensorServiceImpl implements SensorService {
	public static final Logger log = LoggerFactory.getLogger(SensorServiceImpl.class);

	private Gson gson = new GsonBuilder().setPrettyPrinting().create();

	@Autowired
	private ThingRepo thgRepo;

	private Map<String, PayloadParser> parsers = new HashMap<>();

	public SensorServiceImpl(PayloadParser ... parsersArgs) {
		for(var parser : parsersArgs)
			add(parser);
	}

	private void add(PayloadParser parser) {
		parsers.put(parser.getManufacturer().toLowerCase() + "/" + parser.getModel().toLowerCase(), parser);
	}

	public PayloadParser getParser(String manufacturer, String model) {
		var key = manufacturer.toLowerCase() + "/" + model.toLowerCase();
		log.info(gson.toJson(parsers));
		return parsers.get(key);
	}

	@Override
	@Transactional
	public Message<? extends ThingDataMessage> decodeUplink(Message<ThingEncodedMessage> imessage) {
		var thingId = imessage.getHeaders().get(LasRosasHeaders.THING_ID, Long.class);
		var thing = thgRepo.getOne(thingId);

		var parser = getParser(thing.getType().getManufacturer(), thing.getType().getModel());
		if(parser == null) throw new NotFoundException("Parser for sensor manufacturer=" + thing.getType().getManufacturer() + ", model="+ thing.getType().getModel());
		var uplink = parser.decodeUplink(imessage);

		log.info("Decoded message: " + gson.toJson(uplink));

		return uplink;
	}

	/*
	private void handleBatteryLevel(Thing thing, LocalDateTime time, BatteryLevel batteryLevel) {
		boolean newAlarm = false;

		var thingType = thing.getType();

		if (batteryLevel.getAlarm() != null) {
			newAlarm = batteryLevel.getAlarm();

		} else if (batteryLevel.getPercentage() != null) {
			var percentage = batteryLevel.getPercentage();

			if (thingType.getBatteryMinPercentage() != null && percentage < thingType.getBatteryMinPercentage()) {
				newAlarm = true;
			}
		}

		var alarmType = altRepo.getByName(AlarmType.THING_BATTERY_ALARM);
		var alarm = alrRepo.getByTypeAndThingAndStateNot(alarmType, thing, Alarm.State.Closed);

		if (alarm == null && newAlarm) {

			// Activate battery alarm
			alarm = new ThingAlarm(thing, alarmType, time);
			alrRepo.save(alarm);

		} else if (alarm != null && !newAlarm) {

			// Close the battery alarm
			alarm.close(time);
		}
	}
*/
	@Override
	public Collection<Message<Telemetry>> telemetries(Message<ThingDataMessage> imessage) {

		var thingId = LasRosasHeaders.thingid(imessage).get();
		var thing = thgRepo.getOne(thingId);
		var parser = getParser(thing.getType().getManufacturer(), thing.getType().getModel());

		var result = parser.telemetries(imessage);

		// Handle battery level here.

		// TODO. Just ignore the BatteryLevel messages
		result.removeIf(e -> e.getPayload() instanceof BatteryLevel);

		for(var message: result)
			log.info("Decoded message: " + gson.toJson(message));

		return result;
	}

	@Override
	public byte[] encodeOrder(Message<? extends Order> imessage) {
		var thingId = imessage.getHeaders().get(LasRosasHeaders.THING_ID, Long.class);
		var thing = thgRepo.getOne(thingId);

		var parser = getParser(thing.getType().getManufacturer(), thing.getType().getModel());
		if(parser == null) throw new NotFoundException("Parser for sensor manufacturer=" + thing.getType().getManufacturer() + ", model="+ thing.getType().getModel());
		return parser.encodeOrder(imessage.getPayload());
	}
}
