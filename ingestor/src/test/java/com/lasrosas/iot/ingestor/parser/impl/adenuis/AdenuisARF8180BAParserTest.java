package com.lasrosas.iot.ingestor.parser.impl.adenuis;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.lasrosas.iot.ingestor.parser.impl.adenuis.AdenuisARF8170BAFrame.ChannelState;
import com.lasrosas.iot.ingestor.parser.impl.adenuis.AdenuisARF8170BAFrame.ChannelType;
import com.lasrosas.iot.ingestor.parser.impl.adenuis.AdenuisARF8170BAFrame.ProvisioningMode;
import com.lasrosas.iot.ingestor.parser.impl.adenuis.AdenuisARF8170BAFrame.RequestStatus;
import com.lasrosas.iot.shared.utils.ByteParser;

public class AdenuisARF8180BAParserTest {
	private AdenuisARF8170BAFrameDecoder decoder = new AdenuisARF8170BAFrameDecoder();

	@Test
	public void status() {
		var byteParser = new ByteParser(new byte[] {(byte)0xA2});
		var status = decoder.parseStatus(byteParser);
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
		assertEquals(ChannelType.INPUT_EVENT_ON_OFF, frame.getS320_Channel1Configuration().getType());

		assertEquals(ChannelType.INPUT_EVENT_ON_OFF, frame.getS321_Channel2Configuration().getType());
		assertEquals(100, frame.getS321_Channel2Configuration().getDebounceDurationMs());

		assertEquals(ChannelType.INPUT_EVENT_ON_OFF, frame.getS322_Channel3Configuration().getType());
		assertEquals(100, frame.getS322_Channel3Configuration().getDebounceDurationMs());

		assertEquals(ChannelType.INPUT_EVENT_ON_OFF, frame.getS323_Channel4Configuration().getType());
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
		assertEquals(ProvisioningMode.OTAA, frame.getS221_ProvisioningMode());
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
		assertEquals(RequestStatus.SUCSESS, frame.getRequestStatus());
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

		assertEquals(ChannelState.OPEN_OFF, frame.getChannel1State());
		assertEquals(ChannelState.CLOSED_ON, frame.getChannel2State());
		assertEquals(ChannelState.OPEN_OFF, frame.getChannel3State());
		assertEquals(ChannelState.CLOSED_ON, frame.getChannel4State());
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

		assertEquals(ChannelState.OPEN_OFF, frame.getChannel1CurrentState());
		assertEquals(ChannelState.CLOSED_ON, frame.getChannel1PreviousFrameState());

		assertEquals(ChannelState.CLOSED_ON, frame.getChannel2CurrentState());
		assertEquals(ChannelState.OPEN_OFF, frame.getChannel2PreviousFrameState());

		assertEquals(ChannelState.OPEN_OFF, frame.getChannel3CurrentState());
		assertEquals(ChannelState.OPEN_OFF, frame.getChannel3PreviousFrameState());

		assertEquals(ChannelState.CLOSED_ON, frame.getChannel4CurrentState());
		assertEquals(ChannelState.OPEN_OFF, frame.getChannel4PreviousFrameState());
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

		assertEquals("Mon Oct 17 22:32:57 CEST 2022", new Date(frame.getTimestamp()) + "");
	}
}
