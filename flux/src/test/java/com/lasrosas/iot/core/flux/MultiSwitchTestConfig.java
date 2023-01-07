package com.lasrosas.iot.core.flux;

import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.Router;
import org.springframework.integration.annotation.Splitter;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.config.AbstractSimpleMessageHandlerFactoryBean;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.router.PayloadTypeRouter;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.integration.splitter.AbstractMessageSplitter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;

import com.lasrosas.iot.core.reactor.api.ReactorService;
import com.lasrosas.iot.core.reactor.base.ReactorServiceImpl;
import com.lasrosas.iot.core.reactor.reactores.WaterTankReactor;
import com.lasrosas.iot.core.shared.telemetry.Telemetry;

public class MultiSwitchTestConfig {

	@MessagingGateway
	public static interface TelemetryGateway {

		@Gateway(requestChannel = LasRosasIotBaseConfig.telemetryChannelName)
		void sendTelemetry(Message<Telemetry> c);
	}

	@Bean(name = PollerMetadata.DEFAULT_POLLER)
	public PollerMetadata poller() {
		return Pollers.fixedRate(10 * 1000).get();
	}

	@Bean
	public LasRosasFluxDelegate delegate() {
		return new LasRosasFluxDelegate();
	}

	@Bean
	public PublishSubscribeChannel telemetryChannel() {
		return MessageChannels.publishSubscribe().get();
	}

	@Bean
	public PollableChannel errorChannel() {
		return MessageChannels.queue().get();
	}

	@Bean
	public PollableChannel stateChannel() {
		return MessageChannels.queue().get();
	}

	@Bean
	public MessageChannel twinOutputChannel() {
		return MessageChannels.direct().get();
	}

	@Bean
	public PollableChannel orderChannel() {
		return MessageChannels.queue().get();
	}

	@Bean
	public ReactorService ReactorService() {
		return new ReactorServiceImpl();
	}

	@Bean
	public WaterTankReactor WaterTankReactor() {
		return new WaterTankReactor();
	}

	@Bean
	@Splitter(inputChannel = LasRosasIotBaseConfig.telemetryChannelName)
	public AbstractSimpleMessageHandlerFactoryBean<AbstractMessageSplitter> reactOnMessage(ReactorService service,
			MessageChannel twinOutputChannel) {
		return delegate().reactOnMessage(service, twinOutputChannel);
	}

	@Bean
	@Router(inputChannel = LasRosasIotBaseConfig.twinOutputChannelName)
	public PayloadTypeRouter twinOutputRouter() {
		return delegate().twinOutputRouter();
	}
}
