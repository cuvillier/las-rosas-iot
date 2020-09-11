package com.lasrosas.iot.services.db.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lasrosas.iot.services.db.entities.thg.Thing;
import com.lasrosas.iot.services.db.entities.thg.ThingGateway;

public interface ThingRepo extends JpaRepository<Thing, Long>, ThingRepoCustom {
	Thing getByGatewayAndDeveui(ThingGateway gateway, String deveui);
}
