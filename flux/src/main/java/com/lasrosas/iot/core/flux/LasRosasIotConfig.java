package com.lasrosas.iot.core.flux;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.integration.annotation.Router;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Splitter;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.config.AbstractSimpleMessageHandlerFactoryBean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.router.HeaderValueRouter;
import org.springframework.integration.router.PayloadTypeRouter;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.integration.splitter.AbstractMessageSplitter;
import org.springframework.integration.transformer.AbstractTransformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.validation.annotation.Validated;

import com.lasrosas.iot.core.flux.LasRosasFluxDelegate.LasRosasGateway;
import com.lasrosas.iot.core.ingestor.connectionState.api.ConnectionStateService;
import com.lasrosas.iot.core.ingestor.connectionState.impl.ConnectionStateServiceImpl;
import com.lasrosas.iot.core.ingestor.gateway.api.GatewayService;
import com.lasrosas.iot.core.ingestor.gateway.impl.rak7249.api.Rak7249Driver;
import com.lasrosas.iot.core.ingestor.lora.api.LoraService;
import com.lasrosas.iot.core.ingestor.parsers.api.SensorService;
import com.lasrosas.iot.core.ingestor.timeSerieWriter.api.WriteInfluxDB;
import com.lasrosas.iot.core.ingestor.timeSerieWriter.api.WriteSQL;
import com.lasrosas.iot.core.reactor.api.ReactorService;

@ConfigurationProperties
@Validated
@EnableScheduling
public class LasRosasIotConfig {
	public static final Logger log = LoggerFactory.getLogger(LasRosasIotConfig.class);
	public static final Logger messagesLog = LoggerFactory.getLogger("MessagesLog");

	@Autowired
	private LasRosasFluxDelegate delegate;

	@Bean(name = PollerMetadata.DEFAULT_POLLER)
	public PollerMetadata poller() {
		return Pollers.fixedRate(10 * 1000).get();
	}

	/*
	 * Services & Config
	 */

	@Bean
	@ConfigurationProperties(prefix = "rak7249.connect")
	public MqttConnectOptions rak7249MqttConnectOptions() {
		return delegate.rak7249MqttConnectOptions();
	}

	@Bean
	@ConfigurationProperties(prefix = "rak7249.mqtt")
	public MqttConfig rak7249MqttConfig(MqttConnectOptions rak7249MqttConnectOptions) {
		return delegate.rak7249MqttConfig(rak7249MqttConnectOptions);
	}

	// To connect to the RAK7249 mqtt broker
	@Bean
	private MqttPahoClientFactory rak7249MqttClientFactory(MqttConfig rak7249MqttConfig) {
		return delegate.rak7249MqttClientFactory(rak7249MqttConfig);
	}

	@Bean
	@ConfigurationProperties(prefix = "publish.connect")
	public MqttConnectOptions publishMqttConnectOptopns() {
		return delegate.publishMqttConnectOptopns();
	}

	@Bean
	@ConfigurationProperties(prefix = "publish.mqtt")
	public MqttConfig publishMqttConfig(MqttConnectOptions publishMqttConnectOptopns) {
		return delegate.publishMqttConfig(publishMqttConnectOptopns);
	}

	@Bean
	public MqttPahoClientFactory publishMqttClientFactory(MqttConfig publishMqttConfig) {
		return delegate.publishMqttClientFactory(publishMqttConfig);
	}

	/*
	 * Processing nodes
	 */
	@Bean
	public IntegrationFlow rak7249Input(MqttConfig rak7249MqttConfig, MqttPahoClientFactory rak7249MqttClientFactory,
			MessageChannel rak7249UplinkChannel, Rak7249Driver rak7249Driver, LasRosasGateway gateway) {

		return delegate.rak7249Input(rak7249MqttConfig, rak7249MqttClientFactory, rak7249UplinkChannel, rak7249Driver, gateway);
	}

	@Bean
	public IntegrationFlow rak7249ToLoraMessage(Rak7249Driver service, MessageChannel rak7249UplinkTxChannel,
			MessageChannel loraChannel) {
		return delegate.rak7249ToLoraMessage(service, rak7249UplinkTxChannel, loraChannel);
	}

	@Bean
	public IntegrationFlow handleLoraMessages(MessageChannel loraChannel, LoraService service, ConnectionStateService ctxStateService) {
		return delegate.handleLoraMessages(loraChannel, service, ctxStateService);
	}

	@Bean
	public IntegrationFlow handleLoraMetric(MessageChannel loraMetricChannel, WriteInfluxDB influxDB, WriteSQL sql) {
		return delegate.handleLoraMetric(loraMetricChannel, influxDB, sql);
	}

	@Bean
	public IntegrationFlow handleThingMessage(MessageChannel thingEncodedDataChannel, MessageChannel telemetryChannel,
			SensorService service, WriteInfluxDB influxDB, WriteSQL sql) {

		return delegate.handleThingMessage(thingEncodedDataChannel, telemetryChannel, service, influxDB, sql);
	}

	@Bean
	@ServiceActivator(inputChannel = LasRosasIotBaseConfig.telemetryChannelName)
	public MessageHandler writeTelemetry(WriteInfluxDB influxDB, WriteSQL sql) {
		return delegate.writeTelemetry(influxDB, sql);
	}

	@Bean
	@Splitter(inputChannel = LasRosasIotBaseConfig.telemetryChannelName)
	@DependsOn({ "writeTelemetry" })
	public AbstractSimpleMessageHandlerFactoryBean<AbstractMessageSplitter> reactOnMessage(ReactorService service,
			MessageChannel twinOutputChannel) {
		return delegate.reactOnMessage(service, twinOutputChannel);
	}

	@Bean
	@Router(inputChannel = LasRosasIotBaseConfig.twinOutputChannelName)
	public PayloadTypeRouter twinOutputRouter() {
		return delegate.twinOutputRouter();

	}

	@Bean
	@Transformer(inputChannel = LasRosasIotBaseConfig.orderChannelName, outputChannel = LasRosasIotBaseConfig.downlinkChannelName)
	public AbstractTransformer encodeOrder(SensorService sensorService, GatewayService gatewayService) {
		return delegate.encodeOrder(sensorService, gatewayService);
	}

	@Bean
	@Router(inputChannel = LasRosasIotBaseConfig.downlinkChannelName)
	public HeaderValueRouter gatewayDownlinkRouter() {
		return delegate.gatewayDownlinkRouter();
	}

	@Bean
	@ServiceActivator(inputChannel = LasRosasIotBaseConfig.rak7249ChannelName)
	public MessageHandler rak7249DownlinkPublisher(MqttPahoClientFactory rak7249MqttClientFactory) {
		return delegate.rak7249DownlinkPublisher(rak7249MqttClientFactory);	
	}

	@Bean
	@Transformer(inputChannel = LasRosasIotBaseConfig.telemetryChannelName, outputChannel = LasRosasIotBaseConfig.publishMqttChannelName)
	public ToGsonTransformer thingDataToJson() {
		return new ToGsonTransformer();
	}

	@Bean
	@ServiceActivator(inputChannel = LasRosasIotBaseConfig.publishMqttChannelName)
	public MessageHandler mqttPublisher(MqttPahoClientFactory publishMqttClientFactory) {
		MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("lasRosasIot", publishMqttClientFactory) {

			@Override
			protected void publish(String topic, Object mqttMessage, Message<?> message) {

				log.info("Publish mqtt message topic=" + topic);
				log.info(message.getPayload().toString());

				super.publish(topic, mqttMessage, message);
			}
		};

		messageHandler.setAsync(false);
		// topic header is set in jsontransformer
		messageHandler.setTopicExpressionString("headers['topic']");
		return messageHandler;
	}

	/*
	 * Scheduler
	 */
	@Bean
	public ThreadPoolTaskScheduler taskScheduler() {

		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
		threadPoolTaskScheduler.setPoolSize(3);
		threadPoolTaskScheduler.setThreadNamePrefix("LasRosasThread");
		return threadPoolTaskScheduler;
	}

	@Bean
	private ScheduledTaks scheduledTaks() {

		// Create the bean to start the scheduler
		return new ScheduledTaks();
	}
	@Bean
	public ConnectionStateService stateService() {
		return new ConnectionStateServiceImpl();
	}
}
