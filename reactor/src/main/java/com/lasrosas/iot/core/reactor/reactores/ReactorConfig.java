package com.lasrosas.iot.core.reactor.reactores;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReactorConfig {

	@Bean
	public WaterTankReactor WaterTankReactor() {
		return new WaterTankReactor();
	}
}
