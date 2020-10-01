package com.lasrosas.iot.database.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lasrosas.iot.database.entities.tsr.TimeSeriePoint;

public interface TimeSeriePointRepo extends JpaRepository<TimeSeriePoint, Long>{

}
