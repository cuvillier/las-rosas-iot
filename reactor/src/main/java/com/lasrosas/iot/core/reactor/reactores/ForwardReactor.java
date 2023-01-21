package com.lasrosas.iot.core.reactor.reactores;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;

import com.lasrosas.iot.core.database.entities.dtw.TwinReactorReceiver;
import com.lasrosas.iot.core.reactor.base.ReactContext;
import com.lasrosas.iot.core.reactor.base.TwinReactor;
import com.lasrosas.iot.core.shared.telemetry.Telemetry;

public class ForwardReactor implements TwinReactor {
	public static final Logger log = LoggerFactory.getLogger(ForwardReactor.class);

	@Override
	public void react(TwinReactorReceiver receiver, Message<?> message) {
		var payload = message.getPayload();
		if(payload instanceof Telemetry) ReactContext.addTelemetry((Telemetry)payload);
	}
}
