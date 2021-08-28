package com.lasrosas.iot.core.reactor.base;

import java.time.LocalDateTime;

import com.lasrosas.iot.core.database.entities.dtw.TwinReactorReceiver;

public class ReactorReceiverValue {
	private final TwinReactorReceiver receiver;
	private final Object value;
	private final LocalDateTime time = LocalDateTime.now();	// Get time later

	public ReactorReceiverValue(TwinReactorReceiver receiver, Object value) {
		this.receiver = receiver;
		this.value = value;
	}

	public LocalDateTime getTime() {
		return time;
	}
	public TwinReactorReceiver getReceiver() {
		return receiver;
	}
	public Object getValue() {
		return value;
	}
}
