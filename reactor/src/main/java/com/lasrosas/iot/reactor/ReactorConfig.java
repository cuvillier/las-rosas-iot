package com.lasrosas.iot.reactor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lasrosas.iot.database.entities.tsr.TimeSeriePoint;
import com.lasrosas.iot.influxdb.InfluxdbSession;
import com.lasrosas.iot.mqtt.MqttSession;
import com.lasrosas.iot.reactore.reactores.TwinReactors;
import com.lasrosas.iot.reactore.reactores.WaterTankReactor;
import com.lasrosas.iot.shared.utils.LocalTopic;

@Configuration
public class ReactorConfig {

	@Bean
	public Reactor Reactor(TwinReactors twinReactors, InfluxdbSession influxdb, MqttSession mqtt) {
		return new Reactor(twinReactors, influxdb, mqtt);
	}

	@Bean
	public TwinReactors TwinReactors() {
		return new TwinReactors();
	}

	@Bean(name="newPointTopic")
	public LocalTopic<TimeSeriePoint> newPointTopic() {
		return new LocalTopic<TimeSeriePoint>();
	}

	@Bean(name="Cortijo-WaterTank")
	public WaterTankReactor WaterTankReactor() {
		return new WaterTankReactor();
	}
}
