package com.lasrosas.iot.alarm.service.impl;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.google.gson.Gson;
import com.lasrosas.iot.alarm.database.entity.Alarm;
import com.lasrosas.iot.alarm.database.entity.AlarmGravity;
import com.lasrosas.iot.alarm.database.entity.AlarmState;
import com.lasrosas.iot.alarm.database.entity.AlarmThing;
import com.lasrosas.iot.alarm.database.entity.AlarmTwin;
import com.lasrosas.iot.alarm.database.entity.AlarmType;
import com.lasrosas.iot.alarm.database.entity.TelemetryWatcher;
import com.lasrosas.iot.alarm.database.repo.AlarmThingRepo;
import com.lasrosas.iot.alarm.database.repo.AlarmTwinRepo;
import com.lasrosas.iot.alarm.database.repo.AlarmTypeRepo;
import com.lasrosas.iot.alarm.database.repo.TelemetryWatcherRepo;
import com.lasrosas.iot.alarm.service.api.AlarmChange;
import com.lasrosas.iot.alarm.service.api.AlarmService;
import com.lasrosas.iot.core.database.entities.dtw.DigitalTwin;
import com.lasrosas.iot.core.database.entities.thg.Thing;
import com.lasrosas.iot.core.database.repo.DigitalTwinRepo;
import com.lasrosas.iot.core.database.repo.ThingRepo;
import com.lasrosas.iot.core.shared.telemetry.Telemetry;
import com.lasrosas.iot.core.shared.utils.LasRosasHeaders;

@Transactional
public class AlarmServiceImpl implements AlarmService {
	public static final Logger log = LoggerFactory.getLogger(AlarmServiceImpl.class);

	@Autowired
	private AlarmThingRepo alarmThingRepo;

	@Autowired
	private AlarmTwinRepo alarmTwinRepo;

	@Autowired
	private AlarmTypeRepo alarmTypeRepo;

	@Autowired
	private ThingRepo thingRepo;

	@Autowired
	private DigitalTwinRepo twinRepo;

	@Autowired
	private TelemetryWatcherRepo watcherRepo;
	
	@PersistenceContext
	private EntityManager em;

	@Autowired
	private Gson gson;

	@Override
	public Alarm openAlarm(LocalDateTime time, Thing thing, Class<?> dataType, String cause, AlarmGravity gravity) {
		return openAlarm(time, thing, null, dataType, cause, gravity);
	}

	@Override
	public Alarm openAlarm(LocalDateTime time, DigitalTwin twin, Class<?> dataType, String cause, AlarmGravity gravity) {
		return openAlarm(time, null, twin, dataType, cause, gravity);
	}

	private Alarm openAlarm(LocalDateTime time, Thing thing, DigitalTwin twin, Class<?> dataType, String cause, AlarmGravity gravity) {
		Assert.notNull(cause, "cause cannot be null");

		var mayBeAlarmType = alarmTypeRepo.findByDataTypeAndCause(dataType(dataType), cause);
		AlarmType alarmType;
		Optional<? extends Alarm> mayBeAlarm;

		// Create if needed.
		if( mayBeAlarmType.isEmpty() ) {
			alarmType = new AlarmType(dataType(dataType), cause);
			em.persist(alarmType);
			mayBeAlarm = Optional.empty();
		} else {
			alarmType = mayBeAlarmType.get();
			if(thing != null)
				mayBeAlarm = alarmThingRepo.findByThingAndType(thing, alarmType);
			else
				mayBeAlarm = alarmTwinRepo.findByTwinAndType(twin, alarmType);
		}

		Alarm alarm = null;
		if( mayBeAlarm.isEmpty() ) {
			
			if(thing !=  null)
				alarm = new AlarmThing(thing, time, alarmType, gravity);
			else
				alarm = new AlarmTwin(twin, time, alarmType, gravity);

			em.persist(alarm);
		} else {
			alarm = mayBeAlarm.get();

			if( alarm.getGravity() != gravity ) {
				alarm.setGravity(gravity);
			} else
				alarm = null; 	// no change
		}

		if(alarm != null)
			log.info("Alarm changed state=" + alarm.getState() + ", gravity=" + alarm.getGravity() + " thing=" + alarm.getSourceNaturalId() + ", cause=" + cause);

		return alarm;
	}

	private String dataType(Class<?> dataType) {
		return dataType.getSimpleName();
	}

	private Alarm setAlarmState(LocalDateTime time, Thing thing, DigitalTwin twin, Class<?> dataType, String cause, AlarmState state, AlarmGravity gravity) {
		Assert.notNull(cause, "cause cannot be null");

		var mayBeAlarmType = alarmTypeRepo.findByDataTypeAndCause(dataType(dataType), cause);
		if(mayBeAlarmType.isEmpty()) return null;

		var alarmType = mayBeAlarmType.get();

		Optional<? extends Alarm> mayBeAlarm;
		if(thing != null) 
			mayBeAlarm = alarmThingRepo.findByThingAndType(thing, alarmType);
		else
			mayBeAlarm = alarmTwinRepo.findByTwinAndType(twin, alarmType);

		if(mayBeAlarm.isEmpty()) return null;

		var alarm = mayBeAlarm.get();
		if(alarm.getState() != state) {
			alarm.setState(state, time);
		} else
			alarm = null; // No change

		if( alarm != null && gravity != null && alarm.getGravity() != gravity ) {
			alarm.setGravity(gravity);
		}

		if(alarm != null)
			log.info("Alarm changed state=" + alarm.getState() + ", gravity=" + alarm.getGravity() + " thing=" + alarm.getSourceNaturalId() + ", cause=" + cause);

		return alarm;
	}

	@Override
	public Alarm ackAlarm(LocalDateTime time, Thing thing, Class<?> dataType, String cause, AlarmGravity gravity) {
		return setAlarmState(time, thing, null, dataType, cause, AlarmState.ACKNOWLEDGE, gravity);
	}

	@Override
	public Alarm ackAlarm(LocalDateTime time, DigitalTwin twin, Class<?> dataType, String cause, AlarmGravity gravity) {
		return setAlarmState(time, null, twin, dataType, cause, AlarmState.ACKNOWLEDGE, gravity);
	}

	@Override
	public Alarm closeAlarm(LocalDateTime time, Thing thing, Class<?> dataType, String cause) {
		return setAlarmState(time, thing, null, dataType, cause, AlarmState.CLOSED, null);
	}

	@Override
	public Alarm closeAlarm(LocalDateTime time, DigitalTwin twin, Class<?> dataType, String cause) {
		return setAlarmState(time, null, twin, dataType, cause, AlarmState.CLOSED, null);
	}

	@Override
	public boolean clearAlarm(Thing thing, Class<?> dataType, String cause) {
		return clearAlarm(thing, null, dataType, cause);		
	}

	@Override
	public boolean clearAlarm(DigitalTwin twin, Class<?> dataType, String cause) {
		return clearAlarm(null, twin, dataType, cause);
	}

	public boolean clearAlarm(Thing thing, DigitalTwin twin, Class<?> dataType, String cause) {
		var mayBeAlarmType = alarmTypeRepo.findByDataTypeAndCause(dataType(dataType), cause);
		if(mayBeAlarmType.isEmpty()) return false;
		var alarmType = mayBeAlarmType.get();

		Optional<? extends Alarm> mayBeAlarm;
		if(thing != null) 
			mayBeAlarm = alarmThingRepo.findByThingAndType(thing, alarmType);
		else
			mayBeAlarm = alarmTwinRepo.findByTwinAndType(twin, alarmType);

		if(mayBeAlarm.isEmpty()) return false;
		var alarm = mayBeAlarm.get();

		em.remove(alarm);
		return false;
	}

	public List<AlarmChange> checkTelemetry(Message<Object> imessage) {

		List<TelemetryWatcher> watchers;
		var payload = imessage.getPayload();

		var result = new ArrayList<AlarmChange>();

		var mayBeTwinId = LasRosasHeaders.twinId(imessage);
		if(mayBeTwinId.isPresent() ) {
			var twinId = mayBeTwinId.get();
			var twin = thingRepo.getReferenceById(twinId);
			watchers = watcherRepo.findMatchingWatchers(twin, payload);

			for(var watcher: watchers) {				Alarm alarm;
				var cause = watcher.getCause();
				var evaluation = evaluate(watcher, payload);
				
				if(evaluation == null) continue;

				if(evaluation )
					alarm = this.openAlarm(null, twin, getClass(), cause, watcher.getGravity());
				else
					alarm = this.closeAlarm(null, twin, getClass(), cause);

				if( alarm != null ) {
					var time = LasRosasHeaders.timeReceived(imessage);
					var alarmChange = new AlarmChange(
											time, 
											alarm.getState(), 
											alarm.getType().getDataType(), 
											alarm.getType().getCause(),
											alarm.getGravity());
					result.add(alarmChange);
				}
			}
		} else {
			var mayBeThingId = LasRosasHeaders.thingId(imessage);
			
			if( mayBeThingId.isEmpty() ) {
				log.warn("No twinId, no thingId");
				return result;
			}

			var thingId = mayBeThingId.get();
			var thing = thingRepo.getReferenceById(thingId);
			watchers = watcherRepo.findMatchingWatchers(thing, payload);

			for(var watcher: watchers) {
				Alarm alarm;
				var cause = watcher.getCause();
				var evaluation = evaluate(watcher, payload);
				if(evaluation == null) continue;
				
				if(evaluation ) 
					alarm = this.openAlarm(null, thing, getClass(), cause, watcher.getGravity());
				else
					alarm = this.closeAlarm(null, thing, getClass(), cause);

				if( alarm != null ) {
					var time = LasRosasHeaders.timeReceived(imessage);
					var alarmChange = new AlarmChange(
											time, 
											alarm.getState(), 
											alarm.getType().getDataType(), 
											alarm.getType().getCause(),
											alarm.getGravity());
					result.add(alarmChange);
				}
			}
		}

		return result;
	}

	private Boolean evaluate(TelemetryWatcher watcher, Object payload) {
		var fieldName = watcher.getTriggerField();
		Field field;
		try {
			field = payload.getClass().getDeclaredField(fieldName);
		} catch (Exception e1) {
			throw new RuntimeException("Cannot get field " + fieldName + " in class " + payload.getClass().getSimpleName());
		}

		if( field == null ) return null;	// Cannot be evaluated
		field.setAccessible(true);

		try {
			var value = field.get(payload);
			if( value == null ) return null;
			if( !(value instanceof Number)) return null;	// Cannot be evaluated

			var dvalue = ((Number)value).doubleValue();

			switch(watcher.getTriggerOperator()) {
			case SUPERIOR:
				return dvalue > watcher.getTriggerValue();
			case INFERIOR:
				return dvalue > watcher.getTriggerValue();
			case EQUALS:
				return dvalue > watcher.getTriggerValue();
			case DIFFERENT:
				return dvalue > watcher.getTriggerValue();
			default:
				throw new RuntimeException("Unknown triggerOperator, fix the code.");
			}
		} catch(Exception e) {
			log.warn("Watcher evaluation failed : ", e);
			return false;
		}
	}
}
