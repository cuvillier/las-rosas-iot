package com.lasrosas.iot.ingestor.services.lora.api;

import java.time.LocalDateTime;

public abstract class LoraMessage {
	private LocalDateTime time;
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

