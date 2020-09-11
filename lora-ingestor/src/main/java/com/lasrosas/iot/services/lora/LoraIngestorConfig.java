package com.lasrosas.iot.services.lora;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lasrosas.iot.services.db.repo.GatewayRepo;
import com.lasrosas.iot.services.db.repo.ThingRepo;
import com.lasrosas.iot.services.db.repo.TimeSerieRepo;
import com.lasrosas.iot.services.lora.sensors.LoraSensors;

@Configuration
@EnableConfigurationProperties(LoraIngestorConfig.class)
public class LoraIngestorConfig {

	@Bean
	@ConfigurationProperties(prefix = "LoraIngestor")
	public LoraIngestor LoraIngestor(LoraServerRAK7249 rak7249, LoraSensors sensors, ThingRepo thgRepo, TimeSerieRepo tsrRepo, GatewayRepo gtwRepo, Gson gson) {
		return new LoraIngestor(rak7249, sensors, thgRepo, tsrRepo, gtwRepo, gson);
	}

	@Bean
	@ConfigurationProperties(prefix = "loraSensors")
	private LoraSensors LoraSensors() {
		return new LoraSensors();
	}

	@Bean
	public Gson gson() {
		return new GsonBuilder()
				.setPrettyPrinting()
				.create();
	}
	@Bean
	@ConfigurationProperties(prefix = "loraServerRAK7249")
	public LoraServer loraServerRAK4972(Gson gson) {
		return new LoraServerRAK7249(gson);
	}
}
