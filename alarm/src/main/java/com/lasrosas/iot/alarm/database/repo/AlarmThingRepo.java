package com.lasrosas.iot.alarm.database.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lasrosas.iot.alarm.database.entity.AlarmThing;
import com.lasrosas.iot.core.database.entities.thg.Thing;

@Repository
public interface AlarmThingRepo extends AlarmRepoBase<AlarmThing> {

	@Query("SELECT alr from AlarmThing alr WHERE thing=?1 AND type=?2 AND state != 'CLOSED'")
	Optional<AlarmThing> findByThingAndType(Thing thing, String type);
}
