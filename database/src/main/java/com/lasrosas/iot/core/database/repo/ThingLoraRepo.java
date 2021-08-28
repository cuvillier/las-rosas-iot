package com.lasrosas.iot.core.database.repo;

import org.springframework.stereotype.Repository;

import com.lasrosas.iot.core.database.entities.thg.ThingLora;

@Repository
public interface ThingLoraRepo extends ThingRepoBase<ThingLora>, ThingLoraRepoCustom {
	ThingLora getByDeveui(String deveui);
}
