package com.lasrosas.iot.alarm.api;

import org.springframework.context.annotation.Bean;

import com.lasrosas.iot.alarm.impl.AlarmServiceImpl;

public class AlarmServiceConfig {

	@Bean
	public AlarmService AlarmService() {
		return new AlarmServiceImpl();
	}
}
