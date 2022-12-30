package com.lasrosas.iot.core.ingestor.timeSerieWriter.impl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lasrosas.iot.core.ingestor.timeSerieWriter.api.WriteSQL;

@Configuration
public class WriteSQLConfig {

	@Bean
	public WriteSQL writeSQL() {
		return new WriteSQLImpl();
	}
}
