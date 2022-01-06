package com.lasrosas.iot.core.shared.telemetry;

public class MultiSwitchValue extends Telemetry {
	private boolean boot = false;
	private int connected = 0;
	private int state = 0;
	private int excpectedState = 0;

	public MultiSwitchValue() {
	}

	public MultiSwitchValue(int state, int excpectedState, boolean connected, boolean boot) {
		this.state = state;
		this.excpectedState = excpectedState;
		this.connected = connected?1:0;
		this.boot = boot;
	}

	public boolean isBoot() {
		return boot;
	}

	public void setBoot(boolean boot) {
		this.boot = boot;
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
	public int getExcpectedState() {
		return excpectedState;
	}
	public void setExcpectedState(int excpectedState) {
		this.excpectedState = excpectedState;
	}
}
