package com.lasrosas.iot.ingestor.adapters.persisters.timeserie.repositories;

import com.lasrosas.iot.ingestor.adapters.persisters.timeserie.entities.TimeSerieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeSerieEntityRepository extends JpaRepository<TimeSerieEntity, Long> {
}
