package com.lasrosas.iot.database.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lasrosas.iot.database.entities.alrm.AlarmType;

public interface AlarmTypeRepo extends JpaRepository<AlarmType, Long>{
	AlarmType getByName(String name);
}
