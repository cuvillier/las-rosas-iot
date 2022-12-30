package com.lasrosas.iot.core.database.repo;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.lasrosas.iot.core.database.entities.thg.ThingLora;

@Repository
public interface ThingLoraRepo extends ThingRepoBase<ThingLora>, ThingLoraRepoCustom {
	Optional<ThingLora> getByDeveui(String deveui);
}
