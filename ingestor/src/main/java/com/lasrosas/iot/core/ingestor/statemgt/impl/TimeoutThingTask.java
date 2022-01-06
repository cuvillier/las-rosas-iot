package com.lasrosas.iot.core.ingestor.statemgt.impl;

import java.time.LocalDateTime;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;

import com.lasrosas.iot.core.database.repo.ThingRepo;
import com.lasrosas.iot.core.ingestor.statemgt.impl.StateMgtServiceImpl.StateNotifictionCallback;
import com.lasrosas.iot.core.shared.telemetry.ConnectionState;
import com.lasrosas.iot.core.shared.utils.LasRosasHeaders;

public class TimeoutThingTask {

	@Autowired
	private ThingRepo thingRepo;

	@PersistenceContext
	private EntityManager em;
	
	private StateNotifictionCallback stateNotifictionCallback;

	public TimeoutThingTask(StateNotifictionCallback stateNotifictionCallback) {
		this.stateNotifictionCallback = stateNotifictionCallback;
	}

	public void setStateNotifictionCallback(StateNotifictionCallback stateNotifictionCallback) {
		this.stateNotifictionCallback = stateNotifictionCallback;
	}

	@Transactional
	public void timeoutThing() {
		var things = thingRepo.findTimeouted();
		for(var thing: things) {
			if( thing.needToDisconnect() ) {
				var notification = new ConnectionState(0, ConnectionState.CAUSE_NTW_TIMEOUT);
				thing.getCreateProxy(em).setConnected(0);

				var imessage = MessageBuilder.withPayload(notification)
					.setHeader(LasRosasHeaders.THING_ID, thing.getTechid())
					.setHeader(LasRosasHeaders.THING_NATURAL_ID, thing.getNaturalId())
					.setHeader(LasRosasHeaders.TIME_RECEIVED, LocalDateTime.now())
					.build();

				stateNotifictionCallback.notifiyState(imessage);
			}
		}
	}
}
