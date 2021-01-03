package com.lasrosas.iot.shared.rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataChangeRuleManager {
	private Map<String, List<DataChangeRule>> rules = new HashMap<String, List<DataChangeRule>>();

	public void registerRule(DataChangeRule rule) {
		synchronized(rules) {
			for(var matcher: rule.getMatchers()) {
				var rulesForType = rules.get(matcher.getEntityType());
				if(rulesForType == null) {
					rulesForType = new ArrayList<DataChangeRule>();
					rules.put(matcher.getEntityType(), rulesForType);
				}

				rulesForType.add(rule);
			}
		}
	}

	public void unregisterRule(DataChangeRule rule) {
		synchronized(rules) {
			for(var matcher: rule.getMatchers()) {
				var rulesForType = rules.get(matcher.getEntityType());
				rulesForType.remove(rule);
			}
		}
	}

	public void evaluate(DataChange dataChange) {
		synchronized(rules) {
			for(var rule: rules.get(dataChange.getEntityType()) ) {
				rule.evaluate(dataChange);
			}
		}
	}
}
