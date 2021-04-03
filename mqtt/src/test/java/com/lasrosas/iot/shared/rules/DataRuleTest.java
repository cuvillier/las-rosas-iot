package com.lasrosas.iot.shared.rules;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.lasrosas.iot.mqtt.rules.DataChange;
import com.lasrosas.iot.mqtt.rules.DataChangeMatcher;
import com.lasrosas.iot.mqtt.rules.DataChangeRule;

public class DataRuleTest {

	@Test
	public void test() {
		final boolean result [] = new boolean[1];
		var rule = new DataChangeRule(
				(dataChange) -> { result[0] = true;},
				new DataChangeMatcher("Person", null, "firstname"));

		result[0] = false;
		rule.evaluate(new DataChange("Person", 1234L, LocalDateTime.now(), new DataChange.NewValue("firstname", "joe", "bob")));
		assertTrue(result[0]);

		result[0] = false;
		rule.evaluate(new DataChange("Person", 1234L, LocalDateTime.now(), new DataChange.NewValue("notglop", "qweqwe", "poipoi")));
		assertFalse(result[0]);
	}
}
