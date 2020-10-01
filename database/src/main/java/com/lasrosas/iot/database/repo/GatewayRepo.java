package com.lasrosas.iot.database.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lasrosas.iot.database.entities.thg.ThingGateway;

@Repository
public interface GatewayRepo extends JpaRepository<ThingGateway,Long>, GatewayRepoCustom {

	ThingGateway findByNaturalId(String gatewayId);
}
