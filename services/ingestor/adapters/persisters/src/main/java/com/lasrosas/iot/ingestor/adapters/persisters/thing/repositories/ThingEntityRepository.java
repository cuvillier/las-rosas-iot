package com.lasrosas.iot.ingestor.adapters.persisters.thing.repositories;

import com.lasrosas.iot.ingestor.adapters.persisters.thing.entities.ThingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ThingEntityRepository extends JpaRepository<ThingEntity, Long> {
	Optional<ThingEntity> findByNaturalid(String naturalId);
}
