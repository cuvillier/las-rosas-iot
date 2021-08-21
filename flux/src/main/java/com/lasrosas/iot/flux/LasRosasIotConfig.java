package com.lasrosas.iot.flux;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
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
import org.springframework.validation.annotation.Validated;

import com.lasrosas.iot.ingestor.services.lora.api.LoraMessageSplitter;
import com.lasrosas.iot.ingestor.services.lora.api.LoraMetricMessage;
import com.lasrosas.iot.ingestor.services.lora.api.LoraService;
import com.lasrosas.iot.ingestor.services.lora.impl.LoraServiceImpl;
import com.lasrosas.iot.ingestor.services.rak7249.api.Rak7249FluxLoraTransformer;
import com.lasrosas.iot.ingestor.services.rak7249.api.Rak7249Message;
import com.lasrosas.iot.ingestor.services.rak7249.api.Rak7249Service;
import com.lasrosas.iot.ingestor.services.rak7249.impl.Rak7249ServiceImpl;
import com.lasrosas.iot.ingestor.services.sensors.api.DecodeThingMessageTransformer;
import com.lasrosas.iot.ingestor.services.sensors.api.SensorService;
import com.lasrosas.iot.ingestor.services.sensors.api.ThingEncodedMessage;
import com.lasrosas.iot.ingestor.services.sensors.impl.SensorServiceImpl;
import com.lasrosas.iot.ingestor.services.timeSerieWriter.api.WriteInfluxDB;
import com.lasrosas.iot.shared.telemetry.Telemetry;
import com.lasrosas.iot.shared.utils.MqttConfig;

@ConfigurationProperties
@Validated
public class LasRosasIotConfig {

	public static final String rak7249Channel = "rak7249Channel";
	public static final String loraChannel = "loraChannel";
	public static final String mixedLoraChannel = "mixedLoraChannel";
	public static final String loraMetricChannel = "loraMetricChannel";
	public static final String thingEncodedDataChannel = "sensorRawDataChannel";
	public static final String thingDataChannel = "thingDataChannel";
	public static final String thingBatteryChannel = "thingBatteryChannel";
	public static final String alarmChannel = "alarmChannel";
	public static final String publishMqttChannel = "publishMqttChannel";

	/*
	 * Channels
	 */
	@Bean
	public QueueChannel errorChannel() {
	    return new QueueChannel(500);
	}

	@Bean
	public MessageChannel rak7249Channel() {
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
	@ConfigurationProperties(prefix = "rak7249.mqtt")
	public MqttConfig rak7249MqttConfig() {
		return new MqttConfig();
	}
/*
	// To connect to the RAK7249 mqtt broker
	@Bean
	private MqttPahoClientFactory rak7249MqttClientFactory(MqttConfig rak7249MqttConfig) {
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		factory.setConnectionOptions(rak7249MqttConfig.getConnectOptions());
		return factory;
	}
*/

	@Bean
	public Rak7249Service rak7249MessagesService() {
		return new Rak7249ServiceImpl();
	}

	@Bean
	public SensorService sensorService() {
		return new SensorServiceImpl();
	}

	@Bean
	public LoraService loraService() {
		return new LoraServiceImpl();
	}

	@ConfigurationProperties(prefix = "publish.mqtt")
	public MqttConfig publishMqttConfig() {
		return new MqttConfig();
	}

	// To connect to the RAK7249 mqtt broker
	@Bean
	private MqttPahoClientFactory publishMqttClientFactory(MqttConfig publishMqttConfig) {
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		factory.setConnectionOptions(publishMqttConfig.getConnectOptions());
		return factory;
	}

	/*
	 * Processing nodes
	 */

	/*
	@Bean
	public MqttPahoMessageDrivenChannelAdapter rak7249ChannelAdapter(
			MqttConfig rak7249MqttConfig, 
			MqttPahoClientFactory rak7249MqttClientFactory, 
			MessageChannel rak7249MqttChannel) {
		return ConfigUtils.mqttChannelAdapter("application/+/device/+/+", rak7249MqttConfig, rak7249MqttClientFactory, rak7249MqttChannel);
	}
	 */

	// To handle the messages passing through the channel
	@Bean
	@Transformer(inputChannel = rak7249Channel, outputChannel = loraChannel)
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

	@ServiceActivator(inputChannel = loraMetricChannel)
	public MessageHandler writeLoraMetric(WriteInfluxDB influxDB) {
		return new AbstractMessageHandler() {

			@Override
			protected void handleMessageInternal(Message<?> message) {
				var payload = (Telemetry)message.getPayload();
				var measurement = payload.getClass().getName().replaceAll("\\.", "_");
				influxDB.writePoint(measurement, payload);
			}
		};
	}

	// To handle the messages passing through the channel
	@Transformer(inputChannel = thingEncodedDataChannel, outputChannel = thingDataChannel)
	public DecodeThingMessageTransformer decodeThingMessageTransformer(SensorService service) {
		return new DecodeThingMessageTransformer(service);
	}

	@Bean
	@ServiceActivator(inputChannel = publishMqttChannel)
	public MessageHandler mqttPublisher(MqttPahoClientFactory publishMqttClientFactory) {
		MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("lasRosasIot", publishMqttClientFactory);
		messageHandler.setAsync(false);
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
}
