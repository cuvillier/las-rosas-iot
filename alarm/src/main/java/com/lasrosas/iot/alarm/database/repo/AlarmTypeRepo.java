package com.lasrosas.iot.alarm.database.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lasrosas.iot.alarm.database.entity.AlarmType;

public interface AlarmTypeRepo extends JpaRepository<AlarmType, Long>{
	AlarmType getByName(String name);
}
