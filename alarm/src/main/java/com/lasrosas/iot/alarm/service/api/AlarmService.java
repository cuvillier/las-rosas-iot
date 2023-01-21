package com.lasrosas.iot.alarm.service.api;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.messaging.Message;

import com.lasrosas.iot.alarm.database.entity.Alarm;
import com.lasrosas.iot.alarm.database.entity.AlarmGravity;
import com.lasrosas.iot.core.database.entities.dtw.DigitalTwin;
import com.lasrosas.iot.core.database.entities.thg.Thing;
import com.lasrosas.iot.notification.service.api.Notification;

public interface AlarmService {
	boolean clearAlarm(Thing thing, String type);
	Alarm openAlarm(LocalDateTime time, Thing thing, String type, String message, AlarmGravity gravity);
	Alarm ackAlarm(LocalDateTime time, Thing thing, String type);
	Alarm closeAlarm(LocalDateTime time, Thing thing, String type);

	boolean clearAlarm(DigitalTwin twin, String type);
	Alarm openAlarm(LocalDateTime time, DigitalTwin twin, String type, String message, AlarmGravity gravity);
	Alarm ackAlarm(LocalDateTime time, DigitalTwin twin, String type);
	Alarm closeAlarm(LocalDateTime time, DigitalTwin twin, String type);

	public List<Notification>  checkTelemetry(Message<Object> telemetry);
}
