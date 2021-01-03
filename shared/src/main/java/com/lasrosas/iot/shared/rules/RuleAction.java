package com.lasrosas.iot.shared.rules;

public interface RuleAction {
	void execute(DataChange dataChange);
}
