package com.lasrosas.iot.core.shared.telemetry;

public class Switched extends Telemetry {
	public static final String SCHEMA = "SwitchedOnOff";

	public static enum State {
		OFF, ON
	}

	private final State state;

	public static Switched on() {
		return new Switched(State.ON);
	}

	public static Switched off() {
		return new Switched(State.OFF);
	}

	public Switched() {
		this(State.OFF);
	}

	public Switched(State state) {
		this.state = state;
	}


	public State getState() {
		return state;
	}
}
