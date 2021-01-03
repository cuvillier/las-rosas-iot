package com.lasrosas.iot.database.entities.alrm;

import com.lasrosas.iot.shared.rules.DataChangeMatcher;
import com.lasrosas.iot.shared.rules.DataChangeRule;
import com.lasrosas.iot.shared.rules.RuleAction;

public class AlarmRule extends DataChangeRule {

	public AlarmRule(RuleAction action, DataChangeMatcher ... matchers) {
		super(action, matchers);
	}

}
