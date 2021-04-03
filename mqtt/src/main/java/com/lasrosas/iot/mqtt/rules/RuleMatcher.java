package com.lasrosas.iot.mqtt.rules;

public class RuleMatcher {
	private final String eventClass;

	public RuleMatcher(String eventClass) {
		this.eventClass = eventClass;
	}

	public String getEventClass() {
		return eventClass;
	}
}
