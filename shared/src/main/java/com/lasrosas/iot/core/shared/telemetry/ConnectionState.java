package com.lasrosas.iot.core.shared.telemetry;

import java.util.Optional;

public class ConnectionState extends Telemetry {

	private final Optional<Boolean> connected;

	public static ConnectionState connected() {
		return new ConnectionState(true);
	}

	public static ConnectionState disconnected() {
		return new ConnectionState(false);
	}

	public ConnectionState(boolean connected) {
		this(Optional.of(connected));
	}

	public ConnectionState(Optional<Boolean> connected) {
		this.connected = connected;
	}

	public Optional<Boolean> getConnected() {
		return connected;
	}
}
