package com.lasrosas.iot.services.db.repo;

import org.springframework.stereotype.Repository;

import com.lasrosas.iot.services.db.entities.thg.Thing;

@Repository
public interface ThingRepo extends ThingRepoBase<Thing> {

}
