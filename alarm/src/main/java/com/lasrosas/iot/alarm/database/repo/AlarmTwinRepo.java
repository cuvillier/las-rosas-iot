package com.lasrosas.iot.alarm.database.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lasrosas.iot.alarm.database.entity.AlarmTwin;
import com.lasrosas.iot.alarm.database.entity.AlarmType;
import com.lasrosas.iot.core.database.entities.dtw.DigitalTwin;

@Repository
public interface AlarmTwinRepo extends AlarmRepoBase<AlarmTwin> {

	@Query("SELECT alr from AlarmTwin alr WHERE twin=?1 AND type=?2 AND state != 'CLOSED'")
	Optional<AlarmTwin> findByTwinAndType(DigitalTwin twin, AlarmType type);
}
