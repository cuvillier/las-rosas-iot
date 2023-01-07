package com.lasrosas.iot.core.shared.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@SpringBootTest
@ContextConfiguration(classes=UtilsConfig.class)
public class GsonUtilsTest {
	private static final Logger log = LoggerFactory.getLogger(GsonUtilsTest.class);

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

	public static class WithLocalDateTime {
		private LocalDateTime time = LocalDateTime.now();
		private LocalDate date = LocalDate.now();

		public LocalDateTime getTime() {
			return time;
		}

		public void setTime(LocalDateTime time) {
			this.time = time;
		}
	}
	
	@Autowired
	private Gson gson;

	@Test
	public void convertDateTimeThrowException() {
		var o = new WithLocalDateTime();
		var json = gson.toJson(o);
		gson.fromJson(json, WithLocalDateTime.class);
	}
}
