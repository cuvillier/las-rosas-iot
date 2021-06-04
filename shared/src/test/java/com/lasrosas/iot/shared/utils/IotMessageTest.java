package com.lasrosas.iot.shared.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.google.gson.GsonBuilder;
import com.lasrosas.iot.shared.ontology.IotMessage;
import com.lasrosas.iot.shared.ontology.IotMessage.Point;

public class IotMessageTest {

	public static class PointValue {
		private String name;
		private int ivalue;
		private long lvalue;
		private double dvalue;
		private boolean bvalue;
		private List<String> strings;
		public PointValue(String name, int ivalue, long lvalue, double dvalue, boolean bvalue, String ...strings ) {
			super();
			this.name = name;
			this.ivalue = ivalue;
			this.lvalue = lvalue;
			this.dvalue = dvalue;
			this.bvalue = bvalue;
			this.strings = List.of(strings);
		}
		public String getName() {
			return name;
		}
		public int getIvalue() {
			return ivalue;
		}
		public long getLvalue() {
			return lvalue;
		}
		public double getDvalue() {
			return dvalue;
		}
		public boolean isBvalue() {
			return bvalue;
		}
		public List<String> getStrings() {
			return strings;
		}
	}

	@Test
	public void serialize() {

		var gson = new GsonBuilder().create();
		var value = new PointValue("tibo", 1, Long.MAX_VALUE, Double.MAX_VALUE, true, new String[] {"beau", "intelligent", "symptique"});
		var jsonValue = gson.toJson(value);
		var cut = new IotMessage(123, LocalDateTime.now());
		cut.getPoints().add(new Point(567L, "PointValue", jsonValue));

		var json = gson.toJson(cut);
		System.out.println(json);

		var message = gson.fromJson(json, IotMessage.class);
		var json2 = gson.toJson(message);
		assertEquals(json, json2);
	}
}
