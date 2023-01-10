package com.lasrosas.iot.alarm.database.repo;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.lasrosas.iot.alarm.database.entity.AlarmThing;
import com.lasrosas.iot.alarm.database.entity.AlarmType;
import com.lasrosas.iot.core.database.entities.thg.Thing;

@Repository
public interface AlarmThingRepo extends AlarmRepoBase<AlarmThing> {
	Optional<AlarmThing> findByThingAndType(Thing thing, AlarmType alarmType);
}
