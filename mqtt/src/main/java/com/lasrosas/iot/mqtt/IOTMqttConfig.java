package com.lasrosas.iot.mqtt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

import com.lasrosas.iot.mqtt.session.MqttSession;

@ConfigurationProperties(prefix = "mqtt")
@Validated
public class IOTMqttConfig {

	@Bean
	public MqttSession MqttSession() {
		return new MqttSession();
	}
}
