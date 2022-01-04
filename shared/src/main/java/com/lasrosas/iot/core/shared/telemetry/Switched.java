package com.lasrosas.iot.core.shared.telemetry;

public class Switched extends Telemetry {
	public static final String SCHEMA = "SwitchedOnOff";

	public static final int OFF = 0;
	private final int state;

	public Switched() {
		this(OFF);
	}

	public Switched(int state) {
		super();
		this.state = state;
	}


	public int getState() {
		return state;
	}
}
