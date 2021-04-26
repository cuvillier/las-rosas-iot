package com.lasrosas.iot.shared.utils.diffuse;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class DiffuseTest {

	@Test
	public void diffuse() {
		var result = new ArrayList<DiffuseEvent>();

		Diffuse diffuse = new Diffuse();
		diffuse.subscribe(new DiffuseMatcher("", "", String.class), (e) -> result.add(e));
		diffuse.subscribe(new DiffuseMatcher("", "", Integer.class), (e) -> result.add(e));

		var event = new DiffuseEvent(1L, "T", 1L, "Hello");
		diffuse.publish(event);

		assertEquals(1, result.size());
		assertEquals(event, result.get(0));
	}
	
}
