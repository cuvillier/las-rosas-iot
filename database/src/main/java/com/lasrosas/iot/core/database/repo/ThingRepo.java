package com.lasrosas.iot.core.database.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lasrosas.iot.core.database.entities.thg.Thing;

@Repository
public interface ThingRepo extends ThingRepoBase<Thing> {

	@Query("select thg from Thing thg where thg.connectionTimeout is not null and thg.proxy.lastSeen is not null")
	List<Thing> findTimeouted();
	List<Thing> findByGateway_Techid(long gatewayTechid);
}
