package com.lasrosas.iot.core.database.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lasrosas.iot.core.database.entities.thg.ThingGateway;

@Repository
public interface GatewayRepo extends JpaRepository<ThingGateway,Long>, GatewayRepoCustom {

	ThingGateway findByNaturalId(String gatewayId);
}
