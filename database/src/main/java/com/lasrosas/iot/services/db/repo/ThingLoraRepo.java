package com.lasrosas.iot.services.db.repo;

import org.springframework.stereotype.Repository;

import com.lasrosas.iot.services.db.entities.thg.ThingGateway;
import com.lasrosas.iot.services.db.entities.thg.ThingLora;

@Repository
public interface ThingLoraRepo extends ThingRepoBase<ThingLora>, ThingLoraRepoCustom {
	ThingLora getByDeveui(String deveui);
}
