package com.lasrosas.iot.mqtt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "mqtt")
@Validated
public class IOTMqttConfig {

	@ConfigurationProperties(prefix="mqtt")
	@Bean
	public MqttSession MqttSession() {
		return new MqttSession();
	}
}
