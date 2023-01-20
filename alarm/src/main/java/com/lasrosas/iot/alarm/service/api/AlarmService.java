package com.lasrosas.iot.alarm.service.api;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.messaging.Message;

import com.lasrosas.iot.alarm.database.entity.Alarm;
import com.lasrosas.iot.alarm.database.entity.AlarmGravity;
import com.lasrosas.iot.core.database.entities.dtw.DigitalTwin;
import com.lasrosas.iot.core.database.entities.thg.Thing;

public interface AlarmService {
	boolean clearAlarm(Thing thing, Class<?> dataType, String cause);
	Alarm openAlarm(LocalDateTime time, Thing thing, Class<?> dataType, String cause, AlarmGravity gravity);
	Alarm ackAlarm(LocalDateTime time, Thing thing, Class<?> dataType, String cause, AlarmGravity gravity);
	Alarm closeAlarm(LocalDateTime time, Thing thing, Class<?> dataType, String cause);

	boolean clearAlarm(DigitalTwin twin, Class<?> dataType, String cause);
	Alarm openAlarm(LocalDateTime time, DigitalTwin twin, Class<?> dataType, String cause, AlarmGravity gravity);
	Alarm ackAlarm(LocalDateTime time, DigitalTwin twin, Class<?> dataType, String cause, AlarmGravity gravity);
	Alarm closeAlarm(LocalDateTime time, DigitalTwin twin, Class<?> dataType, String cause);

	public List<AlarmChange>  checkTelemetry(Message<Object> telemetry);
}
