package com.lasrosas.iot.ingestor.parser.impl.elsys;

import java.util.Base64;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lasrosas.iot.ingestor.services.sensors.impl.elsys.ElsysGenericFrame;
import com.lasrosas.iot.ingestor.services.sensors.impl.elsys.ElsysGenericParser;

public class ElsysErsParserTest {
	private ElsysGenericParser parser = new ElsysGenericParser();
	private Gson gson = new GsonBuilder().setPrettyPrinting().create();

	@Test
	public void test() {
		String encoded = "AQDmAj8EAAoFAAcOEw";

		var data = Base64.getDecoder().decode(encoded);
		var result = parser.decodeUplink(data);
		
		var message = (ElsysGenericFrame)result;

		var json = gson.toJson(message);
		
		System.out.println(json);
	}
}
