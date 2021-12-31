package com.lasrosas.iot.core.reactor.base;

import org.springframework.messaging.Message;

import com.lasrosas.iot.core.database.entities.dtw.TwinReactorReceiver;
import com.lasrosas.iot.core.shared.telemetry.Telemetry;

public interface TwinReactor {
	void react(TwinReactorReceiver receiver, Message<? extends Telemetry> message);
}
