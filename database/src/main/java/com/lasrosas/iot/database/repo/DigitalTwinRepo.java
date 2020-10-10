package com.lasrosas.iot.database.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lasrosas.iot.database.entities.dtw.DigitalTwin;

public interface DigitalTwinRepo extends JpaRepository<DigitalTwin,Long> {

}
