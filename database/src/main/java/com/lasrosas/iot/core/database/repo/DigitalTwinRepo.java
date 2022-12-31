package com.lasrosas.iot.core.database.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lasrosas.iot.core.database.entities.dtw.DigitalTwin;

public interface DigitalTwinRepo extends JpaRepository<DigitalTwin,Long> {
	DigitalTwin getByName(String name);
}
