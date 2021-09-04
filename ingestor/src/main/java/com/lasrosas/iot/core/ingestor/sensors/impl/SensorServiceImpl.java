package com.lasrosas.iot.core.ingestor.sensors.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.transaction.annotation.Transactional;

import com.lasrosas.iot.core.database.repo.ThingRepo;
import com.lasrosas.iot.core.ingestor.sensors.api.SensorService;
import com.lasrosas.iot.core.ingestor.sensors.api.ThingDataMessage;
import com.lasrosas.iot.core.ingestor.sensors.api.ThingEncodedMessage;
import com.lasrosas.iot.core.shared.telemetry.BatteryLevel;
import com.lasrosas.iot.core.shared.telemetry.Telemetry;
import com.lasrosas.iot.core.shared.utils.LasRosasHeaders;
import com.lasrosas.iot.core.shared.utils.NotFoundException;

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

	@Override
	@Transactional
	public Message<? extends ThingDataMessage> decodeUplink(Message<ThingEncodedMessage> imessage) {
		var thingId = imessage.getHeaders().get(LasRosasHeaders.THING_ID, Long.class);
		var thing = thgRepo.getOne(thingId);

		var parser = getParser(thing.getType().getManufacturer(), thing.getType().getModel());
		if(parser == null) throw new NotFoundException("Parser for sensor manufacturer=" + thing.getType().getManufacturer() + ", model="+ thing.getType().getModel());
		return parser.decodeUplink(imessage);
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

		var thingId = LasRosasHeaders.thingid(imessage);
		var thing = thgRepo.getOne(thingId);
		var parser = getParser(thing.getType().getManufacturer(), thing.getType().getModel());

		var result = parser.telemetries(imessage);
		
		
		// Handle battery level here.

		// TODO. Just ignore the BatteryLevel messages
		result.removeIf(e -> e.getPayload() instanceof BatteryLevel);

		return result;
	}
}
