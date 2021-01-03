package com.lasrosas.iot.shared.rules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataChangeRule {
	private static final Logger log = LoggerFactory.getLogger(DataChangeRule.class);

	private DataChangeMatcher [] matchers;
	private RuleAction action;

	public DataChangeRule(RuleAction action, DataChangeMatcher ... matchers ) {
		this.matchers = matchers;
		this.action = action;
	}

	public DataChangeMatcher[] getMatchers() {
		return matchers;
	}

	public RuleAction getAction() {
		return action;
	}

	public void evaluate(DataChange dataChange) {

		for(var matcher: matchers) {

			if( matcher.match(dataChange) ) {

				try {
					action.execute(dataChange);
				} catch(Exception e) {
					log.error("DataRule evaluation failed", e);
				}

				return;
			}
		}
	}
}
