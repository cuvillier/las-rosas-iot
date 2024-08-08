package com.lasrosas.iot.ingestor.adapters.persisters.timeserie.repositories;

import com.lasrosas.iot.ingestor.adapters.persisters.timeserie.entities.TimeSerieTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TimeSerieTypeEntityRepository extends JpaRepository<TimeSerieTypeEntity, Long> {
    Optional<TimeSerieTypeEntity> getBySchema(String schema);
}
