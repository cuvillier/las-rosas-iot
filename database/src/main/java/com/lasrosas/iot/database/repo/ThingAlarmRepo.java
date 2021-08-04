package com.lasrosas.iot.database.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lasrosas.iot.database.entities.alrm.Alarm;
import com.lasrosas.iot.database.entities.alrm.AlarmType;
import com.lasrosas.iot.database.entities.alrm.ThingAlarm;
import com.lasrosas.iot.database.entities.thg.Thing;

@Repository
public interface ThingAlarmRepo extends JpaRepository<ThingAlarm,Long>{
	ThingAlarm getByTypeAndThingAndStateNot(AlarmType type, Thing thing, Alarm.State state);
}
