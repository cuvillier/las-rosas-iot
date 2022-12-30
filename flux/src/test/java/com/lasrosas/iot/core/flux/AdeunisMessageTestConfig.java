package com.lasrosas.iot.core.flux;

import org.springframework.context.annotation.Bean;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.router.PayloadTypeRouter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;

import com.lasrosas.iot.core.ingestor.lora.api.LoraMessageSplitter;
import com.lasrosas.iot.core.ingestor.lora.api.LoraMetricMessage;
import com.lasrosas.iot.core.ingestor.lora.api.LoraService;
import com.lasrosas.iot.core.ingestor.lora.impl.LoraServiceImpl;
import com.lasrosas.iot.core.ingestor.parsers.api.ThingEncodedMessage;
import com.lasrosas.iot.core.shared.telemetry.StillAlive;

public class AdeunisMessageTestConfig {

	@Bean
	public DirectChannel inputChannel() {
		return new DirectChannel();
	}

	@Bean
	public PollableChannel loraMetricChannel() {
		return new QueueChannel();
	}

	@Bean
	public PollableChannel thingEncodedChannel() {
		return new QueueChannel();
	}

	@Bean
	public PollableChannel errorChannel() {
		return new QueueChannel();
	}

	@Bean
	public LoraService loraService() {
		return new LoraServiceImpl();
	}

	@Bean
	public IntegrationFlow loraServices(MessageChannel inputChannel, LoraService service) {

		var router = new PayloadTypeRouter();
		router.setChannelMapping(LoraMetricMessage.class.getName(), "loraMetricChannel");
		router.setChannelMapping(ThingEncodedMessage.class.getName(), "thingEncodedChannel");
		router.setChannelMapping(StillAlive.class.getName(), "errorChannel");

		var splitter = new LoraMessageSplitter(service);

		return IntegrationFlows.from(inputChannel)
			.split(splitter)
			.route(router).get();
	}
}
