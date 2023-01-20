package com.lasrosas.iot.core.database.entities.thg;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ThingTest {

	@Test
	public void needoDisconnect() {
		var thing = new ThingLora();
		Assertions.assertFalse(thing.needToDisconnect());

		thing.setConnectionTimeout(60);
		Assertions.assertFalse(thing.needToDisconnect());

		var proxy = new ThingProxy();
		thing.setProxy(proxy);
		proxy.setConnected(0);
		Assertions.assertFalse(thing.needToDisconnect());

		proxy.setConnected(1);
		Assertions.assertTrue(thing.needToDisconnect());
		proxy.setConnected(0);

		proxy.setLastSeen(LocalDateTime.now());
		Assertions.assertFalse(thing.needToDisconnect());
		proxy.setConnected(1);
		Assertions.assertFalse(thing.needToDisconnect());

		proxy.setLastSeen(LocalDateTime.now().minus(1, ChronoUnit.HOURS));
		proxy.setConnected(0);
		Assertions.assertFalse(thing.needToDisconnect());
		proxy.setConnected(1);
		Assertions.assertTrue(thing.needToDisconnect());
	}
}
