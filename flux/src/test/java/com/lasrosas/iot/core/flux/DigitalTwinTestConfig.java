package com.lasrosas.iot.core.flux;

import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.router.PayloadTypeRouter;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.integration.transformer.AbstractTransformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;
import org.springframework.transaction.annotation.Transactional;

import com.lasrosas.iot.core.ingestor.gateway.api.GatewayService;
import com.lasrosas.iot.core.ingestor.gateway.impl.GatewayServiceImpl;
import com.lasrosas.iot.core.ingestor.gateway.impl.rak7249.api.Rak7249Driver;
import com.lasrosas.iot.core.ingestor.gateway.impl.rak7249.impl.Rak7249DriverImpl;
import com.lasrosas.iot.core.ingestor.parsers.api.SensorService;
import com.lasrosas.iot.core.reactor.api.ReactorService;
import com.lasrosas.iot.core.reactor.api.ReactorSpliter;
import com.lasrosas.iot.core.reactor.base.ReactorServiceImpl;
import com.lasrosas.iot.core.reactor.reactores.MultiSwitchReactor;
import com.lasrosas.iot.core.reactor.reactores.WaterTankReactor;
import com.lasrosas.iot.core.shared.telemetry.Order;
import com.lasrosas.iot.core.shared.telemetry.Telemetry;

public class DigitalTwinTestConfig {

	@Bean
	public Rak7249Driver rak7249Driver() {
		return new Rak7249DriverImpl();
	}

	@Bean
	public GatewayService gatewayService(Rak7249Driver rak7249) {
		return new GatewayServiceImpl(rak7249);
	}

	@MessagingGateway
	public static interface TelemetryGateway {

		@Gateway(requestChannel = LasRosasIotBaseConfig.telemetryChannelName)
		@Transactional
		void sendTelemetry(Message<?> c);
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
	public PollableChannel downlinkChannel() {
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
	public MultiSwitchReactor MultiSwitchReactor() {
		return new MultiSwitchReactor();
	}

	@Bean
	public IntegrationFlow react(MessageChannel telemetryChannel, ReactorService service) {
		var router = new PayloadTypeRouter();

		router.setChannelMapping(Order.class.getName(), LasRosasIotBaseConfig.orderChannelName);
		router.setChannelMapping(Telemetry.class.getName(), LasRosasIotBaseConfig.telemetryChannelName);
		router.setDefaultOutputChannelName(LasRosasIotBaseConfig.errorChannelName);

		return IntegrationFlows
			.from(telemetryChannel)
			.split(new ReactorSpliter(service))
			.route(router)
			.get();
	}

	@Bean
	@Transformer(inputChannel = LasRosasIotBaseConfig.orderChannelName, outputChannel = LasRosasIotBaseConfig.downlinkChannelName)
	public AbstractTransformer encodeOrder(SensorService sensorService, GatewayService gatewayService) {
		return delegate().encodeOrder(sensorService, gatewayService);
	}

/*
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
*/
}
