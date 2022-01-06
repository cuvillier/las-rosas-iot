package com.lasrosas.iot.core.ingestor.statemgt.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.scheduling.annotation.Scheduled;

import com.lasrosas.iot.core.database.repo.ThingRepo;
import com.lasrosas.iot.core.ingestor.statemgt.api.StateMgtService;
import com.lasrosas.iot.core.shared.telemetry.ConnectionState;
import com.lasrosas.iot.core.shared.telemetry.StateMessage;
import com.lasrosas.iot.core.shared.telemetry.StillAlive;
import com.lasrosas.iot.core.shared.utils.LasRosasHeaders;

public class StateMgtServiceImpl implements StateMgtService {
	public static final Logger log = LoggerFactory.getLogger(StateMgtServiceImpl.class);

	@Autowired
	private ThingRepo thingRepo;

	@Autowired 
	private TimeoutThingTask timeoutThingTask;

	@PersistenceContext
	private EntityManager em;

	public static interface StateNotifictionCallback {
		  void notifiyState(Message<?> cs);
	}

	@Scheduled(cron = "0 0/5 * * * *")
	public void timeoutThingtask() {
		log.info("=== Starting batch timeoutThings");
		timeoutThingTask.timeoutThing();
		log.info("=== End of batch timeoutThings");
	}

	@Override
	public ConnectionState handleStateMessage(Message<StateMessage> message) {
		var thingId = LasRosasHeaders.thingid(message).get();
		var thing = thingRepo.getOne(thingId);

		var proxy = thing.getCreateProxy(em);
		proxy.setLastSeen(LasRosasHeaders.timeReceived(message));
		ConnectionState notification = null;

		var payload = message.getPayload();

		if( payload instanceof ConnectionState) {
			var connectionState = (ConnectionState)payload;

			int remind;

			/*
			 * If the sensor join, it was disconnected, but proxy.connected may be true
			 * because it is updated asynchronously by the TimeoutThingTask.
			 * 
			 * In any case, force the remind to 0, to handle it like a new connection.
			 */
			if( connectionState.getCause() == ConnectionState.CAUSE_NTW_JOIN )
				remind = 0;
			else
				remind = connectionState.getConnected() == proxy.getConnected()?1:0;
			notification = new ConnectionState(connectionState.getConnected(), connectionState.getCause(), remind);

			proxy.setConnected(connectionState.getConnected());

			log.info("Thing " + thing.getNaturalId() + " is now " + (proxy.isConnected()? "connected": "disconnected"));

		} else if( payload instanceof StillAlive) {
			var remind = proxy.isConnected()?1:0;
			notification = new ConnectionState(1, ConnectionState.CAUSE_ALIVE, remind);
			proxy.setConnected(1);
		} else
			throw new RuntimeException("Cannot handle this payload type: " + payload.getClass().getName());

		return notification;
	}
}
