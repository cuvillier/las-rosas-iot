package com.lasrosas.iot.core.database.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lasrosas.iot.core.database.entities.tsr.TimeSeriePoint;

public interface TimeSeriePointRepo extends JpaRepository<TimeSeriePoint, Long>{

}
