package com.lasrosas.iot.core.shared.telemetry;

public class MultiSwitchOrder extends Order {
	private final int state;
	private final String part;

	public MultiSwitchOrder(int state) {
		this(state, null);
	}

	public MultiSwitchOrder(int state, String part) {
		this.state = state;
		this.part = part;
	}

	public int getState() {
		return state;
	}

	public String getPart() {
		return part;
	}
}
