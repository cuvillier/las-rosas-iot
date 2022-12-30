package com.lasrosas.iot.core.flux;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.config.AbstractSimpleMessageHandlerFactoryBean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.handler.AbstractMessageHandler;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.router.HeaderValueRouter;
import org.springframework.integration.router.PayloadTypeRouter;
import org.springframework.integration.splitter.AbstractMessageSplitter;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.transformer.AbstractTransformer;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.google.gson.Gson;
import com.lasrosas.iot.core.ingestor.gateway.api.GatewayService;
import com.lasrosas.iot.core.ingestor.gateway.impl.rak7249.api.Rak7249Driver;
import com.lasrosas.iot.core.ingestor.gateway.impl.rak7249.api.Rak7249Message;
import com.lasrosas.iot.core.ingestor.lora.api.LoraMessage;
import com.lasrosas.iot.core.ingestor.lora.api.LoraMetricMessage;
import com.lasrosas.iot.core.ingestor.lora.api.LoraService;
import com.lasrosas.iot.core.ingestor.parsers.api.DownlinkEncoderTransformer;
import com.lasrosas.iot.core.ingestor.parsers.api.SensorService;
import com.lasrosas.iot.core.ingestor.parsers.api.TelemetrySpliter;
import com.lasrosas.iot.core.ingestor.parsers.api.ThingDataMessage;
import com.lasrosas.iot.core.ingestor.parsers.api.ThingEncodedMessage;
import com.lasrosas.iot.core.ingestor.statemgt.api.StateMgtService;
import com.lasrosas.iot.core.ingestor.statemgt.impl.TimeoutThingTask;
import com.lasrosas.iot.core.ingestor.timeSerieWriter.api.WriteInfluxDB;
import com.lasrosas.iot.core.ingestor.timeSerieWriter.api.WriteSQL;
import com.lasrosas.iot.core.reactor.api.ReactorService;
import com.lasrosas.iot.core.reactor.api.ReactorSpliter;
import com.lasrosas.iot.core.shared.telemetry.ConnectionState;
import com.lasrosas.iot.core.shared.telemetry.StateMessage;
import com.lasrosas.iot.core.shared.telemetry.StillAlive;
import com.lasrosas.iot.core.shared.telemetry.Telemetry;
import com.lasrosas.iot.core.shared.utils.LasRosasHeaders;

/**
 * Create flux elementary services. These services are called by the Spring integration flux.
 * Each service much have a dedicated unit test.
 * 
 * Each event is handled in a single DB transaction to process the event over the multiples services.
 * The transaciton is start by the Gateway method processInTransaction().
 * Transactions are managed for the mySQL database only. The other resources such as InfluxDB
 * do not participate to the transaction.

 * @author tibo
 *
 */
@ConfigurationProperties
@Validated
public class LasRosasFluxDelegate {
	public static final Logger log = LoggerFactory.getLogger(LasRosasFluxDelegate.class);

	/* Log messages received for debuging purposes */
	public static final Logger messagesLog = LoggerFactory.getLogger("MessagesLog");

	@Autowired
	private Gson gson;

	/* --------------------------------------------------------------------------
	 * 
	 * These beans defined the connection parameters to the RAK7249 MQTT broker.
	 * Connection properties are read from :
	 * rak7249
	 * 		connect
	 * 		mqtt
	 */

	// @Bean
	// @ConfigurationProperties(prefix = "rak7249.connect")
	public MqttConnectOptions rak7249MqttConnectOptions() {
		return new MqttConnectOptions();
	}

	// @Bean
	// @ConfigurationProperties(prefix = "rak7249.mqtt")
	public MqttConfig rak7249MqttConfig(MqttConnectOptions rak7249MqttConnectOptions) {
		var config = new MqttConfig();
		config.setConnectOptions(rak7249MqttConnectOptions);
		return config;
	}

	// @Bean
	public MqttPahoClientFactory rak7249MqttClientFactory(MqttConfig rak7249MqttConfig) {
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		factory.setConnectionOptions(rak7249MqttConfig.getConnectOptions());
		return factory;
	}


	/* --------------------------------------------------------------------------
	 *
	 * These beans defined the connection parameters to the local MQTT broker.
	 * Connection properties are read from :
	 * publish
	 * 		connect
	 * 		mqtt
	 */

	// @Bean
	// @ConfigurationProperties(prefix = "publish.connect")
	public MqttConnectOptions publishMqttConnectOptopns() {
		return new MqttConnectOptions();
	}

	// @Bean
	// @ConfigurationProperties(prefix = "publish.mqtt")
	public MqttConfig publishMqttConfig(MqttConnectOptions publishMqttConnectOptopns) {
		var config = new MqttConfig();
		config.setConnectOptions(publishMqttConnectOptopns);
		return config;
	}


	// @Bean
	public MqttPahoClientFactory publishMqttClientFactory(MqttConfig publishMqttConfig) {
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		factory.setConnectionOptions(publishMqttConfig.getConnectOptions());
		if (publishMqttConfig.getPersistFolder() != null)
			factory.setPersistence(new MqttDefaultFilePersistence(publishMqttConfig.getPersistFolder()));
		return factory;
	}

	/* --------------------------------------------------------------------------
	 * Processing nodes. Theses services are called by the Spring integration flux.
	 */

	/**
	 * Create a channel connected to the RAK7249 Mqtt broker to get the events formt he Lrawan sensors.
	 * Get the mqtt message from the RAK7249 Lora gateway.
	 * Store the received messages info the message log file.
	 * 
	 * Start a single DB transaction to process the event over the multiples services.
	 * Transactions are managed for the mySQL database only. The other resources such as InfluxDB
	 * do not participate to the transaction.
	 * 
	 * Events are sent into the rak7249UplinkTxChannel channel.
	 * 
	 * @param rak7249MqttConfig
	 * @param rak7249MqttClientFactory
	 * @param rak7249UplinkChannel
	 * @param rak7249Driver
	 * @param gateway
	 * @return integration flow config
	 */
	public IntegrationFlow rak7249Input(MqttConfig rak7249MqttConfig, MqttPahoClientFactory rak7249MqttClientFactory,
			MessageChannel rak7249UplinkChannel, Rak7249Driver rak7249Driver, LasRosasGateway gateway) {

		var adapter = ConfigUtils.mqttChannelAdapter("rak7249MqttInputAdapter", "application/+/device/+/+",
				rak7249MqttConfig, rak7249MqttClientFactory);

		return IntegrationFlows.from(adapter).channel(rak7249UplinkChannel).handle((imessage) -> {
			messagesLog.info("=============> Received message from RAK7249");
			messagesLog.info(imessage.getPayload().getClass().getSimpleName() + " = " + gson.toJson(imessage));

			var json = (String) imessage.getPayload();
			var topic = (String) imessage.getHeaders().get("mqtt_receivedTopic");
			var rakMessage = rak7249Driver.fromJson(topic, json);
			var message = MessageBuilder.withPayload(rakMessage).copyHeaders(imessage.getHeaders()).build();

			try {
				gateway.processInTransaction(message);
			} catch (Exception e) {
				log.error("Error while processing the message", e);
			}
		}).get();
	}

	/**
	 * Transform RAK7249 messages to messages nomalized model LoraMessage,
	 * independent of the Lora Gateway uased.
	 * 
	 * Lora message can be:
	 *  - Rx message containign sensor data
	 *  - Join messages sent when the sensor initiate a Lorawan session
	 *  - Ack message sent to acknowledge downlink message sent by the Gateway.
	 *  
	 * All these events are sent to the LoraChannel.
	 * 
	 * @param service
	 * @param rak7249UplinkTxChannel
	 * @param loraChannel
	 * @return
	 */
	public IntegrationFlow rak7249ToLoraMessage(Rak7249Driver service, MessageChannel rak7249UplinkTxChannel,
			MessageChannel loraChannel) {

		return IntegrationFlows.from(rak7249UplinkTxChannel)
				.<Message<? extends Rak7249Message>, Message<? extends LoraMessage>>transform(
						new GenericTransformer<Message<? extends Rak7249Message>, Message<? extends LoraMessage>>() {

							@Override
							public Message<? extends LoraMessage> transform(
									Message<? extends Rak7249Message> imessage) {

								// Map RAK7249 message to a LoraMessage
								return service.transform(imessage);
							}
						})
				.channel(loraChannel).get();
	}

	/**
	 * Handle the Lora messages:
	 * 
	 * - LoraMessageUplink =>
	 * 		ThingEncodedMessage containing the encoded data
	 * 		LoraMetricMessage containing the Lora metric, lie SNR
	 * 		StillAlive to update the sensor connection state
	 * 
	 * - LoraMessageJoin =>
	 * 		StateMessage to change the sensor "connection" state
	 * 		If the Thing does not exists, it may be created, if the Sensor name
	 *    	in the Gateway contains the Manufacturer and model names.
	 * 
	 * - LoraMessageAck =>
	 * 		StillAlive to update the sensor connection state
	 * 
	 * Each message is routed to specific channels:
	 * 		thingEncodedDataChannelName
	 * 		loraMetricChannelName
	 * 		stateChannelName
	 * 		errorChannelName
	 * 
	 * @param loraChannel
	 * @param service
	 * @return
	 */
	public IntegrationFlow handleLoraMessages(MessageChannel loraChannel, LoraService service) {

		var router = new PayloadTypeRouter();
		router.setChannelMapping(ThingEncodedMessage.class.getName(), LasRosasIotBaseConfig.thingEncodedDataChannelName);
		router.setChannelMapping(LoraMetricMessage.class.getName(), LasRosasIotBaseConfig.loraMetricChannelName);
		router.setChannelMapping(ConnectionState.class.getName(), LasRosasIotBaseConfig.stateChannelName);
		router.setChannelMapping(StillAlive.class.getName(), LasRosasIotBaseConfig.stateChannelName);
		router.setDefaultOutputChannelName(LasRosasIotBaseConfig.errorChannelName);

		return IntegrationFlows.from(loraChannel).split(Message.class, imessage -> service.splitMessage(imessage))
				.route(router).get();
	}

	/**
	 * Write LoraMetric messages to the MySql and InfluxDB.
	 * Consume the message, do not forward any other message.
	 * 
	 * @param loraMetricChannel
	 * @param influxDB
	 * @param sql
	 * @return
	 */
	public IntegrationFlow handleLoraMetric(MessageChannel loraMetricChannel, WriteInfluxDB influxDB, WriteSQL sql) {

		return IntegrationFlows.from(loraMetricChannel).handle(imessage -> {
			var tsp = sql.writePoint(imessage);
			influxDB.writePoint(tsp.getTimeSerie(), imessage);
		}).get();
	}

	/**
	 * Process the LoraEncodedMessage:
	 * 	- Decode the message using the Manufacturer / Model decoder
	 * 	- Write the decoded message into the MySQL and InfluxDB database.
	 * 
	 * Map the decoded messages to normalized sensor Telemetry independent of the sensor type.
	 * Telemetry are sent to the telemetryChannel.
	 * 
	 * @param thingEncodedDataChannel
	 * @param telemetryChannel
	 * @param service
	 * @param influxDB
	 * @param sql
	 * @return
	 */
	public IntegrationFlow handleThingMessage(MessageChannel thingEncodedDataChannel, MessageChannel telemetryChannel,
			SensorService service, WriteInfluxDB influxDB, WriteSQL sql) {

		/*
		 * The transform required a Message<>, not the payload type. In order to make
		 * that working, use a GenericTransformer with the Message<> data type, do not
		 * use direct lambda. half a day lost to solve this....
		 */
		return IntegrationFlows.from(thingEncodedDataChannel)
				.<Message<ThingEncodedMessage>, Message<? extends ThingDataMessage>>transform(
						new GenericTransformer<Message<ThingEncodedMessage>, Message<? extends ThingDataMessage>>() {

							@Override
							public Message<? extends ThingDataMessage> transform(
									Message<ThingEncodedMessage> imessage) {
								var result = service.decodeUplink(imessage);
								if(sql != null && influxDB != null) {
									var tsp = sql.writePoint(result);
	
									// If writeSQL is mocked for testing.
									if(tsp != null) influxDB.writePoint(tsp.getTimeSerie(), result);
								}

								return result;
							}
						})
				.split(new TelemetrySpliter(service)).channel(telemetryChannel).get();
	}

	/**
	 * Handle the StateMessage, update the sensor connection state and related Alarms.
	 * Consume the incoming message, do no produce any other one.
	 * 
	 * @param service
	 * @return
	 */
	// @Bean
	// @Transformer(inputChannel = stateChannelName, outputChannel = telemetryChannelName)
	public AbstractTransformer handleStateMessage(StateMgtService service) {

		var handler = new AbstractTransformer() {

			@SuppressWarnings("unchecked")
			@Override
			protected Object doTransform(Message<?> imessage) {
				return service.handleStateMessage((Message<StateMessage>) imessage);
			}
		};

		return handler;
	}

	/**
	 * Write the Telemetry message the MySql and InfluxDB.
	 * @param influxDB
	 * @param sql
	 * @return
	 */
	// @Bean
	// @ServiceActivator(inputChannel = telemetryChannelName)
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

	/**
	 * Send the Telemetry messages from the things to the linked DigitalTwins.
	 * The job is done by the Reactor, the mapping between Things and Ditgital Twins.
	 * Forward the digital twin messages to twinOutputChannel.
	 * 
	 * @param service
	 * @param twinOutputChannel
	 * @return
	 */
	// @Bean
	// @Splitter(inputChannel = telemetryChannelName)
	// @DependsOn({ "writeTelemetry" })
	public AbstractSimpleMessageHandlerFactoryBean<AbstractMessageSplitter> reactOnMessage(ReactorService service,
			MessageChannel twinOutputChannel) {
		return new AbstractSimpleMessageHandlerFactoryBean<AbstractMessageSplitter>() {

			@Override
			protected AbstractMessageSplitter createHandler() {
				var splitter = new ReactorSpliter(service);
				splitter.setOutputChannel(twinOutputChannel);
				return splitter;
			}
		};
	}

	/**
	 * Route the dtwin messages:
	 * - Order going to the sensor goes to orderChannelName
	 * - Telemetry going from the sensor to the dtwin goes to telemetryChannelName
	 * 
	 * @return
	 */
	// @Bean
	// @Router(inputChannel = twinOutputChannelName)
	public PayloadTypeRouter twinOutputRouter() {

		var router = new PayloadTypeRouter() {

			@Override
			protected void handleMessageInternal(Message<?> message) {
				super.handleMessageInternal(message);
			}

		};

		router.setChannelMapping(Order.class.getName(), LasRosasIotBaseConfig.orderChannelName);
		router.setChannelMapping(Telemetry.class.getName(), LasRosasIotBaseConfig.telemetryChannelName);
		router.setDefaultOutputChannelName(LasRosasIotBaseConfig.errorChannelName);

		return router;
	}

	/**
	 * Handle OrderMessage, encode the Order to the sensor encoding scheme and to the Gateway scheme.
	 * The result is a Json sent to downlinkChannel
	 * 
	 * @param sensorService
	 * @param gatewayService
	 * @return
	 */
	// @Bean
	// @Transformer(inputChannel = orderChannelName, outputChannel = downlinkChannelName)
	public AbstractTransformer encodeOrder(SensorService sensorService, GatewayService gatewayService) {
		return new DownlinkEncoderTransformer(sensorService, gatewayService);
	}

	/**
	 * Route the message to the Gateway downlink channel.
	 * downlinkChannel messages goes to rak7249Channel
	 * 
	 * @return
	 */
	// @Bean
	// @Router(inputChannel = downlinkChannelName)
	public HeaderValueRouter gatewayDownlinkRouter() {

		var router = new HeaderValueRouter(LasRosasHeaders.GATEWAY_NAURAL_ID);

		// GatewayTechoFinca: Gateway NaturalId from the database.
		router.setChannelMapping("GatewayTechoFinca", LasRosasIotBaseConfig.rak7249ChannelName);
		router.setResolutionRequired(true);
		router.setDefaultOutputChannelName(LasRosasIotBaseConfig.errorChannelName);

		return router;
	}

	/**
	 * Send the downlink message to the rak7249 Gateway.
	 * 
	 * @param rak7249MqttClientFactory
	 * @return
	 */
	// @Bean
	// @ServiceActivator(inputChannel = rak7249ChannelName)
	public MessageHandler rak7249DownlinkPublisher(MqttPahoClientFactory rak7249MqttClientFactory) {
		MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("LasRosasIotDownlink",
				rak7249MqttClientFactory);
		messageHandler.setAsync(false);

		// Chripstack mqtt channel
		// https://forum.chirpstack.io/t/problem-with-sending-downlink-via-mqtt/4263
		messageHandler.setTopicExpressionString("'application/1/device/' + headers['ThingNaturalId'] + '/rx'");
		return messageHandler;
	}

	/**
	 * Reformat the Telemery messages to Json. The topic field is computed and added.
	 * The topic field must be added before the mqttPublisher is called.
	 * 
	 * @return
	 */
	// @Bean
	// @Transformer(inputChannel = telemetryChannelName, outputChannel = publishMqttChannelName)
	public ToGsonTransformer thingDataToJson() {
		return new ToGsonTransformer();
	}

	/**
	 * Publish the telemetry messages from the publishMqttChannel to the local MQTT broker
	 * 
	 * @param publishMqttClientFactory
	 * @return
	 */
	// @Bean
	// @ServiceActivator(inputChannel = publishMqttChannelName)
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

	/*
	 * Gateways
	 */
	@MessagingGateway
	public static interface LasRosasGateway {

		@Gateway(requestChannel = LasRosasIotBaseConfig.telemetryChannelName)
		void sendTelemetry(Message<?> c);

		@Gateway(requestChannel = LasRosasIotBaseConfig.rak7249UplinkTxChannelName)
		@Transactional
		void processInTransaction(Message<Rak7249Message> msg);
	}

	/*
	 * Scheduler
	 */

	public TimeoutThingTask timeoutThingTask(LasRosasGateway gateway) {
		return new TimeoutThingTask((c) -> {
			log.info("Start Handling disconnection");
			gateway.sendTelemetry(c);
			log.info("Stop Handling disconnection");
		});
	}

	// @Bean
	public ThreadPoolTaskScheduler taskScheduler() {

		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
		threadPoolTaskScheduler.setPoolSize(3);
		threadPoolTaskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
		return threadPoolTaskScheduler;
	}
}
