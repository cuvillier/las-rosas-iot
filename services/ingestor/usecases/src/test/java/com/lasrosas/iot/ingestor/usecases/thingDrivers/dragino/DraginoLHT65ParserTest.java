package com.lasrosas.iot.ingestor.usecases.thingDrivers.dragino;

import com.lasrosas.iot.ingestor.shared.JsonUtils;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.LorawanMessageUplinkRx;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.dragino.DraginoLHT65Frame;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.dragino.DraginoLHT65FrameDecoder;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DraginoLHT65ParserTest {
	public static final Logger log = LoggerFactory.getLogger(DraginoLHT65ParserTest.class);

	private DraginoLHT65FrameDecoder frameParser = new DraginoLHT65FrameDecoder();

	@Test
	public void parseUplinkTimeSyncRequest() {
		var bytes = new byte[] {(byte)0xCB, (byte)0xF6, 0x0B, 0x0D, 0x03, 0x76, 0x01, 0x0A, (byte)0xDD, 0x7F, (byte)0xFF};
		var encoded = Base64.getEncoder().encodeToString(bytes);
		var message = LorawanMessageUplinkRx.builder()
				.data(encoded)
				.dataEncode(LorawanMessageUplinkRx.BASE64)
				.build();

		var frame = frameParser.decodeUplink(message);
		log.info(JsonUtils.toJson(frame));

		assertEquals(DraginoLHT65Frame.UplinkTempHumRequest.BatteryStatus.Good, frame.getBatteryStatus());
		assertEquals(3.062, frame.getBatteryVoltage());
		assertEquals(88.6, frame.getHumidityINT());
		assertEquals(28.29, frame.getTemperatureINT());
		assertEquals(27.81, frame.getTemperatureEXT());
	}
}
