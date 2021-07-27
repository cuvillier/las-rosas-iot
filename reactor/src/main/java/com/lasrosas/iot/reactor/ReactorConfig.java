package com.lasrosas.iot.reactor;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
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
import org.springframework.messaging.handler.annotation.Header;

import com.lasrosas.iot.influxdb.InfluxdbSession;
import com.lasrosas.iot.reactore.reactor.WaterTankReactor;
import com.lasrosas.iot.shared.utils.MqttConfig;

@Configuration
@IntegrationComponentScan
@EnableIntegration
public class ReactorConfig {

	@Bean
	public ReactorEngine Reactor(InfluxdbSession influxdb, MqttOutputGateway mqttOutputGateway, MqttPahoMessageDrivenChannelAdapter inbound) {
		return new ReactorEngine(influxdb, mqttOutputGateway, inbound);
	}

	@Bean
	public WaterTankReactor WaterTankReactor() {
		return new WaterTankReactor();
	}

	@Bean
	@ConfigurationProperties(prefix = "mqtt")
	public MqttConfig mqttConfig() {
		return new MqttConfig();
	}

	@Bean
	public MessageChannel mqttInputChannel() {
		return new DirectChannel();
	}

	@Bean
	public MqttPahoClientFactory mqttClientFactory(MqttConfig config) {
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

	@Bean
	public MqttPahoMessageDrivenChannelAdapter inboundChannelAdapter(MqttConfig config, MqttPahoClientFactory mqttClientFactory) {

		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(config.getClientId(),
				mqttClientFactory, "thing/+/+/+", "digital-twin/+/+/+");

		if( config.getCompletionTimeout() != null) adapter.setCompletionTimeout(config.getCompletionTimeout());
		adapter.setConverter(new DefaultPahoMessageConverter());
		if( config.getQOS() != null) adapter.setQos(config.getQOS());
		adapter.setOutputChannel(mqttInputChannel());

		return adapter;
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttInputChannel")
	public MessageHandler handler(ReactorEngine reactorEngine) {
		return new MessageHandler() {

			@Override
			public void handleMessage(Message<?> message) throws MessagingException {
				reactorEngine.handleMessage((String) message.getHeaders().get("mqtt_receivedTopic"),
						(Boolean) message.getHeaders().get("mqtt_duplicate"),
						(Integer) message.getHeaders().get("mqtt_receivedQos"), (String) message.getPayload());
			}
		};
	}

	// Output channel
	@Bean
	public MessageChannel mqttOutputChannel() {
		return new DirectChannel();
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttOutputChannel")
	public MessageHandler mqttOutbound(MqttPahoClientFactory mqttPahoClientFactory) {
		MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("ReactorOutDev", mqttPahoClientFactory);
		messageHandler.setAsync(false);
		return messageHandler;
	}

	@MessagingGateway(defaultRequestChannel = "mqttOutputChannel")
	public interface MqttOutputGateway {
		void sendToMqtt(String data, @Header(MqttHeaders.TOPIC) String topic);
	}
}
