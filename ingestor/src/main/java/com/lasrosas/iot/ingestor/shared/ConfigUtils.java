package com.lasrosas.iot.ingestor.shared;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;

import com.lasrosas.iot.shared.utils.MqttConfig;

public class ConfigUtils {

	public static MqttPahoClientFactory mqttClientFactory(MqttConfig config) {
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
}
