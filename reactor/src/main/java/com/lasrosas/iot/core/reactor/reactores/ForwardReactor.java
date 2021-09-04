package com.lasrosas.iot.core.reactor.reactores;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;

import com.lasrosas.iot.core.database.entities.dtw.DigitalTwin;
import com.lasrosas.iot.core.database.entities.dtw.TwinReactorReceiver;
import com.lasrosas.iot.core.reactor.base.TwinReactor;
import com.lasrosas.iot.core.shared.telemetry.Telemetry;

public class ForwardReactor extends TwinReactor {
	public static final Logger log = LoggerFactory.getLogger(WaterTankReactor.class);

	@SuppressWarnings("unchecked")
	@Override
	public List<? extends Telemetry> react(DigitalTwin twin, TwinReactorReceiver receiver,
			Message<? extends Telemetry> message) {
		return (List<? extends Telemetry>) Arrays.asList(message);
	}
}
