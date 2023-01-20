package com.lasrosas.iot.core.flux;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;

import com.lasrosas.iot.core.flux.LasRosasFluxDelegate.LasRosasGateway;
import com.lasrosas.iot.core.ingestor.connectionState.api.ConnectionStateService;
import com.lasrosas.iot.core.shared.telemetry.ConnectionState;
import com.lasrosas.iot.core.shared.utils.LasRosasHeaders;

public class ScheduledTaks {
	public static final Logger log = LoggerFactory.getLogger(LasRosasIotConfig.class);

	@Autowired
	private ConnectionStateService stateService;

	@Autowired
	private LasRosasGateway gateway;

	@Scheduled(cron = "0 0/1 * * * *")
	public void timeoutThingtask() {
		log.info("=== Starting batch timeoutThings");

		// Need to call stateService in order to start a new transaction.
		// timeoutThingtask() cannot be moved in ConnectionStateService
		var disconnectedThings = stateService.timeoutThing();

		// Notify the connection state change
		for(var thing: disconnectedThings) {
			var message = new ConnectionState(ConnectionState.DISCONNECTED);
			var imessage = MessageBuilder.withPayload(message)
					.setHeader(LasRosasHeaders.TIME_RECEIVED, LocalDateTime.now())
					.setHeader(LasRosasHeaders.THING_ID, thing.getTechid())
					.setHeader(LasRosasHeaders.THING_NATURAL_ID, thing.getNaturalId())
					.build();

			gateway.sendTelemetry(imessage);
		}

		log.info("=== End of batch timeoutThings");
	}
}
