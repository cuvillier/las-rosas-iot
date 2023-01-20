package com.lasrosas.iot.core.database.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lasrosas.iot.core.database.entities.thg.Thing;

@Repository
public interface ThingRepo extends ThingRepoBase<Thing> {

	List<Thing> findByGateway_Techid(long gatewayTechid);
}
