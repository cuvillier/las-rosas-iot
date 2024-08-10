package com.lasrosas.iot.ingestor.adapters.persisters.jpa.thing.repositories;

import java.util.Optional;

import com.lasrosas.iot.ingestor.adapters.persisters.jpa.thing.entities.ThingGatewayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThingGatewayEntityRepository extends JpaRepository<ThingGatewayEntity,Long> {
	Optional<ThingGatewayEntity> findByNaturalid(String gatewayid);
}
