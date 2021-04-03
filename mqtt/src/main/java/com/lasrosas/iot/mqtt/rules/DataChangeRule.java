package com.lasrosas.iot.mqtt.rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DataChangeRule<T> {
	private static final Logger log = LoggerFactory.getLogger(DataChangeRule.class);

	private DataChangeMatcher [] matchers;
	private DataChangeAction<T> action;

	public DataChangeRule(String entityClass, DataChangeAction<T> action, DataChangeMatcher ... matchers ) {
		this.matchers = matchers;
		this.action = action;
	}

	public DataChangeMatcher[] getMatchers() {
		return matchers;
	}

	public DataChangeAction<T> getAction() {
		return action;
	}

	public List<RuleResult<T>> evaluate(T entity, DataChange dataChange) {
		for(var matcher: matchers) {

			if( matcher.match(dataChange) ) {

				try {
					return action.execute(entity, dataChange);
				} catch(Exception e) {
					log.error("DataRule evaluation failed", e);
				}
			}
		}

		return Collections.emptyList();
	}
}
