package com.lasrosas.iot.mqtt.rules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class DataChangeEngine<T> implements Iterable<DataChangeRule<T>> {

	private List<DataChangeRule<T>> rules = new ArrayList<>();

	protected abstract T loadEntity(Long entityId);
	protected abstract String getEntityType(T entity);
	protected abstract void evaluateResult(T entity, RuleResult<T> result);

	public void registerRule(DataChangeRule<T> rule) {
		synchronized(rules) {
			rules.add(rule);
		}
	}

	public void unregisterRule(DataChangeRule<T> rule) {
		synchronized(rules) {
			rules.remove(rule);
		}
	}

	public Iterator<DataChangeRule<T>> iterator() {
		return rules.iterator();
	}

	public void handleDataChange(DataChange dataChange) {
		var entity = loadEntity(dataChange.getEntityId());

		synchronized(rules) {
			for(var rule: rules) {
				var results = rule.evaluate(entity, dataChange);

				for(var result: results)
					evaluateResult(entity, result);
			}
		}
	}
}
