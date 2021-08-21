package com.lasrosas.iot.ingestor.services.lora.api;

import java.time.LocalDateTime;

import com.lasrosas.iot.shared.telemetry.NotPartOfTelemetry;

public abstract class LoraMessage {

	@NotPartOfTelemetry
	private LocalDateTime time;

	@NotPartOfTelemetry
	private String deveui;

	public LocalDateTime getTime() {
		return time;
	}
	public void setTime(LocalDateTime time) {
		this.time = time;
	}
	public String getDeveui() {
		return deveui;
	}
	public void setDeveui(String deveui) {
		this.deveui = deveui;
	}
}

