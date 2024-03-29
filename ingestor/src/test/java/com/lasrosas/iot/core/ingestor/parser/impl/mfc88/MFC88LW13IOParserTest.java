package com.lasrosas.iot.core.ingestor.parser.impl.mfc88;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Month;
import java.util.Base64;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.support.MessageBuilder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lasrosas.iot.core.ingestor.parsers.api.ThingEncodedMessage;
import com.lasrosas.iot.core.ingestor.parsers.impl.mfc88.MFC88LW13IOFrameDecoder;
import com.lasrosas.iot.core.ingestor.parsers.impl.mfc88.MFC88LW13IOFrame.UplinkTimeSyncRequest.UplinkTimeSyncRequestOption;
import com.lasrosas.iot.core.shared.utils.ByteParser;

public class MFC88LW13IOParserTest {
	public static final Logger log = LoggerFactory.getLogger(MFC88LW13IOParserTest.class);

	private MFC88LW13IOFrameDecoder frameParser = new MFC88LW13IOFrameDecoder();
	private Gson gson = new GsonBuilder().setPrettyPrinting().create();

	@Test
	public void printFrame() {
/*
2021/04/28 22:10:15 Uplink0a 2b 00 21 24 01 00 00 00 00 00 00 00 01 00 00 00 2021/04/28 22:10:15 Uplink01 d0 36 dc 21 00 02 07 07 01 00 2021/04/28 22:09:57 Uplink01 be 36 dc 21 00 02 07 07 01 00 2021/04/28 22:09:57 Uplink01 bb 36 dc 21 00 02 07 07 01 00 2021/04/28 22:09:28 Uplink01 a7 36 dc 21 00 02 07 07 01 00 2021/04/28 22:09:26 Uplink01 a5 36 dc 21 00 02 07 07 01 01 2021/04/28 22:08:58 Join2021/04/28 22:08:58 Join2021/04/28 22:05:17 Uplink0a a6 b0 9c 2a 01 00 00 00 00 00 00 00 01 00 00 00 2021/04/28 22:05:15 Uplink01 95 98 1c 28 00 02 07 07 01 00 2021/04/28 22:04:56 Uplink01 83 98 1c 28 00 02 07 07 01 00
 */
		/*
		var byteParser = new ByteParser(new byte[] {0x01, (byte)0x83, (byte)0x98, 0x1c, 0x28, 0x00, 0x02, 0x07, 0x07, 0x01, 0x00});
		var frame = frameParser.decodeUplinkTimeSyncRequest(byteParser);
		 */

		//var bytes = new byte[] {0x0a, (byte)0xa6, (byte)0xb0, (byte)0x9c, 0x2a, 0x01, 00, 00, 00, 00, 00, 00, 00, 01, 00, 00, 00};
		var bytes = new byte[] {0x01, 0x4c, 0x53, (byte)0xdc, 0x21, 0x00, 0x02, 0x07, 0x07, 0x01, 0x00};
		var encoded = Base64.getEncoder().encodeToString(bytes);
		var message = new ThingEncodedMessage(encoded);
		var imessage = MessageBuilder.withPayload(message).build();
		var frame = frameParser.decodeUplink(imessage);
		log.info(gson.toJson(frame));
	}

	@Test
	public void parseUplinkTimeSyncRequest() {
		var byteParser = new ByteParser(new byte[] {0x78, 0x7d, 0x3c, 0x25, 0x00, 0x02, 0x00, 0x02, 0x03, 0x01});
		var frame = frameParser.decodeUplinkTimeSyncRequest(byteParser);		
		log.info(gson.toJson(frame));

		assertEquals(0x01, frame.getCode());
		assertEquals(0x787d3c25, frame.getSyncId());

		assertEquals(0x00, frame.getVersion().getMajor());
		assertEquals(0x02, frame.getVersion().getMinor());
		assertEquals(0x00, frame.getVersion().getBuild());

		assertEquals(0x0203, frame.getApplicationType());
		assertEquals(UplinkTimeSyncRequestOption.AFTER_BOOT, frame.getOption());
	}

	@Test
	public void parseDateTime() {
		var byteParser = new ByteParser(new byte[] {(byte)0xdc, 0x7e, 0x37, 0x21});

		var dateTime = frameParser.decodeDateTime(byteParser);
		System.out.println(dateTime);
		assertEquals(2016, dateTime.getYear());
		assertEquals(Month.SEPTEMBER, dateTime.getMonth());
		assertEquals(23, dateTime.getDayOfMonth());
		assertEquals(15, dateTime.getHour());
		assertEquals(54, dateTime.getMinute());
		assertEquals(56, dateTime.getSecond());
	}

/* No data test
	@Test
	public void parseUplinkIO() {
		var byteParser = new ByteParser(new byte[] {0x78, 0x7d, 0x3c, 0x25, 0x00, 0x02, 0x00, 0x02, 0x03, 0x01});

		var frame = frameParser.parseUplinkIO(byteParser);

		assertEquals(0x0A, frame.getCode());
		assertEquals(0x787d3c25, frame.getDateTime());
	}
*/
}
