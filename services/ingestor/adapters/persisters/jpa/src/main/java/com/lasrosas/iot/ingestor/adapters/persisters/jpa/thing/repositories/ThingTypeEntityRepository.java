package com.lasrosas.iot.ingestor.adapters.persisters.jpa.thing.repositories;

import com.lasrosas.iot.ingestor.adapters.persisters.jpa.thing.entities.ThingTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ThingTypeEntityRepository extends JpaRepository<ThingTypeEntity, Long>{
	Optional<ThingTypeEntity> findByManufacturerAndModel(String manufacturer, String model);
}
