package com.lasrosas.iot.flux;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.integration.router.PayloadTypeRouter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.validation.annotation.Validated;

import com.lasrosas.iot.ingestor.services.lora.api.LoraMessage;
import com.lasrosas.iot.ingestor.services.lora.api.LoraMessageUpload;
import com.lasrosas.iot.ingestor.services.rak7249.api.Rak7249LoraTransformer;
import com.lasrosas.iot.ingestor.services.rak7249.api.Rak7249MessagesService;
import com.lasrosas.iot.ingestor.services.rak7249.impl.Rak7249MessagesServiceImpl;
import com.lasrosas.iot.shared.utils.MqttConfig;

import retrofit2.http.Header;

@ConfigurationProperties
@Validated
public class LasRosasIotConfig {

	public static final String rak7249Channel = "rak7249Channel";
	public static final String loraChannel = "loraChannel";
	public static final String thingDataChannel = "thingDataChannel";
	public static final String thingBatteryChannenl = "thingBatteryChannenl";
	public static final String alarmChannenl = "alarmChannenl";

	@Bean
	@ConfigurationProperties(prefix = "rak7249.mqtt")
	public MqttConfig rak7249MqttConfig() {
		return new MqttConfig();
	}

	// To connect to the RAK7249 mqtt broker
	@Bean
	private MqttPahoClientFactory rak7249MqttClientFactory(MqttConfig rak7249MqttConfig) {
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		factory.setConnectionOptions(rak7249MqttConfig.getConnectOptions());
		return factory;
	}

	// MQTT connector to get the messages from the application/# topic
	@Bean
	public MqttPahoMessageDrivenChannelAdapter rak7249ChannelAdapter(MqttConfig rak7249MqttConfig, MqttPahoClientFactory rak7249MqttClientFactory) {

		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
				rak7249MqttConfig.getClientId(),
				rak7249MqttClientFactory,
				"application/+/device/+/+");

		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setOutputChannel(rak7249MqttChannel());

		// Set options from config
		if( rak7249MqttConfig.getCompletionTimeout() != null) adapter.setCompletionTimeout(rak7249MqttConfig.getCompletionTimeout());
		if( rak7249MqttConfig.getQoss() != null) adapter.setQos(rak7249MqttConfig.getQoss());
		if( rak7249MqttConfig.getDisconnectCompletionTimeout() != null) adapter.setDisconnectCompletionTimeout(rak7249MqttConfig.getDisconnectCompletionTimeout());
		if( rak7249MqttConfig.getRecoveryInterval() != null) adapter.setRecoveryInterval(rak7249MqttConfig.getRecoveryInterval());
		if( rak7249MqttConfig.getSendTimeout() != null) adapter.setSendTimeout(rak7249MqttConfig.getSendTimeout());

		return adapter;
	}

	@Bean
	public MessageChannel rak7249MqttChannel() {
		return new PublishSubscribeChannel();
	}

	@Bean
	public Rak7249MessagesService rak7249MessagesService() {
		return new Rak7249MessagesServiceImpl();
	}

	// To handle the messages passing through the channel
	@Bean
	@Transformer(inputChannel = rak7249Channel, outputChannel = "rak7249ToLoraMessageTransformer")
	public Rak7249LoraTransformer rak7249ToLoraMessageTransformer(Rak7249MessagesService service) {
		return new Rak7249LoraTransformer(service);
	}

	@Bean
	public MessageChannel rak7249ToLoraMessageTransformer() {
		return new DirectChannel();
	}

	@ServiceActivator(inputChannel = "rak7249ToLoraMessageTransformer")
	@Bean
	public PayloadTypeRouter router() {
	    PayloadTypeRouter router = new PayloadTypeRouter();
	    router.setChannelMapping(LoraMessage.class.getName(), "loraChannel");
	    router.setChannelMapping(EncodedPayloadMessage.class.getName(), "payloadChannel");
	    return router;
	}

	// Handle Lora normalized messages, independent if the type of gateway.
	@Bean
	public MessageChannel loraChannel() {
		return new PublishSubscribeChannel();
	}

	// Handle Lora normalized messages, independent if the type of gateway.
	@Bean
	public MessageChannel sensorPayloadChannel() {
		return new PublishSubscribeChannel();
	}

	@Bean
	@ServiceActivator(inputChannel = "sensorPayloadChannel", outputChannel="sensorDataChannel")
	public Message<?> parseSensorDataTransformer(Message<EncodedSensorPayload> ingestor) {
	}

	@ServiceActivator(inputChannel = "thingDataChannel", outputChannel = "ThingMessage")
	@Bean
	public IotMessage normalizeSplit() {
	}

	@Bean
	public MessageChannel thingBatteryChannel() {
		return new PublishSubscribeChannel();
	}

	@Bean
	public MessageChannel thingDataChannel() {
		return new PublishSubscribeChannel();
	}

	@Bean
	public MessageChannel alarmChannel() {
		return new PublishSubscribeChannel();
	}

	@MessagingGateway(defaultRequestChannel = "mqttLoraChannel")
	public interface MqttLoraGateway {
		void send(String data, @Header(MqttHeaders.TOPIC) String topic);
	}

	// IngestorOut goes to the local mqtt broker
	@Bean
	@ConfigurationProperties(prefix = "mqtt")
	public MqttConfig mqttIngestorOutConfig() {
		return new MqttConfig();
	}

	@Bean
	private MqttPahoClientFactory mqttIngestorOutClientFactory(MqttConfig mqttIngestorOutConfig) {
		return mqttClientFactory(mqttIngestorOutConfig);
	}

	@Bean
	public MessageChannel mqttIngestorOutChannel() {
		return new DirectChannel();
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttIngestorOutChannel")
	public MessageHandler mqttOutbound(MqttPahoClientFactory mqttPahoClientFactory) {
		MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("mqttIngestorOut", mqttPahoClientFactory);
		messageHandler.setAsync(false);
		return messageHandler;
	}

	@MessagingGateway(defaultRequestChannel = "mqttIngestorOutChannel")
	public interface MqttIngestorOutGateway {
		void sendToMqtt(String data, @Header(MqttHeaders.TOPIC) String topic);
	}
}
