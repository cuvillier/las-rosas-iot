package com.lasrosas.iot.core.database.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lasrosas.iot.core.database.entities.tsr.TimeSerieType;

public interface TimeSerieTypeRepo extends JpaRepository<TimeSerieType, Long> {
	Optional<TimeSerieType> findBySchema(String schema);
}
