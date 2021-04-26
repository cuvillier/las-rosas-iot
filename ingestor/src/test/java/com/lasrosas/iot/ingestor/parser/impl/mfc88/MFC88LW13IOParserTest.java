package com.lasrosas.iot.ingestor.parser.impl.mfc88;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Month;

import org.junit.jupiter.api.Test;

import com.lasrosas.iot.ingestor.parser.mfc88.MFC88LW13IOFrame.UplinkTimeSyncRequest.UplinkTimeSyncRequestOption;
import com.lasrosas.iot.ingestor.parser.mfc88.MFC88LW13IOFrameDecoder;
import com.lasrosas.iot.shared.utils.ByteParser;

public class MFC88LW13IOParserTest {
	private MFC88LW13IOFrameDecoder frameParser = new MFC88LW13IOFrameDecoder();

	@Test
	public void frame0x10() {
		var byteParser = new ByteParser(new byte[] {0x78, 0x7d, 0x3c, 0x25, 0x00, 0x02, 0x00, 0x02, 0x03, 0x01});

		var frame = frameParser.parseUplinkTimeSyncRequest(byteParser);

		assertEquals(0x01, frame.getCode());
		assertEquals(0x787d3c25, frame.getSyncId());

		assertEquals(0x00, frame.getVersion().getMajor());
		assertEquals(0x02, frame.getVersion().getMinor());
		assertEquals(0x00, frame.getVersion().getBuild());

		assertEquals(0x0203, frame.getApplicationType());
		assertEquals(UplinkTimeSyncRequestOption.AFTER_BOOT, frame.getOption());
	}

	@Test
	public void dareTime() {
		var byteParser = new ByteParser(new byte[] {(byte)0xdc, 0x7e, 0x37, 0x21});

		var dateTime = frameParser.parseDateTime(byteParser);
		System.out.println(dateTime);
		assertEquals(2016, dateTime.getYear());
		assertEquals(Month.SEPTEMBER, dateTime.getMonth());
		assertEquals(23, dateTime.getDayOfMonth());
		assertEquals(15, dateTime.getHour());
		assertEquals(54, dateTime.getMinute());
		assertEquals(56, dateTime.getSecond());
	}
}
