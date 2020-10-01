package com.lasrosas.iot.database.repo;

import org.springframework.stereotype.Repository;

import com.lasrosas.iot.database.entities.thg.Thing;

@Repository
public interface ThingRepo extends ThingRepoBase<Thing> {

}
