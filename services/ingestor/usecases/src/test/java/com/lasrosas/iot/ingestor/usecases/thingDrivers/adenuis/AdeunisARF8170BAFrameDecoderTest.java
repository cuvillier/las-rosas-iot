package com.lasrosas.iot.ingestor.usecases.thingDrivers.adenuis;

import com.lasrosas.iot.ingestor.shared.ByteParser;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.LorawanMessageUplinkRx;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.adeunis.AdeunisARF8170BAFrame;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.adeunis.AdeunisARF8170BAFrameDecoder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;

import static com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.adeunis.AdeunisARF8170BAFrame.ChannelState.CLOSED_ON;
import static com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.adeunis.AdeunisARF8170BAFrame.ChannelState.OPEN_OFF;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class AdeunisARF8170BAFrameDecoderTest {
	private AdeunisARF8170BAFrameDecoder decoder = new AdeunisARF8170BAFrameDecoder();

	@Test
	public void decode() {
		var message = LorawanMessageUplinkRx.builder()
				.data("MCAAIQAAAAAAAAA=")
				.dataEncode(LorawanMessageUplinkRx.BASE64)
				.build();

		var frame = decoder.decodeUplink(message);

		assertEquals(AdeunisARF8170BAFrame.UplinkFrame0x30.class, frame.getClass());
		AdeunisARF8170BAFrame.UplinkFrame0x30 frame0x30 = (AdeunisARF8170BAFrame.UplinkFrame0x30)frame;
		
		assertEquals(1, frame0x30.getStatus().getFrameCounter());
		assertFalse(frame0x30.getStatus().isConfig());
		assertFalse(frame0x30.getStatus().isLowBat());
		assertFalse(frame0x30.getStatus().isTimestamp());

		assertEquals(OPEN_OFF, frame0x30.getChannel1State());
		assertEquals(OPEN_OFF, frame0x30.getChannel2State());
		assertEquals(OPEN_OFF, frame0x30.getChannel3State());
		assertEquals(OPEN_OFF, frame0x30.getChannel4State());

		assertEquals(33, frame0x30.getChannel1EventCounter());
		assertEquals(0, frame0x30.getChannel2EventCounter());
		assertEquals(0, frame0x30.getChannel3EventCounter());
		assertEquals(0, frame0x30.getChannel4EventCounter());

		assertNull(frame0x30.getTimestamp());
}

	@Test
	public void uplinkFrame0x10() {
		var byteParser = new ByteParser(new byte[] {0x20, 0x21, (byte)0xC0, 0x00, 0x00, 0x43, 0x43, 0x43, 0x43});
		
		var frame = decoder.parseUplinkFrame0x10(byteParser);

		assertEquals(1, frame.getStatus().getFrameCounter());
		assertFalse(frame.getStatus().isConfig());
		assertFalse(frame.getStatus().isLowBat());
		assertFalse(frame.getStatus().isTimestamp());

		assertEquals(100, frame.getS320_Channel1Configuration().getDebounceDurationMs());
		assertEquals(AdeunisARF8170BAFrame.ChannelType.INPUT_EVENT_ON_OFF, frame.getS320_Channel1Configuration().getType());

		assertEquals(AdeunisARF8170BAFrame.ChannelType.INPUT_EVENT_ON_OFF, frame.getS321_Channel2Configuration().getType());
		assertEquals(100, frame.getS321_Channel2Configuration().getDebounceDurationMs());

		assertEquals(AdeunisARF8170BAFrame.ChannelType.INPUT_EVENT_ON_OFF, frame.getS322_Channel3Configuration().getType());
		assertEquals(100, frame.getS322_Channel3Configuration().getDebounceDurationMs());

		assertEquals(AdeunisARF8170BAFrame.ChannelType.INPUT_EVENT_ON_OFF, frame.getS323_Channel4Configuration().getType());
		assertEquals(100, frame.getS323_Channel4Configuration().getDebounceDurationMs());
	}

	@Test
	public void tooManyBytes() {
		var byteParser = new ByteParser(new byte[] {0x20, 0x05, 0x01, 0x00});

		Assertions.assertThrows(RuntimeException.class, () -> decoder.parseUplinkFrame0x20(byteParser));
	}

	@Test
	public void uplinkFrame0x20() {
		var byteParser = new ByteParser(new byte[] {0x20, 0x05, 0x01});
		
		var frame = decoder.parseUplinkFrame0x20(byteParser);

		assertEquals(1, frame.getStatus().getFrameCounter());
		assertFalse(frame.getStatus().isConfig());
		assertFalse(frame.getStatus().isLowBat());
		assertFalse(frame.getStatus().isTimestamp());

		assertTrue(frame.getS220_lorawanOptions().isClassA());
		assertTrue(frame.getS220_lorawanOptions().isDutyCycle());
		assertTrue(frame.getS220_lorawanOptions().isActivatedByADR());
		assertEquals(AdeunisARF8170BAFrame.ProvisioningMode.OTAA, frame.getS221_ProvisioningMode());
	}

	@Test
	public void uplinkFrame0x2F() {
		var byteParser = new ByteParser(new byte[] {0x20, 0x06, 0x1});

		var frame = decoder.parseUplinkFrame0x2F(byteParser);

		assertEquals(1, frame.getStatus().getFrameCounter());
		assertFalse(frame.getStatus().isConfig());
		assertFalse(frame.getStatus().isLowBat());
		assertFalse(frame.getStatus().isTimestamp());

		assertEquals(0x6, frame.getDownlinkFrameCode());
		assertEquals(AdeunisARF8170BAFrame.Request2FStatus.SUCSESS, frame.getRequestStatus());
	}

	@Test
	public void uplinkFrame0x30() {
		var byteParser = new ByteParser(new byte[] {(byte)0xE2, 0x00, 0x01, 0x01, 0x00, 0x00, 0x00, (byte)0xFF, (byte)0xFF, 0x0A});

		var frame = decoder.parseUplinkFrame0x30(byteParser);

		assertEquals(7, frame.getStatus().getFrameCounter());
		assertFalse(frame.getStatus().isConfig());
		assertTrue(frame.getStatus().isLowBat());
		assertFalse(frame.getStatus().isTimestamp());

		assertEquals(0x1, frame.getChannel1EventCounter());
		assertEquals(256, frame.getChannel2EventCounter());
		assertEquals(0, frame.getChannel3EventCounter());
		assertEquals(65535, frame.getChannel4EventCounter());

		assertEquals(OPEN_OFF, frame.getChannel1State());
		assertEquals(CLOSED_ON, frame.getChannel2State());
		assertEquals(OPEN_OFF, frame.getChannel3State());
		assertEquals(CLOSED_ON, frame.getChannel4State());
	}

	@Test
	public void uplinkFrame0x40() {
		var byteParser = new ByteParser(new byte[] {0x40, 0x00, 0x01, 0x01, 0x00, 0x00, 0x00, 0x00, 0x01, 0x46});

		var frame = decoder.parseUplinkFrame0x40(byteParser);

		assertEquals(2, frame.getStatus().getFrameCounter());
		assertFalse(frame.getStatus().isConfig());
		assertFalse(frame.getStatus().isLowBat());
		assertFalse(frame.getStatus().isTimestamp());

		assertEquals(0x1, frame.getChannel1EventCounter());
		assertEquals(256, frame.getChannel2EventCounter());
		assertEquals(0, frame.getChannel3EventCounter());
		assertEquals(1, frame.getChannel4EventCounter());

		assertEquals(OPEN_OFF, frame.getChannel1CurrentState());
		assertEquals(CLOSED_ON, frame.getChannel1PreviousFrameState());

		assertEquals(CLOSED_ON, frame.getChannel2CurrentState());
		assertEquals(OPEN_OFF, frame.getChannel2PreviousFrameState());

		assertEquals(OPEN_OFF, frame.getChannel3CurrentState());
		assertEquals(OPEN_OFF, frame.getChannel3PreviousFrameState());

		assertEquals(CLOSED_ON, frame.getChannel4CurrentState());
		assertEquals(OPEN_OFF, frame.getChannel4PreviousFrameState());
	}

	@Test
	public void uplinkFrame0x59() {
		var byteParser = new ByteParser(new byte[] {(byte)0xA4, 0x05, 0x00, 0x01, 0x23, 0x45, 0x00, 0x00, 0x11, 0x00, 0x12, 0x6b, (byte)0x94, (byte)0xF9});

		var frame = decoder.parseUplinkFrame0x59(byteParser);

		assertEquals(5, frame.getStatus().getFrameCounter());
		assertFalse(frame.getStatus().isConfig());
		assertFalse(frame.getStatus().isLowBat());
		assertTrue(frame.getStatus().isTimestamp());

		assertEquals(74565, frame.getChannel1TimeCounter());
		assertNull(frame.getChannel2TimeCounter());
		assertEquals(4352, frame.getChannel3TimeCounter());
		assertNull(frame.getChannel4TimeCounter());

		assertEquals(frame.getTimestamp(), 1666038777000L);
	}
	
	@Test
	public void uplinkFrame0x31() {
		var byteParser = new ByteParser(new byte[] {(byte)0xA4,
				/* 304 2 bytes			1962		*/ 0x07, (byte)0xAA,
				/* 306 1 byte			200			*/ (byte)0xC8,
				/* 300 2 bytes			12345 		*/ 0x30, 0x39,
				/* 301 2 bytes			30000		*/ 0x075, 0x30,
				/* 308 4 bytes			4042322160	*/ (byte)0xF0, (byte)0xF0, (byte)0xF0, (byte)0xF0,
				/* 315 1 bytes, signed	-128		*/ (byte)0xFF,
				});

		var frame0x40 = new AdeunisARF8170BAFrame.DownlinkFrame0x40();

		frame0x40.addRegisters(304, 306, 300, 301, 308, 315);
		var frame = decoder.parseUplinkFrame0x31(byteParser, frame0x40);

		assertEquals(AdeunisARF8170BAFrame.UplinkFrame0x31.CODE, frame.getCode());

		assertEquals(5, frame.getStatus().getFrameCounter());
		assertFalse(frame.getStatus().isConfig());
		assertFalse(frame.getStatus().isLowBat());
		assertTrue(frame.getStatus().isTimestamp());

		var value = frame.getValues().get(0);
		assertEquals(1962, value.getAsInt());			// 304		

		value = frame.getValues().get(1);
		assertEquals(200, value.getAsInt());			// 306

		value = frame.getValues().get(2);
		assertEquals(12345, value.getAsInt());			// 300

		value = frame.getValues().get(3);
		assertEquals(30000, value.getAsInt());			// 301

		value = frame.getValues().get(4);
		assertEquals(4042322160L, value.getAsLong());	// 308

		value = frame.getValues().get(5);
		assertEquals(-1, value.getAsInt());			// 315
	}

	@Test
	public void uplinkFrame0x33() {
		var byteParser = new ByteParser(new byte[] {(byte)0x80, 0x04, 0x01, 0x40});

		var frame = decoder.parseUplinkFrame0x33(byteParser);

		assertEquals(4, frame.getStatus().getFrameCounter());
		assertFalse(frame.getStatus().isConfig());
		assertFalse(frame.getStatus().isLowBat());
		assertFalse(frame.getStatus().isTimestamp());

		assertEquals(AdeunisARF8170BAFrame.Request33Status.ERROR_INVALID_REGISTER, frame.getRequestStatus());
		assertEquals(320, frame.getRegsiterIdIfError());
	}

	@Test
	public void downlinkFrame0x01() {
		var frame = new AdeunisARF8170BAFrame.DownlinkFrame0x01();
		byte[] bytes = decoder.encodeDownlinkFrame0x01(frame);

		assertEquals(1, bytes.length);
		assertEquals(0x01, bytes[0]);
	}

	@Test
	public void downlinkFrame0x02() {
		var frame = new AdeunisARF8170BAFrame.DownlinkFrame0x02();
		byte[] bytes = decoder.encodeDownlinkFrame0x02(frame);

		assertEquals(1, bytes.length);
		assertEquals(0x02, bytes[0]);
	}

	@Test
	public void downlinkFrame0x05() {
		var frame = new AdeunisARF8170BAFrame.DownlinkFrame0x05();
		byte[] bytes = decoder.encodeDownlinkFrame0x05(frame);

		assertEquals(1, bytes.length);
		assertEquals(0x05, bytes[0]);
	}

	@Test
	public void downlinkFrame0x07() {
		var frame = new AdeunisARF8170BAFrame.DownlinkFrame0x07();
		frame.setChannel2PulseDuration(1);
		frame.setChannel3PulseDuration(255);
		byte[] bytes = decoder.encodeDownlinkFrame0x07(frame);

		assertEquals(6, bytes.length);
		assertEquals(0x07, bytes[0]);
		assertEquals(0x00, bytes[1]);
		assertEquals(0x01, bytes[2]);
		assertEquals((byte)0xFF, bytes[3]);
		assertEquals(0x00, bytes[4]);
		assertEquals(0x00, bytes[5]);
	}

	@Test
	public void downlinkFrame0x40() {
		var frame = new AdeunisARF8170BAFrame.DownlinkFrame0x40();
		frame.addRegisters(300, 301, 302, 304);
		byte[] bytes = decoder.encodeDownlinkFrame0x40(frame);

		assertEquals(5, bytes.length);
		assertEquals(0x40, bytes[0]);
		assertEquals(0, bytes[1]);
		assertEquals(1, bytes[2]);
		assertEquals(2, bytes[3]);
		assertEquals(4, bytes[4]);
	}

	@Test
	public void downlinkFrame0x41() {
		var frame = new AdeunisARF8170BAFrame.DownlinkFrame0x41();
		AdeunisARF8170BAFrame.RegisterValue[] values = {
				new AdeunisARF8170BAFrame.RegisterValue(306, 123),
				new AdeunisARF8170BAFrame.RegisterValue(304, 1234),
				new AdeunisARF8170BAFrame.RegisterValue(308, 1234123412L),
				new AdeunisARF8170BAFrame.RegisterValue(315, -100)
		};
		frame.setRegisterValues(values);
		byte[] bytes = decoder.encodeDownlinkFrame0x41(frame);

		assertEquals(9, bytes.length);
		assertEquals(0x41, bytes[0]);

		assertEquals(0x7B, bytes[1]);

		assertEquals(0x04, bytes[2]);
		assertEquals((byte)0xD2, bytes[3]);

		assertEquals((byte)0x49, bytes[4]);
		assertEquals((byte)0x8F, bytes[5]);
		assertEquals((byte)0x3A, bytes[6]);
		assertEquals((byte)0x94, bytes[7]);
		
		assertEquals((byte)-100, bytes[8]);
	}

	@Test
	public void downlinkFrame0x48() {
		var frame = new AdeunisARF8170BAFrame.DownlinkFrame0x48();
		frame.setMinutesBeforeReboot(1440);
		byte[] bytes = decoder.encodeDownlinkFrame0x48(frame);

		assertEquals(3, bytes.length);
		assertEquals((byte)0x48, bytes[0]);
		assertEquals(0x05, bytes[1]);
		assertEquals((byte)0xA0, bytes[2]);
	}

	@Test
	public void downlinkFrame0x49() {
		var frame = new AdeunisARF8170BAFrame.DownlinkFrame0x49();
		frame.setTimestamp(238613932L /* Epoch 2013 */ + 1356998400L /* ms 2013 */);
		frame.setClockDriftCompensation(-35);
		byte[] bytes = decoder.encodeDownlinkFrame0x49(frame);

		assertEquals(6, bytes.length);

		assertEquals((byte)0x49, bytes[0]);

		assertEquals((byte)0x0E, bytes[1]);
		assertEquals((byte)0x38, bytes[2]);
		assertEquals((byte)0xF5, bytes[3]);
		assertEquals((byte)0xAC, bytes[4]);

		assertEquals((byte)0xDD, bytes[5]);
	}
}
