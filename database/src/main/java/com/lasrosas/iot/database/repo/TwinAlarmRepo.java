package com.lasrosas.iot.database.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lasrosas.iot.database.entities.alrm.AlarmType;
import com.lasrosas.iot.database.entities.alrm.TwinAlarm;
import com.lasrosas.iot.database.entities.dtw.DigitalTwin;

public interface TwinAlarmRepo extends JpaRepository<TwinAlarm, Long>{
	TwinAlarm getByTwinAndType(DigitalTwin twin, AlarmType type);
}
