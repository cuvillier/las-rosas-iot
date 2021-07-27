package com.lasrosas.iot.ingestor;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.validation.annotation.Validated;

import com.google.gson.JsonObject;
import com.lasrosas.iot.ingestor.services.rak7249.impl.Rak7249MessagesServiceImpl;
import com.lasrosas.iot.ingestor.services.sensors.impl.PayloadParsers;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8170BAParser;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8180BAParser;
import com.lasrosas.iot.ingestor.services.sensors.impl.elsys.ElsysErsParser;
import com.lasrosas.iot.ingestor.services.sensors.impl.elsys.ElsysGenericParser;
import com.lasrosas.iot.ingestor.services.sensors.impl.elsys.ElsysMB7389Parser;
import com.lasrosas.iot.ingestor.services.sensors.impl.mfc88.MFC88LW13IOParser;
import com.lasrosas.iot.shared.utils.MqttConfig;

import retrofit2.http.Header;

@ConfigurationProperties
@Validated
public class IngestorConfig {

	@Bean
	public LoraIngestor LoraIngestor(PayloadParsers sensors) {
		return new LoraIngestor(sensors);
	}

	@Bean
	public Rak7249MessagesServiceImpl loraServerRAK7249() {
		return new Rak7249MessagesServiceImpl();
	}

	@Bean
	public AdeunisARF8180BAParser AdenuisARF8180BAParser() {
		return new AdeunisARF8180BAParser();
	}

	@Bean
	public AdeunisARF8170BAParser AdenuisARF8170BAParser() {
		return new AdeunisARF8170BAParser();
	}

	@Bean
	public ElsysGenericParser ElsysGenericParser() {
		return new ElsysGenericParser();
	}

	@Bean
	public ElsysErsParser ElsysErsParser() {
		return new ElsysErsParser();
	}

	@Bean
	public ElsysMB7389Parser ElsysMB7389Parser() {
		return new ElsysMB7389Parser();
	}

	@Bean
	public MFC88LW13IOParser MFC88LW13IOParser() {
		return new MFC88LW13IOParser();
	}

	@Bean
	@ConfigurationProperties(prefix = "lora-sensors")
	public PayloadParsers LoraSensors(
			AdeunisARF8180BAParser adenuisARF8180BAParser,
			AdeunisARF8170BAParser adenuisARF8170BAParser,
			ElsysErsParser elsysErsParser,
			ElsysMB7389Parser elsysMB7389Parser,
			MFC88LW13IOParser mfc88LW1310Parser) {

		return new PayloadParsers(adenuisARF8180BAParser, adenuisARF8170BAParser, elsysErsParser, elsysMB7389Parser, mfc88LW1310Parser);
	}

	// Get messages from RAK7249 gateway, normalize to Lora ontology
	@Bean
	@ConfigurationProperties(prefix = "lora-servers.rak7249.mqtt")
	public MqttConfig mqttRAK7942Config() {
		return new MqttConfig();
	}

	@Bean
	public MessageChannel mqttRAK7942Channel() {
		return new DirectChannel();
	}

	// To connect to the RAK7249 mqtt broker
	@Bean
	private MqttPahoClientFactory mqttRAK7942ClientFactory(MqttConfig mqttRAK7942Config) {
		return mqttClientFactory(mqttRAK7942Config);
	}

	private MqttPahoClientFactory mqttClientFactory(MqttConfig config) {
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		MqttConnectOptions options = new MqttConnectOptions();
		options.setServerURIs(new String[] { config.getURL() });
		if (config.getAutomaticReconnect() != null)
			options.setAutomaticReconnect(config.getAutomaticReconnect());
		if (config.getCleanSession() != null)
			options.setCleanSession(config.getCleanSession());
		if (config.getConnectionTimeout() != null)
			options.setConnectionTimeout(config.getConnectionTimeout());
		if (config.getMaxReconnectDelay() != null)
			options.setMaxReconnectDelay(config.getMaxReconnectDelay());
		if (config.getConnectionTimeout() != null)
			options.setConnectionTimeout(config.getConnectionTimeout());
		factory.setConnectionOptions(options);
		return factory;
	}

	// MQTT connector to get the messages from the application/# topic
	@Bean
	public MqttPahoMessageDrivenChannelAdapter mqttRAK7942ChannelAdapter(MqttConfig mqttRAK7942Config, MqttPahoClientFactory mqttRAK7942ClientFactory) {

		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(mqttRAK7942Config.getClientId(),
				mqttRAK7942ClientFactory, "application/+/device/+/+");

		if( mqttRAK7942Config.getCompletionTimeout() != null) adapter.setCompletionTimeout(mqttRAK7942Config.getCompletionTimeout());
		adapter.setConverter(new DefaultPahoMessageConverter());
		if( mqttRAK7942Config.getQOS() != null) adapter.setQos(mqttRAK7942Config.getQOS());
		adapter.setOutputChannel(mqttRAK7942Channel());

		return adapter;
	}

	// To handle the messages passing through the channel
	@Bean
	@ServiceActivator(inputChannel = "mqttRAK7942Channel", outputChannel = "mqttLoraChannel")
	public MessageHandler handlerRAK7249(Rak7249MessagesServiceImpl rak7249) {
		return new MessageHandler() {

			@Override
			public void handleMessage(Message<?> message) throws MessagingException {

				// Decode, save messages and send messages to the Lora pubsub channel.
				rak7249.handleMessage((String) message.getHeaders().get("mqtt_receivedTopic"),
						(Boolean) message.getHeaders().get("mqtt_duplicate"),
						(Integer) message.getHeaders().get("mqtt_receivedQos"), (String) message.getPayload());
			}
		};
	}

	// Handle Lora normalized messages, independent if the type of gateway.
	@Bean
	public MessageChannel mqttLoraChannel() {
		return new PublishSubscribeChannel();
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttLoraChannel")
	public MessageHandler mqttIngestorLora(LoraIngestor ingestor) {
		return new MessageHandler() {

			@Override
			public void handleMessage(Message<?> message) throws MessagingException {
				ingestor.handleLoraMessage((JsonObject)message.getPayload());
			}
		};
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
