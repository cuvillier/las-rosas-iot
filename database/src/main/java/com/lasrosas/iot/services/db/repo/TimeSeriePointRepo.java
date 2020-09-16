package com.lasrosas.iot.services.db.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lasrosas.iot.services.db.entities.tsr.TimeSeriePoint;

public interface TimeSeriePointRepo extends JpaRepository<TimeSeriePoint, Long>{

}
