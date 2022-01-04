package com.lasrosas.iot.core.reactor.base;

import org.springframework.messaging.Message;

import com.lasrosas.iot.core.database.entities.dtw.TwinReactorReceiver;

public interface TwinReactor {
	void react(TwinReactorReceiver receiver, Message<?> message);
}
