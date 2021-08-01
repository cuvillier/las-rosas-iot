package com.lasrosas.iot.reactor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;

import com.lasrosas.iot.reactore.reactor.WaterTankReactor;

@Configuration
@IntegrationComponentScan
@EnableIntegration
public class ReactorConfig {

	@Bean
	public WaterTankReactor WaterTankReactor() {
		return new WaterTankReactor();
	}
}
