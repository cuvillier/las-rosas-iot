package com.lasrosas.iot.core.ingestor.parser.impl.elsys;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.support.MessageBuilder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lasrosas.iot.core.ingestor.sensors.api.ThingEncodedMessage;
import com.lasrosas.iot.core.ingestor.sensors.impl.elsys.ElsysGenericParser;

public class ElsysErsParserTest {
	public static final Logger log = LoggerFactory.getLogger(ElsysErsParserTest.class);
	private ElsysGenericParser parser = new ElsysGenericParser();
	private Gson gson = new GsonBuilder().setPrettyPrinting().create();

	@Test
	public void test() {
		var message = new ThingEncodedMessage("AQDmAj8EAAoFAAcOEw", "base64");
		var imessage = MessageBuilder.withPayload(message).build();
		var result = parser.decodeUplink(imessage);
		var json = gson.toJson(result);
		log.info(json);
	}
}
