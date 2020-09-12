package com.lasrosas.iot.services.db.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lasrosas.iot.services.db.entities.thg.Thing;

public interface ThingRepoBase <THG extends Thing> extends JpaRepository<THG, Long> {

}
