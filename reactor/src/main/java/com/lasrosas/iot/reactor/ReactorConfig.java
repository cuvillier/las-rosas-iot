package com.lasrosas.iot.reactor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lasrosas.iot.database.entities.tsr.TimeSeriePoint;
import com.lasrosas.iot.reactore.reactores.TwinReactors;
import com.lasrosas.iot.shared.utils.LocalTopic;

@Configuration
public class ReactorConfig {
	
	@Bean
	public Reactor Reactor(TwinReactors twinReactors) {
		return new Reactor(twinReactors);
	}

	@Bean
	public TwinReactors TwinReactors() {
		return new TwinReactors();
	}

	@Bean(name="newPointTopic")
	public LocalTopic<TimeSeriePoint> newPointTopic() {
		return new LocalTopic<TimeSeriePoint>();
	}
}
