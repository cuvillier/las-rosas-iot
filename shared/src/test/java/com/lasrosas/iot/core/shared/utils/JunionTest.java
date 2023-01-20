package com.lasrosas.iot.core.shared.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JunionTest {

	@Test
	public void junion() {
		var junion = new Junion(1, "test");
		var value = junion.getValueAs("test", Integer.class).get();
		Assertions.assertEquals(1, value);

		var mayBeValue1 = junion.getValueAs("xxxx", Integer.class);
		Assertions.assertTrue(mayBeValue1.isEmpty());
		
		var mayBeValue2 = junion.getValueAs("test", String.class);
		Assertions.assertTrue(mayBeValue2.isEmpty());
	}

}
