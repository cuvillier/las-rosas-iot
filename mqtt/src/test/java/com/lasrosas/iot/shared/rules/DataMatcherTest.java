package com.lasrosas.iot.shared.rules;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.lasrosas.iot.mqtt.rules.DataChangeMatcher;

public class DataMatcherTest {

	@Test
	public void perfectMatch() {
		var matcher = new DataChangeMatcher("Person", 1234L, new String [] {"name"});

		assertTrue(matcher.match("Person", 1234L, "name"));
		assertFalse(matcher.match("Personx", 1234L, "name"));
		assertFalse(matcher.match("Person", 1234L, "namex"));
	}

	@Test
	public void matchMultipleAttributes() {
		var matcher = new DataChangeMatcher("Person", null, new String[] {"lastname","surname","nomdefamille","apellido"});
		assertTrue(matcher.match("Person", null, "lastname"));
		assertTrue(matcher.match("Person", null, "surname"));
		assertTrue(matcher.match("Person", null, "nomdefamille"));
		assertFalse(matcher.match("Person", null, "badattribute"));
	}

	@Test
	public void matchNull() {
		var matcher = new DataChangeMatcher("Person", null, new String[] {"name"});
		assertTrue(matcher.match("Person", 12345L, "name"));
		assertTrue(matcher.match("Person", null, "name"));
	}
}
