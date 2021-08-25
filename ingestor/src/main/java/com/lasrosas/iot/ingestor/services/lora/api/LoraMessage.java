package com.lasrosas.iot.ingestor.services.lora.api;

import com.lasrosas.iot.shared.telemetry.NotPartOfState;

public abstract class LoraMessage {

	@NotPartOfState
	private String deveui;

	public String getDeveui() {
		return deveui;
	}
	public void setDeveui(String deveui) {
		this.deveui = deveui;
	}
}

