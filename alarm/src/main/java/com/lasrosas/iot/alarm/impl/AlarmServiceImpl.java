package com.lasrosas.iot.alarm.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.lasrosas.iot.alarm.api.AlarmService;
import com.lasrosas.iot.alarm.database.entity.AlarmState;
import com.lasrosas.iot.alarm.database.entity.AlarmThing;
import com.lasrosas.iot.alarm.database.entity.AlarmType;
import com.lasrosas.iot.alarm.database.repo.AlarmThingRepo;
import com.lasrosas.iot.alarm.database.repo.AlarmTypeRepo;
import com.lasrosas.iot.core.database.entities.thg.Thing;

public class AlarmServiceImpl implements AlarmService {
	public static final Logger log = LoggerFactory.getLogger(AlarmServiceImpl.class);

	@Autowired
	private AlarmThingRepo alarmThingRepo;

	@Autowired
	private AlarmTypeRepo alarmTypeRepo;

	@PersistenceContext
	private EntityManager em;

	@Override
	public boolean openAlarm(LocalDateTime time, Thing thing, Class<?> dataType, String cause) {
		Assert.notNull(cause, "cause cannot be null");

		var mayBeAlarmType = alarmTypeRepo.findByDataTypeAndCause(dataType(dataType), cause);
		AlarmType alarmType;
		Optional<AlarmThing> mayBeAlarm;

		// Create if needed.
		if( mayBeAlarmType.isEmpty() ) {
			alarmType = new AlarmType(dataType(dataType), cause);
			em.persist(alarmType);
			mayBeAlarm = Optional.empty();
		} else {
			alarmType = mayBeAlarmType.get();
			mayBeAlarm = alarmThingRepo.findByThingAndType(thing, alarmType);
		}

		AlarmThing alarm = null;
		boolean changed = false;
		if( mayBeAlarm.isEmpty() ) {
			alarm = new AlarmThing(thing, time, alarmType);
			em.persist(alarm);
			changed = true;
		}

		if(changed)
			log.info("Alarm state changed to " + alarm.getState() + " thing=" + alarm.getThing().getNaturalId() + ", cause=" + cause);

		return changed;
	}

	private String dataType(Class<?> dataType) {
		return dataType.getSimpleName();
	}

	private boolean setAlarmState(LocalDateTime time, Thing thing, Class<?> dataType, String cause, AlarmState state) {
		Assert.notNull(cause, "cause cannot be null");

		var alarmType = alarmTypeRepo.findByDataTypeAndCause(dataType(dataType), cause).orElseThrow();
		var alarm = alarmThingRepo.findByThingAndType(thing, alarmType).orElseThrow();

		boolean changed = false;
		if(alarm.getState() != state) {
			alarm.setState(state, time);
			changed = true;
		}

		if(changed)
			log.info("Alarm state changed to " + alarm.getState() + " thing=" + alarm.getThing().getNaturalId() + ", cause=" + cause);

		return changed;
	}

	@Override
	public boolean ackAlarm(LocalDateTime time, Thing thing, Class<?> dataType, String cause) {
		return setAlarmState(time, thing, dataType, cause, AlarmState.ACKNOWLEDGE);
	}

	@Override
	public boolean closeAlarm(LocalDateTime time, Thing thing, Class<?> dataType, String cause) {
		return setAlarmState(time, thing, dataType, cause, AlarmState.CLOSED);
	}

	@Override
	public boolean clearAlarm(Thing thing, Class<?> dataType, String cause) {
		var mayBeAlarmType = alarmTypeRepo.findByDataTypeAndCause(dataType(dataType), cause);
		if(mayBeAlarmType.isEmpty()) return false;
		var alarmType = mayBeAlarmType.get();

		var mayBeAlarm = alarmThingRepo.findByThingAndType(thing, alarmType);
		if(mayBeAlarm.isEmpty()) return false;
		var alarm = mayBeAlarm.get();

		em.remove(alarm);
		return false;
	}
}
