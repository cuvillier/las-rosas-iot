package com.lasrosas.iot.alarm.api;

import java.time.LocalDateTime;

import com.lasrosas.iot.core.database.entities.thg.Thing;

public interface AlarmService {
	boolean clearAlarm(Thing thing, Class<?> dataType, String cause);
	boolean openAlarm(LocalDateTime time, Thing thing, Class<?> dataType, String cause);
	boolean ackAlarm(LocalDateTime time, Thing thing, Class<?> dataType, String cause);
	boolean closeAlarm(LocalDateTime time, Thing thing, Class<?> dataType, String cause);
}
