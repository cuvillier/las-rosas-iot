package com.lasrosas.iot.reactor;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lasrosas.iot.database.entities.tsr.TimeSeriePoint;
import com.lasrosas.iot.shared.utils.LocalTopic;

@Configuration
public class ReactorConfig {
	
	@Bean
	@ConfigurationProperties(prefix = "lora-ingestor")
	public Reactor Reactor() {
		return new Reactor();
	}

	@Bean(name="newPointTopic")
	public LocalTopic<TimeSeriePoint> newPointTopic() {
		return new LocalTopic<TimeSeriePoint>();
	}
}
