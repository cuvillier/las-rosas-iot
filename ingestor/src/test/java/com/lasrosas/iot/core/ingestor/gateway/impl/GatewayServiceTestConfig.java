package com.lasrosas.iot.core.ingestor.gateway.impl;

import org.springframework.context.annotation.Bean;

import com.lasrosas.iot.core.ingestor.gateway.api.GatewayService;
import com.lasrosas.iot.core.ingestor.gateway.impl.rak7249.api.Rak7249Driver;
import com.lasrosas.iot.core.ingestor.gateway.impl.rak7249.impl.Rak7249DriverImpl;

public class GatewayServiceTestConfig {

	@Bean
	public Rak7249Driver rak7249Driver() {
		return new Rak7249DriverImpl();
	}

	@Bean
	public GatewayService gatewayService(Rak7249Driver rak7249) {
		return new GatewayServiceImpl(rak7249);
	}

}
