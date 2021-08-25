package com.lasrosas.iot.ingestor.services.rak7249.api;

import java.time.LocalDateTime;

import com.lasrosas.iot.shared.telemetry.NotPartOfState;

public abstract class Rak7249Message {

	private LocalDateTime time = LocalDateTime.now();

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}
}
