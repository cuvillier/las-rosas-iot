package com.lasrosas.iot.core.shared.telemetry;

public class ThingConnectionState extends Telemetry {
	public static int CAUSE_NTW_CONNECT = 1;
	public static int CAUSE_NTW_TIMEOUT = 2;

	private boolean connected;
	private Integer cause;

	public ThingConnectionState() {
		super();
	}

	public ThingConnectionState(boolean connected, Integer cause) {
		super();
		this.connected = connected;
		this.cause = cause;
	}

	public boolean isConnected() {
		return connected;
	}
	public void setConnected(boolean connected) {
		this.connected = connected;
	}
	public Integer getCause() {
		return cause;
	}
	public void setCause(Integer cause) {
		this.cause = cause;
	}
}
