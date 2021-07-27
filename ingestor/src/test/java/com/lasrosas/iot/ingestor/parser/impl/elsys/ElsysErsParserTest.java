package com.lasrosas.iot.ingestor.parser.impl.elsys;

import static org.junit.jupiter.api.Assertions.*;

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
		var result = parser.decode(data);
		
		assertEquals(1, result.size());
		var holder = result.get(0);

		var message = (ElsysGenericFrame)holder.getMessage();

		var json = gson.toJson(message);
		
		System.out.println(json);
	}
}
