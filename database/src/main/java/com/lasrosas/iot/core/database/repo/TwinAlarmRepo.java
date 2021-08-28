package com.lasrosas.iot.core.database.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lasrosas.iot.core.database.entities.alrm.AlarmType;
import com.lasrosas.iot.core.database.entities.alrm.TwinAlarm;
import com.lasrosas.iot.core.database.entities.dtw.DigitalTwin;

public interface TwinAlarmRepo extends JpaRepository<TwinAlarm, Long>{
	TwinAlarm getByTwinAndType(DigitalTwin twin, AlarmType type);
}
