package com.lasrosas.iot.alarm.database.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lasrosas.iot.alarm.database.entity.AlarmType;
import com.lasrosas.iot.alarm.database.entity.TwinAlarm;
import com.lasrosas.iot.core.database.entities.dtw.DigitalTwin;

public interface TwinAlarmRepo extends JpaRepository<TwinAlarm, Long>{
	TwinAlarm getByTwinAndType(DigitalTwin twin, AlarmType type);
}
