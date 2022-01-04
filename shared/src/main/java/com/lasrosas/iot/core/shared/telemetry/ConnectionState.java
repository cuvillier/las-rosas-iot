package com.lasrosas.iot.core.shared.telemetry;

public class ConnectionState extends StateMessage {

	public static final int CAUSE_NTW_JOIN = 1;
	public static final int CAUSE_NTW_TIMEOUT = 2;
	public static final int CAUSE_ALIVE = 3;

	private int remind = 0;
	private int connected;
	private int cause;

	public ConnectionState(int connected, int cause) {
		this(connected, cause, 0);
	}

	public ConnectionState(int connected, int cause, int remind) {
		this.connected = connected;
		this.cause = cause;
		this.remind = remind;
	}

	public int getRemind() {
		return remind;
	}

	public void setRemind(int remind) {
		this.remind = remind;
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
