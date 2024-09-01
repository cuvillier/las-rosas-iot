package com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.repositories;

import com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.entities.DigitalTwinEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DigitalTwinEntityRepository extends JpaRepository<DigitalTwinEntity,Long> {
	DigitalTwinEntity getByName(String name);
}
