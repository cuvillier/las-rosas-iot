package com.lasrosas.iot.database.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lasrosas.iot.database.entities.tsr.TimeSerieType;

public interface TimeSerieTypeRepo extends JpaRepository<TimeSerieType, Long> {
	TimeSerieType findBySchema(String schema);
}
