package com.lasrosas.iot.shared.rules;

public class RuleMatcher {
	private final String eventClass;

	public RuleMatcher(String eventClass) {
		this.eventClass = eventClass;
	}

	public String getEventClass() {
		return eventClass;
	}
}
