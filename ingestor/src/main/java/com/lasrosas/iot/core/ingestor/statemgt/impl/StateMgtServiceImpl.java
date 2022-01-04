package com.lasrosas.iot.core.ingestor.statemgt.impl;

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

	public static interface StateNotifictionCallback {
		  void notifiyState(Message<?> cs);
	}

	@Scheduled(cron = "0 0/1 * * * *")
	public void timeoutThingtask() {
		log.info("=== Starting batch timeoutThings");
		timeoutThingTask.timeoutThing();
		log.info("=== End of batch timeoutThings");
	}
/*
	/**
	 * Use a method to be transactional
	 * @param thing
	 * /
	@Transactional(propagation = Propagation.REQUIRED)
	public void timeoutThings() {
		var things = thingRepo.findTimeouted();
		for(var thing: things) {
			if( thing.needToDisconnect() ) {
				thing.getProxy().setConnected(false);

				var notification = new ConnectionState(false, ConnectionState.CAUSE_NTW_TIMEOUT);

				var imessage = MessageBuilder.withPayload(notification)
					.setHeader(LasRosasHeaders.THING_ID, thing.getTechid())
					.setHeader(LasRosasHeaders.THING_NATURAL_ID, thing.getNaturalId())
					.setHeader(LasRosasHeaders.TIME_RECEIVED, LocalDateTime.now())
					.build();

				stateNotifictionCallback.notifiyState(imessage);
			}
		}
	}
*/
	@Override
	public ConnectionState handleStateMessage(Message<StateMessage> message) {
		var thingId = LasRosasHeaders.thingid(message).get();
		var thing = thingRepo.getOne(thingId);

		var proxy = thing.getProxy();
		proxy.setLastSeen(LasRosasHeaders.timeReceived(message));
		ConnectionState notification = null;

		var payload = message.getPayload();

		if( payload instanceof ConnectionState) {
			var connectionState = (ConnectionState)payload;

			if(connectionState.getConnected() != proxy.getConnected()) {
				proxy.setConnected(connectionState.getConnected());
			}
			notification = connectionState;

			log.info("Thing " + thing.getNaturalId() + " is now " + (proxy.isConnected()? "connected": "disconnected"));

		} else if( payload instanceof StillAlive) {
			if(!proxy.isConnected()) {
				proxy.setConnected(1);
			}
			notification = new ConnectionState(1, ConnectionState.CAUSE_ALIVE);
		} else
			throw new RuntimeException("Cannot handle this payload type: " + payload.getClass().getName());

		return notification;
	}
}
