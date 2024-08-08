package com.lasrosas.iot.ingestor.adapters.persisters.timeserie.repositories;

import com.lasrosas.iot.ingestor.adapters.persisters.timeserie.entities.TimeSerieEntity;
import com.lasrosas.iot.ingestor.adapters.persisters.timeserie.entities.TimeSeriePointEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TimeSeriePointEntityRepository extends JpaRepository<TimeSeriePointEntity, Long> {
    Optional<TimeSeriePointEntity> getByTimeAndTimeSerie(LocalDateTime time, TimeSerieEntity timeSerie);
}
