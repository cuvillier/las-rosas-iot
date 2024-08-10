package com.lasrosas.iot.ingestor.adapters.persisters.jpa.thing.repositories;

import com.lasrosas.iot.ingestor.adapters.persisters.jpa.thing.entities.ThingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ThingEntityRepository extends JpaRepository<ThingEntity, Long> {
	Optional<ThingEntity> getByNaturalid(String naturalId);
}
