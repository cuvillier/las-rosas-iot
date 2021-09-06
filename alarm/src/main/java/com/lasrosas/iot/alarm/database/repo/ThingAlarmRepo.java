package com.lasrosas.iot.alarm.database.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lasrosas.iot.alarm.database.entity.Alarm;
import com.lasrosas.iot.alarm.database.entity.AlarmType;
import com.lasrosas.iot.alarm.database.entity.ThingAlarm;
import com.lasrosas.iot.core.database.entities.thg.Thing;

@Repository
public interface ThingAlarmRepo extends JpaRepository<ThingAlarm,Long>{
	ThingAlarm getByTypeAndThingAndStateNot(AlarmType type, Thing thing, Alarm.State state);
}
