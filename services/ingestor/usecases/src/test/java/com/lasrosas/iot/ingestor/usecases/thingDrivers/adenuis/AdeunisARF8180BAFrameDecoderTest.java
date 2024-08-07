package com.lasrosas.iot.ingestor.usecases.thingDrivers.adenuis;

import com.lasrosas.iot.ingestor.shared.ByteParser;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.adeunis.AdeunisARF8180BAFrame;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.adeunis.AdeunisARF8180BAFrameDecoder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AdeunisARF8180BAFrameDecoderTest {
	private AdeunisARF8180BAFrameDecoder  decoder = new AdeunisARF8180BAFrameDecoder();

	@Test
	public void status() {
		var byteParser = new ByteParser(new byte[] {(byte)0xA2});
		var status = decoder.parseStatus(byteParser);

		assertFalse(status.isHwError());
		assertTrue(status.isLowBat());
		assertEquals(5, status.getFrameCounter());
	}

	@Test
	public void frame0x10() {
		var byteParser = new ByteParser(new byte[] {0x48, 0x00, (byte)0xD0, 0x03, (byte)0x80, 0x02, 0x01, 0x02, 0x0A});
		
		var frame = decoder.parse0x10(byteParser);
		assertEquals(0x48, frame.getS300_PeriodicityOfKeepAlive10mn());
		assertEquals(0, frame.getS301_PeriodicityOfTransmission10mn());
		assertEquals(0xD0, frame.getS302_ConfigurationOfTheInternalSensor());
		assertEquals(3, frame.getS303_ConfigurationOfTheEventsOfTheInternalSensor());
		assertEquals(0x80, frame.getS304_ConfigurationOfTheExternalSensor());
		assertEquals(0x2, frame.getS305_ConfigurationOfTheEventsOfTheExternalSensor());
		assertEquals(AdeunisARF8180BAFrame.ProductMode.PRODUCTION, frame.getS306_ProductMode());
		assertEquals(AdeunisARF8180BAFrame.TypeOfExternalSensor.FANB57863_400_1, frame.getTypeOfExternalSensor());
		assertEquals(0xA, frame.getS317_PeriodicityOfTheAcquisitionMn());
	}

	@Test
	public void frame0x11() {
		var byteParser = new ByteParser(new byte[] {0x01, 0x2C, 0x0A, 0x00, 0x32, 0x05, 0x06});

		var frame = decoder.parse0x11(byteParser);
		assertEquals(0x012C, frame.getS309_HighThresholdOfTheInternalSensor());
		assertEquals(0x0A, frame.getS310_HysteresisOfTheHighThresholdOfTheInternalSensor());
		assertEquals(0x0032, frame.getS311_LowThresholdOfTheInternalSensor());
		assertEquals(0x05, frame.getS312_HysteresisOfTheLowThresholdOfTheInternalSensor());
		assertEquals(0x06, frame.getS318_SuperSamplingFactor());
	}

	@Test
	public void frame0x12() {
		var byteParser = new ByteParser(new byte[] {0x01, (byte)0x90, 0x14, (byte)0xFE, (byte)0xD4, 0x05});

		var frame = decoder.parse0x12(byteParser);
		assertEquals(0x0190, frame.getS313_HighThresholdOfTheExternalSensor());
		assertEquals(0x14, frame.getS314_HysteresisOfTheHighThresholdOfTheExternalSensor());
		assertEquals(0xFED4, frame.getS315_LowThresholdOfTheExternalSensor());
		assertEquals(0x05, frame.getS316_HysteresisOfTheLowThresholdOfTheExternalSensor());
	}

	@Test
	public void frame0x20() {
		var byteParser = new ByteParser(new byte[] {0x01, 0x01});

		var frame = decoder.parse0x20(byteParser);
		assertEquals(true, frame.isAdr());
		assertEquals(AdeunisARF8180BAFrame.ConnectionMode.OTAA, frame.getConectionMode());;
	}

	@Test
	public void frame0x30() {
		var byteParser = new ByteParser(new byte[] {(byte)0xD1, 0x01, 0x5E, (byte)0x82, (byte)0xFF, 0x06});
		
		var frame = decoder.parse0x300x43(byteParser);
		assertEquals(AdeunisARF8180BAFrame.InternalSensorIdentifier.B57863S0303F040, frame.getInternalSensorIdentifier());
		assertEquals(0x0D, frame.getS302_UserIdentifier());
		assertEquals(350, frame.getValueInternalSensor10thDeg());
		assertEquals(AdeunisARF8180BAFrame.ExternalSensorIdentifier.FANB57863_400_1, frame.getExternalSensorIdentifier());
		assertEquals(0x08, frame.getS304_UserIdentifier());
		assertEquals(-250, frame.getValueExternalSensor10thDeg());
	}
}
