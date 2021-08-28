package com.lasrosas.iot.core.database.repo;

import org.springframework.stereotype.Repository;

import com.lasrosas.iot.core.database.entities.thg.Thing;

@Repository
public interface ThingRepo extends ThingRepoBase<Thing> {

}
