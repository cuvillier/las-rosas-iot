package com.lasrosas.iot.core.shared.telemetry;

public class MultiSwitchValue extends Telemetry {
	private int connected = 0;
	private int state = 0;
	private int expectedState = 0;

	public MultiSwitchValue() {
	}

	public MultiSwitchValue(int state, int expectedState, boolean connected) {
		this.state = state;
		this.expectedState = expectedState;
		this.connected = connected?1:0;
	}

	public int getConnected() {
		return connected;
	}

	public void setConnected(int connected) {
		this.connected = connected;
	}

	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getExpectedState() {
		return expectedState;
	}
	public void setExpectedState(int expectedState) {
		this.expectedState = expectedState;
	}
}
