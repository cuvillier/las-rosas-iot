package com.lasrosas.iot.core.database.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lasrosas.iot.core.database.entities.alrm.AlarmType;

public interface AlarmTypeRepo extends JpaRepository<AlarmType, Long>{
	AlarmType getByName(String name);
}
