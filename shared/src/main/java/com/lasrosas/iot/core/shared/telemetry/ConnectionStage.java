package com.lasrosas.iot.core.shared.telemetry;

public class ConnectionStage extends Telemetry {
	public static final int JOINING = 1;
	public static final int JOINED= 2;

	private final int stage;

	public static ConnectionStage joining() {
		return new ConnectionStage(JOINING);
	}

	public static ConnectionStage joined() {
		return new ConnectionStage(JOINED);
	}

	public ConnectionStage(int stage) {
		this.stage = stage;
	}

	public int getStage() {
		return stage;
	}
	
}
