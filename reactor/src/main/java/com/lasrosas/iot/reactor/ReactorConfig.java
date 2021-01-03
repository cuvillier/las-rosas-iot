package com.lasrosas.iot.reactor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lasrosas.iot.influxdb.InfluxdbSession;
import com.lasrosas.iot.mqtt.MqttSession;
import com.lasrosas.iot.reactore.reactores.TwinReactors;
import com.lasrosas.iot.reactore.reactores.WaterTankReactor;

@Configuration
public class ReactorConfig {

	@Bean
	public ReactorEngine Reactor(TwinReactors twinReactors, InfluxdbSession influxdb, MqttSession mqtt) {
		return new ReactorEngine(twinReactors, influxdb, mqtt);
	}

	@Bean
	public TwinReactors TwinReactors() {
		return new TwinReactors();
	}

	@Bean(name="FincaLasRosas-WaterTank")
	public WaterTankReactor WaterTankReactor() {
		return new WaterTankReactor();
	}
}
