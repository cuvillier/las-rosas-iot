package com.lasrosas.iot.core.database.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lasrosas.iot.core.database.entities.thg.ThingType;

@Repository
public interface ThingTypeRepo extends JpaRepository<ThingType, Long>{
	ThingType getByManufacturerAndModel(String manufacturer, String model);

}
