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
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.validation.annotation.Validated;

import com.lasrosas.iot.ingestor.services.lora.api.LoraMessageSplitter;
import com.lasrosas.iot.ingestor.services.lora.api.LoraService;
import com.lasrosas.iot.ingestor.services.rak7249.api.Rak7249FluxLoraTransformer;
import com.lasrosas.iot.ingestor.services.rak7249.api.Rak7249Service;
import com.lasrosas.iot.ingestor.services.rak7249.impl.Rak7249ServiceImpl;
import com.lasrosas.iot.ingestor.services.sensors.api.DecodeThingMessageTransformer;
import com.lasrosas.iot.ingestor.services.sensors.api.SensorService;
import com.lasrosas.iot.ingestor.services.sensors.impl.SensorServiceImpl;
import com.lasrosas.iot.ingestor.shared.ConfigUtils;
import com.lasrosas.iot.shared.utils.MqttConfig;

@ConfigurationProperties
@Validated
public class LasRosasIotConfig {

	public static final String rak7249Channel = "rak7249Channel";
	public static final String loraChannel = "loraChannel";
	public static final String thingEncodedDataChannel = "sensorRawDataChannel";
	public static final String thingDataChannel = "thingDataChannel";
	public static final String thingBatteryChannel = "thingBatteryChannel";
	public static final String alarmChannel = "alarmChannel";
	public static final String publishMqttChannel = "publishMqttChannel";
	public static final String errorChannel = "errorChannel";

	@Bean
	public MessageChannel errorChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageChannel rak7249Channel() {
		return new PublishSubscribeChannel();
	}

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
	public MqttPahoMessageDrivenChannelAdapter rak7249ChannelAdapter(
			MqttConfig rak7249MqttConfig, 
			MqttPahoClientFactory rak7249MqttClientFactory, 
			MessageChannel rak7249MqttChannel) {
		return ConfigUtils.mqttChannelAdapter("application/+/device/+/+", rak7249MqttConfig, rak7249MqttClientFactory, rak7249MqttChannel);
	}

	@Bean
	public Rak7249Service rak7249MessagesService() {
		return new Rak7249ServiceImpl();
	}

	// Handle Lora normalized messages, independent if the type of gateway.
	@Bean
	public MessageChannel loraChannel() {
		return new PublishSubscribeChannel();
	}

	// To handle the messages passing through the channel
	@Bean
	@Transformer(inputChannel = rak7249Channel, outputChannel = loraChannel)
	public Rak7249FluxLoraTransformer rak7249ToLoraMessageTransformer(Rak7249Service service) {
		return new Rak7249FluxLoraTransformer(service);
	}

	@MessagingGateway(defaultRequestChannel = rak7249Channel, errorChannel = errorChannel)
	public interface rak7249MqttChannelGateway {
		void send(String data);
	}

	// Handle Lora normalized messages, independent if the type of gateway.
	@Bean
	public MessageChannel sensorDataChannel() {
		return new PublishSubscribeChannel();
	}

	@Bean
	public SensorService sensorService() {
		return new SensorServiceImpl();
	}

	// To handle the messages passing through the channel
	@Bean
	public LoraMessageSplitter handleLoraMessage(LoraService service) {
		return new LoraMessageSplitter(service);
	}

	// Handle Lora normalized messages, independent if the type of gateway.
	@Bean
	public MessageChannel thingDataChannel() {
		return new PublishSubscribeChannel();
	}

	// To handle the messages passing through the channel
	@Transformer(inputChannel = thingEncodedDataChannel, outputChannel = thingDataChannel)
	public DecodeThingMessageTransformer decodeThingMessageTransformer(SensorService service) {
		return new DecodeThingMessageTransformer(service);
	}

	@Bean
	public MessageChannel telemetryChannel() {
		return new PublishSubscribeChannel();
	}

	/////////////////////////
	@Bean
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

	@Bean
	public MessageChannel publishMqttChannel() {
		return new DirectChannel();
	}

	@Bean
	@ServiceActivator(inputChannel = publishMqttChannel)
	public MessageHandler mqttPublisher(MqttPahoClientFactory publishMqttClientFactory) {
		MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("lasRosasIot", publishMqttClientFactory);
		messageHandler.setAsync(false);
		return messageHandler;
	}

	@MessagingGateway(defaultRequestChannel = publishMqttChannel)
	public interface MqttIngestorOutGateway {
		void sendToMqtt(String data, @Header(MqttHeaders.TOPIC) String topic);
	}
}
