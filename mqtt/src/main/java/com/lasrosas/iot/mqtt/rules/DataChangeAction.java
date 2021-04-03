package com.lasrosas.iot.mqtt.rules;

import java.util.List;

public interface DataChangeAction<T> {
	List<RuleResult<T>> execute(T entity, DataChange dataChange);
}
