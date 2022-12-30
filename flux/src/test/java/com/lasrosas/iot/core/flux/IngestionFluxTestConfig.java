package com.lasrosas.iot.core.flux;

import org.springframework.context.annotation.Bean;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;

import com.lasrosas.iot.core.ingestor.lora.api.LoraService;
import com.lasrosas.iot.core.ingestor.lora.impl.LoraServiceImpl;
import com.lasrosas.iot.core.ingestor.parsers.api.SensorService;

public class IngestionFluxTestConfig {

	@Bean(name = PollerMetadata.DEFAULT_POLLER)
	public PollerMetadata poller() {
		return Pollers.fixedRate(10 * 1000).get();
	}

	@Bean
	public LasRosasFluxDelegate delegate() {
		return new LasRosasFluxDelegate();
	}

	@Bean
	public DirectChannel loraChannel() {
		return new DirectChannel();
	}

	@Bean
	public PollableChannel stateChannel() {
		return new QueueChannel();
	}

	@Bean
	public PollableChannel telemetryChannel() {
		return new QueueChannel();
	}

	@Bean
	public PollableChannel errorChannel() {
		return new QueueChannel();
	}

	@Bean
	public PollableChannel loraMetricChannel() {
		return new QueueChannel();
	}

	@Bean
	public PollableChannel thingEncodedDataChannel() {
		return new QueueChannel();
	}

	@Bean
	public LoraService loraService() {
		return new LoraServiceImpl();
	}

	@Bean
	public IntegrationFlow handleLoraMessages(MessageChannel loraChannel, LoraService service) {
		return delegate().handleLoraMessages(loraChannel, service);
	}

	@Bean
	public IntegrationFlow handleThingMessage(MessageChannel thingEncodedDataChannel, MessageChannel telemetryChannel,
			SensorService service) {
		return delegate().handleThingMessage(thingEncodedDataChannel, telemetryChannel, service, null, null);
	}
}
