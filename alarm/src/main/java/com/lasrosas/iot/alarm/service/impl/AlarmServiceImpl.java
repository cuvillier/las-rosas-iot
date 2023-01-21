package com.lasrosas.iot.alarm.service.impl;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

import com.google.gson.Gson;
import com.lasrosas.iot.alarm.database.entity.Alarm;
import com.lasrosas.iot.alarm.database.entity.AlarmGravity;
import com.lasrosas.iot.alarm.database.entity.AlarmState;
import com.lasrosas.iot.alarm.database.entity.AlarmThing;
import com.lasrosas.iot.alarm.database.entity.AlarmTwin;
import com.lasrosas.iot.alarm.database.entity.TelemetryWatcher;
import com.lasrosas.iot.alarm.database.repo.AlarmThingRepo;
import com.lasrosas.iot.alarm.database.repo.AlarmTwinRepo;
import com.lasrosas.iot.alarm.database.repo.TelemetryWatcherRepo;
import com.lasrosas.iot.alarm.service.api.AlarmService;
import com.lasrosas.iot.core.database.entities.dtw.DigitalTwin;
import com.lasrosas.iot.core.database.entities.thg.Thing;
import com.lasrosas.iot.core.database.repo.DigitalTwinRepo;
import com.lasrosas.iot.core.database.repo.ThingRepo;
import com.lasrosas.iot.core.shared.utils.LasRosasHeaders;
import com.lasrosas.iot.notification.service.api.Notification;
import com.lasrosas.iot.notification.service.api.NotificationPriority;

@Transactional
public class AlarmServiceImpl implements AlarmService {
	public static final Logger log = LoggerFactory.getLogger(AlarmServiceImpl.class);

	@Autowired
	private AlarmThingRepo alarmThingRepo;

	@Autowired
	private AlarmTwinRepo alarmTwinRepo;

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
	public Alarm openAlarm(LocalDateTime time, Thing thing, String type, String message, AlarmGravity gravity) {
		return openAlarm(time, thing, null, type, message, gravity);
	}

	@Override
	public Alarm openAlarm(LocalDateTime time, DigitalTwin twin, String type, String message, AlarmGravity gravity) {
		return openAlarm(time, null, twin, type, message, gravity);
	}

	private Alarm openAlarm(LocalDateTime time, Thing thing, DigitalTwin twin, String type, String message, AlarmGravity gravity) {
		Optional<? extends Alarm> mayBeAlarm;
		if(thing != null)
			mayBeAlarm = alarmThingRepo.findByThingAndType(thing, type);
		else
			mayBeAlarm = alarmTwinRepo.findByTwinAndType(twin, type);

		Alarm alarm = null;
		if( mayBeAlarm.isEmpty() ) {
			
			if(thing !=  null)
				alarm = new AlarmThing(thing, time, type, message, gravity);
			else
				alarm = new AlarmTwin(twin, time, type, message, gravity);

			em.persist(alarm);
		} else {
			alarm = mayBeAlarm.get();

			if( alarm.getGravity() != gravity ) {
				alarm.setGravity(gravity);
			} else
				alarm = null; 	// no change
		}

		if(alarm != null)
			log.info("Alarm changed state=" + alarm.getState() + ", gravity=" + alarm.getGravity() + " thing=" + alarm.getSourceNaturalId() + ", type=" + type);

		return alarm;
	}

	private Alarm setAlarmState(LocalDateTime time, Thing thing, DigitalTwin twin, String type, AlarmState state) {
		return setAlarmState(time, thing, twin, type, state, null, null);
	}

	private Alarm setAlarmState(LocalDateTime time, Thing thing, DigitalTwin twin, String type, AlarmState state, String message, AlarmGravity gravity) {

		Optional<? extends Alarm> mayBeAlarm;
		if(thing != null) 
			mayBeAlarm = alarmThingRepo.findByThingAndType(thing, type);
		else
			mayBeAlarm = alarmTwinRepo.findByTwinAndType(twin, type);

		if(mayBeAlarm.isEmpty()) return null;

		var alarm = mayBeAlarm.get();
		if(alarm.getState() != state) {
			alarm.setState(state, time);
		} else
			alarm = null; // No change

		if(alarm != null)
			log.info("Alarm changed state=" + alarm.getState() + ", gravity=" + alarm.getGravity() + " thing=" + alarm.getSourceNaturalId() + ", type=" + type + ", message=" + message);

		return alarm;
	}

	@Override
	public Alarm ackAlarm(LocalDateTime time, Thing thing, String type) {
		return setAlarmState(time, thing, null, type, AlarmState.ACKNOWLEDGE);
	}

	@Override
	public Alarm ackAlarm(LocalDateTime time, DigitalTwin twin, String type) {
		return setAlarmState(time, null, twin, type, AlarmState.ACKNOWLEDGE);
	}

	@Override
	public Alarm closeAlarm(LocalDateTime time, Thing thing, String type) {
		return setAlarmState(time, thing, null, type, AlarmState.CLOSED);
	}

	@Override
	public Alarm closeAlarm(LocalDateTime time, DigitalTwin twin, String type) {
		return setAlarmState(time, null, twin, type, AlarmState.CLOSED);
	}

	@Override
	public boolean clearAlarm(Thing thing, String type) {
		return clearAlarm(thing, null, type);		
	}

	@Override
	public boolean clearAlarm(DigitalTwin twin, String type) {
		return clearAlarm(null, twin, type);
	}

	public boolean clearAlarm(Thing thing, DigitalTwin twin, String type) {
		Optional<? extends Alarm> mayBeAlarm;
		if(thing != null) 
			mayBeAlarm = alarmThingRepo.findByThingAndType(thing, type);
		else
			mayBeAlarm = alarmTwinRepo.findByTwinAndType(twin, type);

		if(mayBeAlarm.isEmpty()) return false;
		var alarm = mayBeAlarm.get();

		em.remove(alarm);
		return false;
	}

	public List<Notification> checkTelemetry(Message<Object> imessage) {

		List<TelemetryWatcher> watchers;
		var payload = imessage.getPayload();

		var result = new ArrayList<Notification>();

		var mayBeTwinId = LasRosasHeaders.twinId(imessage);

		if(mayBeTwinId.isPresent() ) {
			var twinId = mayBeTwinId.get();
			var twin = thingRepo.getReferenceById(twinId);
			watchers = watcherRepo.findMatchingWatchers(twin, payload);

			for(var watcher: watchers) {
				Alarm alarm;
				var type = watcher.getType();
				var evaluation = evaluate(watcher, payload);

				if(evaluation == null) continue;

				if(evaluation ) {
					var message = watcher.getMessage();
					alarm = this.openAlarm(null, twin, type, message, watcher.getGravity());
					result.add(notifyAlarm(imessage, twin, alarm));
				} else
					alarm = this.closeAlarm(null, twin, type);
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
				var type = watcher.getMessage();
				var evaluation = evaluate(watcher, payload);
				if(evaluation == null) continue;

				if(evaluation ) {
					var message = watcher.getMessage();
					alarm = this.openAlarm(null, thing, type, message, watcher.getGravity());
					result.add(notifyAlarm(imessage, thing, alarm));
				} else
					alarm = this.closeAlarm(null, thing, type);
			}
		}

		return result;
	}

	private Notification notifyAlarm(Message<?> imessage, Thing thing, Alarm alarm) {
		var title = String.format(
				"Thing %s of type %s/%s",
				thing.getNaturalId(),
				thing.getType().getManufacturer(),
				thing.getType().getModel());
		return notifyAlarm(imessage, title, alarm);
	}

	private Notification notifyAlarm(Message<?> imessage, DigitalTwin twin, Alarm alarm) {
		var title = String.format(
					"Twin %s of type %s",
					twin.getName(),
					twin.getType().getName());
		return notifyAlarm(imessage, title, alarm);
	}

	private Notification notifyAlarm(Message<?> imessage, String title, Alarm alarm) {

		var time = LasRosasHeaders.timeReceived(imessage);
	        var formatter = DateTimeFormatter.ofPattern("dd MM yyyy HH:mm:ss");
			var content = String.format("%s: %s",
					formatter.format(time),
					alarm.getMessage()
				);

		return Notification.create(NotificationPriority.DEFAULT, title, content);
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
