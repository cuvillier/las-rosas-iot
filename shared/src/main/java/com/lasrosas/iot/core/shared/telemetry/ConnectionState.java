package com.lasrosas.iot.core.shared.telemetry;

public class ConnectionState extends SwitchTelemetry {
	private final boolean connected;

	public static ConnectionState connected() {
		return new ConnectionState(true);
	}

	public static ConnectionState disconnected() {
		return new ConnectionState(false);
	}

	public ConnectionState(boolean connected) {
		this.connected = connected;
	}

	public boolean isConnected() {
		return connected;
	}
}
