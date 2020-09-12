package com.lasrosas.iot.services.lora;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lasrosas.iot.services.lora.sensors.LoraSensors;


@Configuration
public class LoraIngestorConfig {
	
	@Bean
	@ConfigurationProperties(prefix = "lora-ingestor")
	public LoraIngestor LoraIngestor(LoraServerRAK7249 rak7249, LoraSensors sensors) {
		return new LoraIngestor(rak7249, sensors);
	}

	@Bean LoraServerRAK7249 LoraServerRAK7249() {
		return new LoraServerRAK7249();
	}

	@Bean
	@ConfigurationProperties(prefix = "lora-sensors")
	public LoraSensors LoraSensors() {
		return new LoraSensors();
	}

	@Bean
	@ConfigurationProperties(prefix = "rak7249")
	public LoraServer loraServerRAK4972() {
		return new LoraServerRAK7249();
	}
}
