package com.lasrosas.iot.reactore.reactores;

import java.time.LocalDateTime;

import com.google.gson.JsonObject;

public class TransmitterValue {
	private Transmitter transmitter;
	private LocalDateTime time;
	private JsonObject value;
	
	public TransmitterValue(Transmitter transmitter, LocalDateTime time, JsonObject value) {
		super();
		this.transmitter = transmitter;
		this.value = value;
		this.time = time;
	}
	public LocalDateTime getTime() {
		return time;
	}
	public void setTime(LocalDateTime time) {
		this.time = time;
	}
	public void setTransmitter(Transmitter transmitter) {
		this.transmitter = transmitter;
	}
	public void setValue(JsonObject value) {
		this.value = value;
	}
	public Transmitter getTransmitter() {
		return transmitter;
	}
	public JsonObject getValue() {
		return value;
	}

}
