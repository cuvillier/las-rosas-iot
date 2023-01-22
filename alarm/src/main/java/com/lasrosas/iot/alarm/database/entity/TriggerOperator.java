package com.lasrosas.iot.alarm.database.entity;

public enum TriggerOperator {
	SUPERIOR(">"),
	INFERIOR("<"),
	EQUALS("="),
	DIFFERENT("!=");

	private final String readable;

	TriggerOperator(String readable) {
		this.readable = readable;
	}

	public String getReadable() {
		return readable;
	}

}
