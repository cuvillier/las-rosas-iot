package com.lasrosas.iot.core.shared.telemetry;

import java.util.Optional;

public class ConnectionState extends Telemetry {
	public static final int CONNECTED = 1;
	public static final int DISCONNECTED = 0;

	private final Integer connected;

	public static ConnectionState connected() {
		return new ConnectionState(CONNECTED);
	}

	public static ConnectionState disconnected() {
		return new ConnectionState(DISCONNECTED);
	}

	public ConnectionState(int connected) {
		this.connected = connected;
	}

	public Optional<Integer> getConnected() {
		return connected == null? Optional.empty(): Optional.of(connected);
	}
}
