package com.lasrosas.iot.core.shared.telemetry;

public class ConnectionState extends StateMessage {

	public static final int CAUSE_NTW_JOIN = 1;
	public static final int CAUSE_NTW_TIMEOUT = 2;
	public static final int CAUSE_ALIVE = 3;

	private int connected;
	private int cause;

	public ConnectionState(int connected, int cause) {
		super();
		this.connected = connected;
		this.cause = cause;
	}

	public int getConnected() {
		return connected;
	}
	public void setConnected(int connected) {
		this.connected = connected;
	}
	public int getCause() {
		return cause;
	}
	public void setCause(int cause) {
		this.cause = cause;
	}

	public boolean isConnected() {
		return connected == 1;
	}
}
