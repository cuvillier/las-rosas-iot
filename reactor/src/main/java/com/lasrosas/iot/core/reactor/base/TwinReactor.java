package com.lasrosas.iot.core.reactor.base;

import java.util.List;

import org.springframework.messaging.Message;

import com.lasrosas.iot.core.database.entities.dtw.DigitalTwin;
import com.lasrosas.iot.core.database.entities.dtw.TwinReactorReceiver;
import com.lasrosas.iot.core.shared.telemetry.Telemetry;

public abstract class TwinReactor {
	public abstract List<?extends Telemetry> react(DigitalTwin twin, TwinReactorReceiver receiver, Message<? extends Telemetry> message);
}
