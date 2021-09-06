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
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.AbstractSimpleMessageHandlerFactoryBean;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.handler.AbstractMessageHandler;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.integration.router.PayloadTypeRouter;
import org.springframework.integration.splitter.AbstractMessageSplitter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lasrosas.iot.core.ingestor.lora.api.LoraMessageSplitter;
import com.lasrosas.iot.core.ingestor.lora.api.LoraMetricMessage;
import com.lasrosas.iot.core.ingestor.lora.api.LoraService;
import com.lasrosas.iot.core.ingestor.lora.impl.LoraServiceImpl;
import com.lasrosas.iot.core.ingestor.rak7249.api.Rak7249FluxLoraTransformer;
import com.lasrosas.iot.core.ingestor.rak7249.api.Rak7249Message;
import com.lasrosas.iot.core.ingestor.rak7249.api.Rak7249Service;
import com.lasrosas.iot.core.ingestor.rak7249.impl.Rak7249ServiceImpl;
import com.lasrosas.iot.core.ingestor.sensors.api.DecodeThingMessageTransformer;
import com.lasrosas.iot.core.ingestor.sensors.api.SensorService;
import com.lasrosas.iot.core.ingestor.sensors.api.TelemetrySpliter;
import com.lasrosas.iot.core.ingestor.sensors.api.ThingEncodedMessage;
import com.lasrosas.iot.core.ingestor.timeSerieWriter.api.InfluxDBConfig;
import com.lasrosas.iot.core.ingestor.timeSerieWriter.api.WriteInfluxDB;
import com.lasrosas.iot.core.ingestor.timeSerieWriter.api.WriteSQL;
import com.lasrosas.iot.core.ingestor.timeSerieWriter.impl.WriteInfluxDBImpl;
import com.lasrosas.iot.core.ingestor.timeSerieWriter.impl.WriteSQLImpl;
import com.lasrosas.iot.core.reactor.api.ReactorService;
import com.lasrosas.iot.core.reactor.api.ReactorSpliter;
import com.lasrosas.iot.core.reactor.base.ReactorServiceImpl;
import com.lasrosas.iot.core.shared.telemetry.Telemetry;

@ConfigurationProperties
@Validated
public class LasRosasIotConfig {
	public static final Logger log = LoggerFactory.getLogger(LasRosasIotConfig.class);
	public static final Logger messagesLog = LoggerFactory.getLogger("MessagesLog");

	public static final String rak7249Channel = "rak7249Channel";
	public static final String rak7249TxChannel = "rak7249TxChannel";
	public static final String loraChannel = "loraChannel";
	public static final String mixedLoraChannel = "mixedLoraChannel";
	public static final String loraMetricChannel = "loraMetricChannel";
	public static final String thingEncodedDataChannel = "thingEncodedDataChannel";
	public static final String thingDataChannel = "thingDataChannel";
	public static final String thingBatteryChannel = "thingBatteryChannel";
	public static final String telemetryChannel = "telemetryChannel";
	public static final String alarmChannel = "alarmChannel";
	public static final String publishMqttChannel = "publishMqttChannel";

	private Gson gson = new GsonBuilder().setPrettyPrinting().create();

	/*
	 * Channels
	 */
	@Bean
	public QueueChannel errorChannel() {
	    return new QueueChannel(500);
	}

	@Bean
	public MessageChannel rak7249Channel() {
		return new DirectChannel();
	}

	@Bean
	public MessageChannel rak7249TxChannel() {
		return new PublishSubscribeChannel();
	}

	@Bean
	public MessageChannel loraChannel() {
		return new PublishSubscribeChannel();
	}

	@Bean
	public MessageChannel loraMetricChannel() {
		return new PublishSubscribeChannel();
	}

	@Bean
	public MessageChannel sensorDataChannel() {
		return new PublishSubscribeChannel();
	}


	@Bean
	public MessageChannel mixedLoraChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageChannel thingDataChannel() {
		return new PublishSubscribeChannel();
	}

	@Bean
	public MessageChannel telemetryChannel() {
		return new PublishSubscribeChannel();
	}

	@Bean
	public MessageChannel publishMqttChannel() {
		return new DirectChannel();
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
	public Rak7249Service rak7249MessagesService() {
		return new Rak7249ServiceImpl();
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
	public MessageProducerSupport rak7249ChannelAdapter(
			MqttConfig rak7249MqttConfig, 
			MqttPahoClientFactory rak7249MqttClientFactory, 
			MessageChannel rak7249Channel,
			MqttRak7249Converter mqttRak7249Converter) {
		var adapter = ConfigUtils.mqttChannelAdapter("application/+/device/+/+", rak7249MqttConfig, rak7249MqttClientFactory, rak7249Channel, mqttRak7249Converter);
		adapter.setOutputChannel(rak7249Channel);
		return adapter;
	}

	@Bean
	@ServiceActivator(inputChannel = rak7249Channel)
	public MessageHandler processMessageInTransaction(TransactionalGateway gate) {
		return new AbstractMessageHandler() {

			@Override
			@Transactional
			protected void handleMessageInternal(Message<?> imessage) {
				log.info("");
				log.info("");
				log.info("=============== Processing message =================");

				var json = gson.toJson(imessage);
				messagesLog.info(imessage.getPayload().getClass().getSimpleName() + " = " + json);
				try {
					gate.sendIntransaction(imessage);
					log.info("Message processed wihtout error");
				} catch(Exception e) {
					log.error("Error while processing the message", e);
					log.info("Error in the message processing");
				}
			}
		};
	}

	/*
	 * This flow is used to execute the message processing in a single transaction.
	 */
/*
	@Bean
    public IntegrationFlow mqttInFlow(MessageProducerSupport rak7249ChannelAdapter, TransactionalGateway gate) {
        return IntegrationFlows.from(rak7249ChannelAdapter)
                .log()
                .handle(
                		msg -> {
                			gate.sendIntransaction(msg);
                		} )
                .get();
    }
*/
    @Bean
	@Transformer(inputChannel = rak7249TxChannel, outputChannel = loraChannel)
	public Rak7249FluxLoraTransformer rak7249ToLoraMessageTransformer(Rak7249Service service) {
		return new Rak7249FluxLoraTransformer(service);
	}

	@Bean
	@Splitter(inputChannel = loraChannel)
	public AbstractSimpleMessageHandlerFactoryBean<AbstractMessageSplitter> loraSplitter(LoraService service, MessageChannel mixedLoraChannel) {
		return new AbstractSimpleMessageHandlerFactoryBean<AbstractMessageSplitter>() {

			@Override
	        protected AbstractMessageSplitter createHandler() {
	        	var splitter = new LoraMessageSplitter(service);
	        	splitter.setOutputChannel(mixedLoraChannel);
	        	return splitter;
	        }
		};
	}

	@Bean
	@Router(inputChannel = mixedLoraChannel)
	public PayloadTypeRouter mixedLoraRouter() {

		var router = new PayloadTypeRouter();
	    router.setChannelMapping(ThingEncodedMessage.class.getName(), thingEncodedDataChannel);
	    router.setChannelMapping(LoraMetricMessage.class.getName(), loraMetricChannel);

	    return router;
	}

	@Bean
	@ServiceActivator(inputChannel = loraMetricChannel)
	public MessageHandler writeLoraMetric(WriteInfluxDB influxDB, WriteSQL sql) {
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
	@Transformer(inputChannel = thingEncodedDataChannel, outputChannel = thingDataChannel)
	@DependsOn({"writeLoraMetric"})
	public DecodeThingMessageTransformer decodeThingMessageTransformer(SensorService service) {
		return new DecodeThingMessageTransformer(service);
	}

	@Bean
	@Order(1)
	@ServiceActivator(inputChannel = thingDataChannel)
	public MessageHandler writeThingData(WriteInfluxDB influxDB, WriteSQL sql) {
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
	@Splitter(inputChannel = thingDataChannel)
	@Order(2)
	@DependsOn({"writeThingData"})
	public AbstractSimpleMessageHandlerFactoryBean<AbstractMessageSplitter> normalize(SensorService service, MessageChannel telemetryChannel) {
		return new AbstractSimpleMessageHandlerFactoryBean<AbstractMessageSplitter>() {

	        @Override
			protected AbstractMessageSplitter createHandler() {
	        	var splitter = new TelemetrySpliter(service);
	        	splitter.setOutputChannel(telemetryChannel);
	        	return splitter;
	        }
		};
	}


	@Bean
	@ServiceActivator(inputChannel = telemetryChannel)
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
	@Splitter(inputChannel = telemetryChannel)
	@DependsOn({"writeTelemetry"})
	public AbstractSimpleMessageHandlerFactoryBean<AbstractMessageSplitter> reactor(ReactorService service, MessageChannel telemetryChannel) {
		return new AbstractSimpleMessageHandlerFactoryBean<AbstractMessageSplitter>() {

	        @Override
			protected AbstractMessageSplitter createHandler() {
	        	var splitter = new ReactorSpliter(service);
	        	splitter.setOutputChannel(telemetryChannel);
	        	return splitter;
	        }
		};
	}

	@Bean
	@Transformer(inputChannel = telemetryChannel, outputChannel = publishMqttChannel)
	public ToGsonTransformer thingDataToJson() {
	    return new ToGsonTransformer();
	}

	@Bean
	@ServiceActivator(inputChannel = publishMqttChannel)
	public MessageHandler mqttPublisher(MqttPahoClientFactory publishMqttClientFactory) {
		MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("lasRosasIot", publishMqttClientFactory);
		messageHandler.setAsync(false);
		messageHandler.setDefaultTopic("lasrosas");
		return messageHandler;
	}

	/*
	 * Gateways
	 */
	@MessagingGateway
	public static interface LasRosasGateway {

		@Gateway(requestChannel = rak7249Channel)
		void sendRak7149(Rak7249Message message);

		@Gateway(requestChannel = publishMqttChannel)
		void sendToMqtt(Telemetry telemetry, @Header(MqttHeaders.TOPIC) String topic);
	}

	@MessagingGateway(defaultRequestChannel = rak7249TxChannel)
	interface TransactionalGateway {

	    @Transactional
	    void sendIntransaction(Message<?> msg);
	}
}
