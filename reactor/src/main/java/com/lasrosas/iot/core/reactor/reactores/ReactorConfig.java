package com.lasrosas.iot.core.reactor.reactores;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReactorConfig {

	@Bean
	public WaterTankReactor WaterTankReactor() {
		return new WaterTankReactor();
	}

	@Bean
	public ForwardReactor ForwardReactor() {
		return new ForwardReactor();
	}

	@Bean
	public FridgeReactor FridgeReactor() {
		return new FridgeReactor();
	}

	@Bean
	public MultiSwitchReactor MultiSwitchReactor() {
		return new MultiSwitchReactor();
	}
}
