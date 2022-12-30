package com.lasrosas.iot.core.ingestor.lora.api;

import com.lasrosas.iot.core.shared.telemetry.NotPartOfState;

public abstract class LoraMessage {

	@NotPartOfState
	private String deveui;

	public String getDeveui() {
		return deveui;
	}
	public void setDeveui(String deveui) {
		this.deveui = deveui;
	}
	public LoraMessage() {
		super();
	}
	public LoraMessage(String deveui) {
		super();
		this.deveui = deveui;
	}

}

