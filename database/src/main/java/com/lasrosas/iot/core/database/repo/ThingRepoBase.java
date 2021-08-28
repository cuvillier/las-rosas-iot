package com.lasrosas.iot.core.database.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lasrosas.iot.core.database.entities.thg.Thing;

public interface ThingRepoBase <THG extends Thing> extends JpaRepository<THG, Long> {

}
