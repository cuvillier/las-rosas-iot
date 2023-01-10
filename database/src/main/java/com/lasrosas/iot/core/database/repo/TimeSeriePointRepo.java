package com.lasrosas.iot.core.database.repo;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lasrosas.iot.core.database.entities.tsr.TimeSerie;
import com.lasrosas.iot.core.database.entities.tsr.TimeSeriePoint;

public interface TimeSeriePointRepo extends JpaRepository<TimeSeriePoint, Long>{
	Optional<TimeSeriePoint> getByTimeAndTimeSerie(LocalDateTime time, TimeSerie timeSerie);

}
