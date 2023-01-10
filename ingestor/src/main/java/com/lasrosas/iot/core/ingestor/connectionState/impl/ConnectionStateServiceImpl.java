package com.lasrosas.iot.core.ingestor.connectionState.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.lasrosas.iot.core.database.entities.thg.Thing;
import com.lasrosas.iot.core.database.repo.ThingRepo;
import com.lasrosas.iot.core.ingestor.connectionState.api.ConnectionStateService;

public class ConnectionStateServiceImpl implements ConnectionStateService {
	public static final Logger log = LoggerFactory.getLogger(ConnectionStateServiceImpl.class);

	@Autowired
	private ThingRepo thingRepo;

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public List<Thing> timeoutThing() {
		var things = thingRepo.findTimeouted();
		var result = new ArrayList<Thing>();

		for(var thing: things) {
			if( thing.needToDisconnect() ) {

				thing.getCreateProxy(em).setConnected(0);
				em.persist(thing);
				result.add(thing);

				log.info("Thing " + thing.getNaturalId() + " is marked as disconneted");
			}
		}
		
		return result;
	}

	@Override
	public boolean alive(LocalDateTime time, Thing thing) {
		var proxy = thing.getCreateProxy(em);

		if(time == null) time = LocalDateTime.now();
		proxy.setLastSeen(time);

		if(proxy.getConnected() == 0) {
			proxy.setConnected(1);
			return true;
		}
		
		return false;
	}
}
