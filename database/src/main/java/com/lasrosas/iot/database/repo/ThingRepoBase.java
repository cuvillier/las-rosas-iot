package com.lasrosas.iot.database.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lasrosas.iot.database.entities.thg.Thing;

public interface ThingRepoBase <THG extends Thing> extends JpaRepository<THG, Long> {

}
