package com.lasrosas.iot.core.ingestor.parser.impl.dragino;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lasrosas.iot.core.ingestor.sensors.impl.dragino.DraginoLHT65Frame.UplinkTempHumRequest.BatteryStatus;
import com.lasrosas.iot.core.ingestor.sensors.impl.dragino.DraginoLHT65FrameDecoder;

public class DraginoLHT65ParserTest {
	public static final Logger log = LoggerFactory.getLogger(DraginoLHT65ParserTest.class);

	private DraginoLHT65FrameDecoder frameParser = new DraginoLHT65FrameDecoder();
	private Gson gson = new GsonBuilder().setPrettyPrinting().create();

	@Test
	public void parseUplinkTimeSyncRequest() {
		var bytes = new byte[] {(byte)0xCB, (byte)0xF6, 0x0B, 0x0D, 0x03, 0x76, 0x01, 0x0A, (byte)0xDD, 0x7F, (byte)0xFF};
		
		var frame = frameParser.decodeUplink(bytes);		
		log.info(gson.toJson(frame));

		assertEquals(BatteryStatus.Good, frame.getBatteryStatus());
		assertEquals(3.062, frame.getBatteryVoltage());
		assertEquals(88.6, frame.getHumidityINT());
		assertEquals(28.29, frame.getTemperatureINT());
		assertEquals(27.81, frame.getTemperatureEXT());
	}
}
