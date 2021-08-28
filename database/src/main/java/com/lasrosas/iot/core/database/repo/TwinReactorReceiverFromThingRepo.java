package com.lasrosas.iot.core.database.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lasrosas.iot.core.database.entities.dtw.TwinReactorReceiverFromThing;
import com.lasrosas.iot.core.database.entities.thg.Thing;

public interface TwinReactorReceiverFromThingRepo extends JpaRepository<TwinReactorReceiverFromThing, Long> {
	List<TwinReactorReceiverFromThing> findByThing(Thing thing);
}
