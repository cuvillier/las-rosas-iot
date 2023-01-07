package com.lasrosas.iot.core.ingestor.statemgt.api;

import java.time.LocalDateTime;
import java.util.List;

import com.lasrosas.iot.core.database.entities.thg.Thing;

public interface ConnectionStateService {
	
	// This thing is alive
	boolean alive(LocalDateTime time, Thing thing);

	// Check for disconnected things
	List<Thing> timeoutThing();
}
