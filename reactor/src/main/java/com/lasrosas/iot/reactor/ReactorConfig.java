package com.lasrosas.iot.reactor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lasrosas.iot.influxdb.InfluxdbSession;
import com.lasrosas.iot.mqtt.session.MqttSession;
import com.lasrosas.iot.reactore.reactor.WaterTankReactor;

@Configuration
public class ReactorConfig {

	@Bean
	public ReactorEngine Reactor(InfluxdbSession influxdb, MqttSession mqtt) {
		return new ReactorEngine(influxdb, mqtt);
	}

	@Bean
	public WaterTankReactor WaterTankReactor() {
		return new WaterTankReactor();
	}
}
