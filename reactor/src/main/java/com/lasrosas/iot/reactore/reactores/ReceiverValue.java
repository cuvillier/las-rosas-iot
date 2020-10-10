package com.lasrosas.iot.reactore.reactores;

import java.time.LocalDateTime;

import com.google.gson.JsonObject;

public class ReceiverValue {
	private Receiver receiver;
	private LocalDateTime time;
	private JsonObject value;

	public ReceiverValue(Receiver receiver, JsonObject value, LocalDateTime time) {
		super();
		this.receiver = receiver;
		this.value = value;
		this.time = time;
	}
	public LocalDateTime getTime() {
		return time;
	}
	public void setTime(LocalDateTime time) {
		this.time = time;
	}
	public void setReceiver(Receiver receiver) {
		this.receiver = receiver;
	}
	public void setValue(JsonObject value) {
		this.value = value;
	}
	public Receiver getReceiver() {
		return receiver;
	}
	public JsonObject getValue() {
		return value;
	}
	
}
