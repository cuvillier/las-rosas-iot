package com.lasrosas.iot.core.database.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lasrosas.iot.core.database.entities.alrm.Alarm;
import com.lasrosas.iot.core.database.entities.alrm.AlarmType;
import com.lasrosas.iot.core.database.entities.alrm.ThingAlarm;
import com.lasrosas.iot.core.database.entities.thg.Thing;

@Repository
public interface ThingAlarmRepo extends JpaRepository<ThingAlarm,Long>{
	ThingAlarm getByTypeAndThingAndStateNot(AlarmType type, Thing thing, Alarm.State state);
}
