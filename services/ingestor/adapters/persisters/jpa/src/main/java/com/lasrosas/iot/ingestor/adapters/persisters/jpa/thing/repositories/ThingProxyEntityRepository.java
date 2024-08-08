package com.lasrosas.iot.ingestor.adapters.persisters.jpa.thing.repositories;

import com.lasrosas.iot.ingestor.adapters.persisters.jpa.thing.entities.ThingProxyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThingProxyEntityRepository extends JpaRepository<ThingProxyEntity,Long> {
}
