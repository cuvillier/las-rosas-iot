package com.lasrosas.iot.shared.rules;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class DataRuleTest {

	@Test
	public void test() {
		final boolean result [] = new boolean[1];
		var rule = new DataChangeRule(
				(dataChange) -> { result[0] = true;},
				new DataChangeMatcher("Person", null, "firstname"));

		result[0] = false;
		rule.evaluate(new DataChange("Person", 1234L, new DataChange.NewValue(LocalDateTime.now(), "firstname", "joe", "bob")));
		assertTrue(result[0]);

		result[0] = false;
		rule.evaluate(new DataChange("Person", 1234L, new DataChange.NewValue(LocalDateTime.now(), "notglop", "qweqwe", "poipoi")));
		assertFalse(result[0]);
	}
}
