package com.lasrosas.iot.services.db.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lasrosas.iot.services.db.entities.tsr.TimeSerieType;

public interface TimeSerieTypeRepo extends JpaRepository<TimeSerieType, Long> {
	TimeSerieType findBySchema(String schema);
}
