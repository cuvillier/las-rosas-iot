package com.lasrosas.iot.services.lora;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lasrosas.iot.services.lora.server.LoraServer;
import com.lasrosas.iot.services.lora.server.LoraServerRAK4279;
import com.lasrosas.iot.services.mqtt.MqttReader;

@Configuration
@EnableConfigurationProperties(LoraIngestorConfig.class)
public class LoraIngestorConfig {

	@Bean
	@ConfigurationProperties(prefix = "RAK7249Ingestor")
	public LoraIngestor RAK7249Ingestor() {
		return new LoraIngestor(mqttRAK4279(), loraServerRAK4972(), "lora/+/+");
	}

	@Bean
	@ConfigurationProperties(prefix = "loraServerRAK4972")
	public LoraServer loraServerRAK4972() {
		return new LoraServerRAK4279();
	}

	@Bean
	@ConfigurationProperties(prefix = "mqttRAK4279")
	public MqttReader mqttRAK4279() {
		return new MqttReader("mqttRAK4279");
	}
}
