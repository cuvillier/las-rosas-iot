package com.lasrosas.iot.shared.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;

public class GsonUtilsTest {

	@Test
	public void mergeEmpty() {
		var json1 = new JsonObject();
		var json2 = new JsonObject();

		var changes = GsonUtils.mergeJsonObjects(json1, json2, null);
		
		assertEquals(0, changes);
		assertEquals(0, json2.entrySet().size());
	}

	@Test
	public void mergeNew() {
		var json1 = new JsonObject();
		var json2 = new JsonObject();
		
		json1.addProperty("int", 1);
		json1.addProperty("string", "s");

		var changes = GsonUtils.mergeJsonObjects(json1, json2, null);
		assertEquals(2, changes);
		
		assertEquals(2, json2.entrySet().size());
		assertEquals(1, json2.get("int").getAsInt());
		assertEquals("s", json2.get("string").getAsString());
		
		
		assertEquals(2, json2.entrySet().size());
	}

	@Test
	public void mergeUpdate() {
		var json1 = new JsonObject();
		var json2 = new JsonObject();

		json1.addProperty("int", 1);
		json1.addProperty("string", "s");

		json2.addProperty("int", 1212);
		json2.addProperty("string", "sasdsd");

		var changes = GsonUtils.mergeJsonObjects(json1, json2, null);

		assertEquals(2, changes);

		assertEquals(2, json2.entrySet().size());
		assertEquals(1, json2.get("int").getAsInt());
		assertEquals("s", json2.get("string").getAsString());

		assertEquals(2, json2.entrySet().size());
	}

	@Test
	public void mergeSubjson() {
		var json1 = new JsonObject();
		var json2 = new JsonObject();
		
		json1.addProperty("int", 1);
		json1.addProperty("string", "s");

		var sub = new JsonObject();
		sub.addProperty("name", "sub");

		json1.add("sub", sub);

		json2.addProperty("int", 1212);
		json2.addProperty("string", "sasdsd");

		var changes = GsonUtils.mergeJsonObjects(json1, json2, null);
		assertEquals(3, changes);
		
		assertEquals(3, json2.entrySet().size());
		assertEquals(1, json2.get("int").getAsInt());
		assertEquals("s", json2.get("string").getAsString());
		assertNotNull(json2.get("sub"));
		assertEquals("sub", json2.get("sub").getAsJsonObject().get("name").getAsString());
	}
}
