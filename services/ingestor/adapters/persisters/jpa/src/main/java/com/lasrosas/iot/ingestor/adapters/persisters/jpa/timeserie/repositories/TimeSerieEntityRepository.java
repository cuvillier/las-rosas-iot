package com.lasrosas.iot.ingestor.adapters.persisters.jpa.timeserie.repositories;

import com.lasrosas.iot.ingestor.adapters.persisters.jpa.thing.entities.ThingEntity;
import com.lasrosas.iot.ingestor.adapters.persisters.jpa.timeserie.entities.TimeSerieEntity;
import com.lasrosas.iot.ingestor.adapters.persisters.jpa.timeserie.entities.TimeSerieTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TimeSerieEntityRepository extends JpaRepository<TimeSerieEntity, Long> {
    Optional<TimeSerieEntity> getByThingAndTypeAndSensor(ThingEntity thing, TimeSerieTypeEntity timeSerieType, String sensor);
}
