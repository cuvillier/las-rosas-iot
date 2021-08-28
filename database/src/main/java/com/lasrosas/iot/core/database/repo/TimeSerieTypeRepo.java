package com.lasrosas.iot.core.database.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lasrosas.iot.core.database.entities.tsr.TimeSerieType;

public interface TimeSerieTypeRepo extends JpaRepository<TimeSerieType, Long> {
	TimeSerieType findBySchema(String schema);
}
