package com.lasrosas.iot.reactore.reactor;

import java.time.LocalDateTime;

import com.google.gson.JsonObject;
import com.lasrosas.iot.database.entities.dtw.TwinReactorReceiver;

public class ReactorReceiverValue {
	private final TwinReactorReceiver receiver;
	private final JsonObject value;
	private final LocalDateTime time = LocalDateTime.now();	// Get time later

	public ReactorReceiverValue(TwinReactorReceiver receiver, JsonObject value) {
		this.receiver = receiver;
		this.value = value;
	}

	public LocalDateTime getTime() {
		return time;
	}
	public TwinReactorReceiver getReceiver() {
		return receiver;
	}
	public JsonObject getValue() {
		return value;
	}
}
