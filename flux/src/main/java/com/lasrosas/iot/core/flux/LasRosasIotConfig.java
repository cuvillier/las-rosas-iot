package com.lasrosas.iot.core.flux;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.Router;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Splitter;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.AbstractSimpleMessageHandlerFactoryBean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.handler.AbstractMessageHandler;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.integration.router.HeaderValueRouter;
import org.springframework.integration.router.PayloadTypeRouter;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.integration.splitter.AbstractMessageSplitter;
import org.springframework.integration.transformer.AbstractTransformer;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lasrosas.iot.core.ingestor.gateway.api.GatewayService;
import com.lasrosas.iot.core.ingestor.gateway.impl.GatewayServiceImpl;
import com.lasrosas.iot.core.ingestor.gateway.impl.rak7249.api.Rak7249Driver;
import com.lasrosas.iot.core.ingestor.gateway.impl.rak7249.api.Rak7249Message;
import com.lasrosas.iot.core.ingestor.gateway.impl.rak7249.impl.Rak7249DriverImpl;
import com.lasrosas.iot.core.ingestor.lora.api.LoraMessage;
import com.lasrosas.iot.core.ingestor.lora.api.LoraMetricMessage;
import com.lasrosas.iot.core.ingestor.lora.api.LoraService;
import com.lasrosas.iot.core.ingestor.lora.impl.LoraServiceImpl;
import com.lasrosas.iot.core.ingestor.sensors.api.DownlinkEncoderTransformer;
import com.lasrosas.iot.core.ingestor.sensors.api.SensorService;
import com.lasrosas.iot.core.ingestor.sensors.api.TelemetrySpliter;
import com.lasrosas.iot.core.ingestor.sensors.api.ThingDataMessage;
import com.lasrosas.iot.core.ingestor.sensors.api.ThingEncodedMessage;
import com.lasrosas.iot.core.ingestor.statemgt.api.StateMgtService;
import com.lasrosas.iot.core.ingestor.statemgt.impl.StateMgtServiceImpl;
import com.lasrosas.iot.core.ingestor.statemgt.impl.TimeoutThingTask;
import com.lasrosas.iot.core.ingestor.timeSerieWriter.api.InfluxDBConfig;
import com.lasrosas.iot.core.ingestor.timeSerieWriter.api.WriteInfluxDB;
import com.lasrosas.iot.core.ingestor.timeSerieWriter.api.WriteSQL;
import com.lasrosas.iot.core.ingestor.timeSerieWriter.impl.WriteInfluxDBImpl;
import com.lasrosas.iot.core.ingestor.timeSerieWriter.impl.WriteSQLImpl;
import com.lasrosas.iot.core.reactor.api.ReactorService;
import com.lasrosas.iot.core.reactor.api.ReactorSpliter;
import com.lasrosas.iot.core.reactor.base.ReactorServiceImpl;
import com.lasrosas.iot.core.shared.telemetry.ConnectionState;
import com.lasrosas.iot.core.shared.telemetry.StateMessage;
import com.lasrosas.iot.core.shared.telemetry.StillAlive;
import com.lasrosas.iot.core.shared.telemetry.Telemetry;
import com.lasrosas.iot.core.shared.utils.LasRosasHeaders;

@ConfigurationProperties
@Validated
@EnableScheduling
public class LasRosasIotConfig {
	public static final Logger log = LoggerFactory.getLogger(LasRosasIotConfig.class);
	public static final Logger messagesLog = LoggerFactory.getLogger("MessagesLog");

	public static final String rak7249UplinkChannelName = "rak7249UplinkChannel";
	public static final String rak7249UplinkTxChannelName = "rak7249UplinkTxChannel";
	public static final String rak7249DownlinkChannelName = "rak7249DownlinkChannel";
	public static final String loraChannelName = "loraChannel";
	public static final String errorChannelName = "errorChannel";
	public static final String stateChannelName = "stateChannel";
	public static final String mixedLoraChannelName = "mixedLoraChannel";
	public static final String loraMetricChannelName = "loraMetricChannel";
	public static final String thingEncodedDataChannelName = "thingEncodedDataChannel";
	public static final String thingDataChannelName = "thingDataChannel";
	public static final String thingBatteryChannelName = "thingBatteryChannel";
	public static final String telemetryChannelName = "telemetryChannel";
	public static final String alarmChannelName = "alarmChannel";
	public static final String publishMqttChannelName = "publishMqttChannel";
	public static final String twinOutputChannelName = "twinOutputChannel";
	public static final String orderChannelName = "downlinkChannel";
	public static final String downlinkChannelName = "downlinkChannel";

	private Gson gson = new GsonBuilder().setPrettyPrinting().create();

	@Bean(name = PollerMetadata.DEFAULT_POLLER)
	public PollerMetadata poller() {
		return Pollers.fixedRate(10*1000).get();
	}

	@Bean
	public TimeoutThingTask timeoutThingTask(LasRosasGateway gateway) {
		return new TimeoutThingTask((c) -> {
				log.info("Start Handling disconnection");
				gateway.sendTelemetry(c);
				log.info("Stop Handling disconnection");
			});

	}

	/*
	 * Channels
	 */
	@Bean
	public MessageChannel errorChannel() {
	    var queue = new QueueChannel(500);

	    queue.addInterceptor(new ChannelInterceptor() {
	    	@Override
			public Message<?> preSend(Message<?> message, MessageChannel channel) {
				log.error("");
				log.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				log.error(message.getPayload().getClass().getName());
				log.error(gson.toJson(message.getPayload()));
	    		return message;
	    	}
	    });

	    return queue;
	}

	@Bean
	public MessageChannel rak7249UplinkChannel() {
		return MessageChannels.direct().get();
	}

	@Bean
	public MessageChannel rak7249UplinkTxChannel() {
		return MessageChannels.publishSubscribe().get();
	}

	@Bean
	public MessageChannel loraChannel() {
		return MessageChannels.publishSubscribe().get();
	}

	@Bean
	public MessageChannel loraJoinChannel() {
		return MessageChannels.direct().get();
	}

	@Bean
	public MessageChannel loraMetricChannel() {
		return MessageChannels.publishSubscribe().get();
	}

	@Bean
	public MessageChannel orderChannel() {
		return MessageChannels.direct().get();
	}

	@Bean
	public MessageChannel downlinkChannel() {
		return MessageChannels.direct().get();
	}

	@Bean
	public MessageChannel stateChannel() {
		return MessageChannels.direct().get();
	}

	@Bean
	public MessageChannel rak7249DownlinkChannel() {
		return MessageChannels.direct().get();
	}
	@Bean
	public MessageChannel twinOutputChannel() {
		return MessageChannels.direct().get();
	}

	@Bean
	public MessageChannel sensorDataChannel() {
		return MessageChannels.publishSubscribe().get();
	}


	@Bean
	public MessageChannel mixedLoraChannel() {
		return MessageChannels.direct().get();
	}
	
	@Bean
	public MessageChannel thingEncodedDataChannel() {
		return MessageChannels.direct().get();
	}

	@Bean
	public MessageChannel thingDataChannel() {
		return MessageChannels.publishSubscribe().get();
	}

	@Bean
	public MessageChannel telemetryChannel() {
		return MessageChannels.publishSubscribe().get();
	}

	@Bean
	public MessageChannel connectionChannel() {
		return MessageChannels.publishSubscribe().get();
	}

	@Bean
	public MessageChannel publishMqttChannel() {
		return MessageChannels.direct().get();
	}

	/*
	 * Services & Config
	 */

	@Bean
	@ConfigurationProperties(prefix = "rak7249.connect")
	public MqttConnectOptions rak7249MqttConnectOptopns() {
		return new MqttConnectOptions();
	}

	@Bean
	@ConfigurationProperties(prefix = "rak7249.mqtt")
	public MqttConfig rak7249MqttConfig(MqttConnectOptions rak7249MqttConnectOptopns) {
		var config = new MqttConfig();
		config.setConnectOptions(rak7249MqttConnectOptopns);
		return config;
	}

	// To connect to the RAK7249 mqtt broker
	@Bean
	private MqttPahoClientFactory rak7249MqttClientFactory(MqttConfig rak7249MqttConfig) {
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		factory.setConnectionOptions(rak7249MqttConfig.getConnectOptions());
		return factory;
	}

	@Bean
	public Rak7249Driver rak7249Driver() {
		return new Rak7249DriverImpl();
	}

	@Bean
	public MqttRak7249Converter mqttRak7249Converter() {
		return new MqttRak7249Converter();
	}

	@Bean
	public LoraService loraService() {
		return new LoraServiceImpl();
	}

	@Bean
	public GatewayService gatewayService(Rak7249Driver rak7249) {
		return new GatewayServiceImpl(rak7249);
	}

	@Bean
	public StateMgtService stateMgtService() {
		return new StateMgtServiceImpl();
	}

	@Bean
	public ReactorService ReactorService() {
		return new ReactorServiceImpl();
	}
	
	@Bean
	@ConfigurationProperties(prefix = "influxdb")
	public InfluxDBConfig influxDBConfig() {
		return new InfluxDBConfig();
	}

	@Bean
	public WriteInfluxDB WriteInfluxDB(InfluxDBConfig influxDBConfig) {
		return new WriteInfluxDBImpl(influxDBConfig);
	}

	@Bean
	public WriteSQL WriteSQL() {
		return new WriteSQLImpl();
	}

	@Bean
	@ConfigurationProperties(prefix = "publish.connect")
	public MqttConnectOptions publishMqttConnectOptopns() {
		return new MqttConnectOptions();
	}

	@Bean
	@ConfigurationProperties(prefix = "publish.mqtt")
	public MqttConfig publishMqttConfig(MqttConnectOptions publishMqttConnectOptopns) {
		var config = new MqttConfig();
		config.setConnectOptions(publishMqttConnectOptopns);
		return config;
	}

	@Bean
	private MqttPahoClientFactory publishMqttClientFactory(MqttConfig publishMqttConfig) {
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		factory.setConnectionOptions(publishMqttConfig.getConnectOptions());
		if(publishMqttConfig.getPersistFolder() != null)
			factory.setPersistence(new MqttDefaultFilePersistence(publishMqttConfig.getPersistFolder()));
		return factory;
	}

	/*
	 * Processing nodes
	 */
	
	@Bean
	public IntegrationFlow rak7249Input(
			MqttConfig rak7249MqttConfig, 
			MqttPahoClientFactory rak7249MqttClientFactory, 
			MessageChannel rak7249UplinkChannel,
			MqttRak7249Converter mqttRak7249Converter,
			LasRosasGateway gateway) {

		var adapter = ConfigUtils.mqttChannelAdapter("rak7249MqttInputAdapter", "application/+/device/+/+", rak7249MqttConfig, rak7249MqttClientFactory, mqttRak7249Converter);

		return IntegrationFlows.from(adapter)
				.channel(rak7249UplinkChannel)
				.handle((imessage) -> {
					log.info("=============== Processing message in transaction =================");

					var json = gson.toJson(imessage);
					messagesLog.info(imessage.getPayload().getClass().getSimpleName() + " = " + json);
					try {
						gateway.sendIntransaction(imessage);
						log.info("Message processed wihtout error");
					} catch(Exception e) {
						log.error("Error while processing the message", e);
						log.info("Error in the message processing");
					}
				})
                .get();
	}

    @Bean
	public IntegrationFlow rak7249ToLoraMessage(Rak7249Driver service, MessageChannel rak7249UplinkTxChannel, MessageChannel loraChannel) {

    	return IntegrationFlows
    				.from(rak7249UplinkTxChannel)
    				.<Message<? extends Rak7249Message>, Message<? extends LoraMessage>>transform(
    						new GenericTransformer<Message<? extends Rak7249Message>, Message<? extends LoraMessage>>() {

						@Override
						public Message<? extends LoraMessage> transform(Message<? extends Rak7249Message> imessage) {
							return service.transform(imessage);
							}
    				})
    				.channel(loraChannel)
    				.get();
    }

	@Bean
	public IntegrationFlow handleLoraMessages(
			MessageChannel loraChannel, 
			LoraService service) {

		var router = new PayloadTypeRouter();
		router.setChannelMapping(ThingEncodedMessage.class.getName(), thingEncodedDataChannelName);
		router.setChannelMapping(LoraMetricMessage.class.getName(), loraMetricChannelName);
		router.setChannelMapping(ConnectionState.class.getName(), stateChannelName);
		router.setChannelMapping(StillAlive.class.getName(), stateChannelName);
		router.setDefaultOutputChannelName(errorChannelName);

		return IntegrationFlows
				.from(loraChannel)
				.split(Message.class, imessage -> service.splitMessage(imessage))
				.route(router)
				.get();
	}

	@Bean
	public IntegrationFlow handleLoraMetric(MessageChannel loraMetricChannel, WriteInfluxDB influxDB, WriteSQL sql) {

		return IntegrationFlows
				.from(loraMetricChannel)
				.handle(imessage -> {
					var tsp = sql.writePoint(imessage);
					influxDB.writePoint(tsp.getTimeSerie(), imessage);
				})
				.get();
	}

	@Bean
	public IntegrationFlow handleThingMessage(MessageChannel thingEncodedDataChannel, MessageChannel telemetryChannel, SensorService service, WriteInfluxDB influxDB, WriteSQL sql) {

		return IntegrationFlows
					.from(thingEncodedDataChannel)
					.<Message<ThingEncodedMessage>, Message<? extends ThingDataMessage>>transform(imessage -> {
							var result = service.decodeUplink(imessage);
							var tsp = sql.writePoint(result);
							influxDB.writePoint(tsp.getTimeSerie(), result);
							return result;
						}
					)
					.split(new TelemetrySpliter(service))
					.channel(telemetryChannel)
					.get();
	}
	
	@Bean
	@Transformer(inputChannel = stateChannelName, outputChannel = telemetryChannelName)
	public AbstractTransformer handleStateMessage(StateMgtService service) {

		var handler = new AbstractTransformer() {

			@SuppressWarnings("unchecked")
			@Override
			protected Object doTransform(Message<?> imessage) {
				return service.handleStateMessage((Message<StateMessage>)imessage);
			}
		};

		return handler;
	}

	@Bean
	@ServiceActivator(inputChannel = telemetryChannelName)
	public MessageHandler writeTelemetry(WriteInfluxDB influxDB, WriteSQL sql) {
		return new AbstractMessageHandler() {

			@Override
			@Transactional
			protected void handleMessageInternal(Message<?> imessage) {
				var tsp = sql.writePoint(imessage);
				influxDB.writePoint(tsp.getTimeSerie(), imessage);
			}
		};
	}

	@Bean
	@Splitter(inputChannel = telemetryChannelName)
	@DependsOn({"writeTelemetry"})
	public AbstractSimpleMessageHandlerFactoryBean<AbstractMessageSplitter> reactor(ReactorService service, MessageChannel twinOutputChannel) {
		return new AbstractSimpleMessageHandlerFactoryBean<AbstractMessageSplitter>() {

	        @Override
			protected AbstractMessageSplitter createHandler() {
	        	var splitter = new ReactorSpliter(service);
	        	splitter.setOutputChannel(twinOutputChannel);
	        	return splitter;
	        }
		};
	}

	@Bean
	@Router(inputChannel = twinOutputChannelName)
	public PayloadTypeRouter twinOutputRouter() {

		var router = new PayloadTypeRouter() {

			@Override
			protected void handleMessageInternal(Message<?> message) {
				super.handleMessageInternal(message);
			}
			
		};
	    router.setChannelMapping(Order.class.getName(), orderChannelName);
	    router.setChannelMapping(Telemetry.class.getName(), telemetryChannelName);
	    router.setDefaultOutputChannelName(errorChannelName);

	    return router;
	}

	@Bean
	@Transformer(inputChannel = orderChannelName, outputChannel = downlinkChannelName)
	public AbstractTransformer encodeOrder(SensorService sensorService, GatewayService gatewayService) {
		return new DownlinkEncoderTransformer(sensorService, gatewayService);
	}

	@Bean
	@Router(inputChannel = downlinkChannelName)
	public HeaderValueRouter gatewayDownlinkRouter() {

		var router = new HeaderValueRouter(LasRosasHeaders.GATEWAY_NAURAL_ID);
	    router.setChannelMapping("rak7249", rak7249DownlinkChannelName);
	    router.setResolutionRequired(true);
	    router.setDefaultOutputChannelName(errorChannelName);

	    return router;
	}

	@Bean
	@ServiceActivator(inputChannel = rak7249DownlinkChannelName)
	public MessageHandler rak7249DownlinkPublisher(MqttPahoClientFactory rak7249MqttClientFactory) {
		MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("LasRosasIotDownlink", rak7249MqttClientFactory);
		messageHandler.setAsync(false);

		// Chripstack mqtt channel
		// https://forum.chirpstack.io/t/problem-with-sending-downlink-via-mqtt/4263
		messageHandler.setTopicExpressionString("'application/1/device/' + headers['ThingNaturalId'] + '/tx'");
		return messageHandler;
	}

	@Bean
	@Transformer(inputChannel = telemetryChannelName, outputChannel = publishMqttChannelName)
	public ToGsonTransformer thingDataToJson() {
	    return new ToGsonTransformer();
	}

	@Bean
	@ServiceActivator(inputChannel = publishMqttChannelName)
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
		// topic header is set in the jsontransformer
		messageHandler.setTopicExpressionString("headers['topic']");
		return messageHandler;
	}

	@Bean
	public MessageProducerSupport localChannelAdapter(
			MqttConfig publishMqttConfig, 
			MqttPahoClientFactory publishMqttClientFactory, 
			MessageChannel orderChannel,
			GatewayService gatewayService) {
		var adapter = ConfigUtils.mqttChannelAdapter("localChannelAdapter", "order", publishMqttConfig, publishMqttClientFactory, new OrderConverter(gatewayService));
		adapter.setOutputChannel(orderChannel);
		return adapter;
	}

	/*
	 * Gateways
	 */
	@MessagingGateway
	public static interface LasRosasGateway {

		@Gateway(requestChannel = rak7249UplinkChannelName)
		void sendRak7249(Rak7249Message message);

		@Gateway(requestChannel = telemetryChannelName)
		void sendTelemetry(Message<?> c);

		@Gateway(requestChannel = publishMqttChannelName)
		void sendToMqtt(Telemetry telemetry, @Header(MqttHeaders.TOPIC) String topic);

		@Gateway(requestChannel = orderChannelName)
		void sendOrder(Message<? extends Order> order);

		@Gateway(requestChannel = rak7249UplinkTxChannelName)
		@Transactional
	    void sendIntransaction(Message<?> msg);
	}
/*
	@MessagingGateway(defaultRequestChannel = rak7249UplinkTxChannel)
	interface TransactionalGateway {

	}
*/
	/*
	 * Scheduler
	 */
	@Bean
    public ThreadPoolTaskScheduler taskScheduler() {

		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(3);
        threadPoolTaskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
        return threadPoolTaskScheduler;
    }
}
