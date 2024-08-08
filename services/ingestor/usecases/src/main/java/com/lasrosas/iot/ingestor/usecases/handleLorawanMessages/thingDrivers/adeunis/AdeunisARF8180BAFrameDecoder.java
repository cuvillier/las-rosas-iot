package com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.adeunis;

import com.lasrosas.iot.ingestor.domain.model.message.BaseMessage;
import com.lasrosas.iot.ingestor.shared.ByteParser;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.LorawanMessageUplinkRx;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.adeunis.AdeunisARF8180BAFrame.*;

import java.util.List;

public class AdeunisARF8180BAFrameDecoder {

	public BaseMessage decodeUplink(LorawanMessageUplinkRx uplink) {
		ByteParser parser = new ByteParser(uplink.decodeData());

		int code = parser.uint8();
		Status status = parseStatus(parser);

		UplinkFrame frame = switch(code) {
				case 0x10 -> parse0x10(parser);
				case 0x11 -> parse0x11(parser);
				case 0x12 -> parse0x12(parser);
				case 0x20 -> parse0x20(parser);
				case 0x30 -> parse0x300x43(parser);
				case 0x43 -> parse0x300x43(parser);
				default ->  throw new RuntimeException("Unknown frame code.");
		};

		frame.setStatus(status);
		return frame;
	}

	public UplinkFrame0x10 parse0x10(ByteParser parser) {

		return  UplinkFrame0x10.builder()
					.S300_PeriodicityOfKeepAlive10mn(parser.uint8())
					.S301_PeriodicityOfTransmission10mn(parser.uint8())
					.S302_ConfigurationOfTheInternalSensor(parser.uint8())
					.S303_ConfigurationOfTheEventsOfTheInternalSensor(parser.uint8())
					.S304_ConfigurationOfTheExternalSensor(parser.uint8())
					.S305_ConfigurationOfTheEventsOfTheExternalSensor(parser.uint8())
					.S306_ProductMode(ProductMode.parse306(parser.uint8()))
					.typeOfExternalSensor(TypeOfExternalSensor.parse(parser.uint8()))
					.S317_PeriodicityOfTheAcquisitionMn(parser.uint8())
					.build();
	}

	public UplinkFrame0x11 parse0x11(ByteParser parser) {
		return  UplinkFrame0x11.builder()
					.S309_HighThresholdOfTheInternalSensor(parser.uint16BI())
					.S310_HysteresisOfTheHighThresholdOfTheInternalSensor(parser.uint8())
					.S311_LowThresholdOfTheInternalSensor(parser.uint16BI())
					.S312_HysteresisOfTheLowThresholdOfTheInternalSensor(parser.uint8())
					.S318_SuperSamplingFactor(parser.uint8())
					.build();
	}

	public UplinkFrame0x12 parse0x12(ByteParser parser) {
		return UplinkFrame0x12.builder()
					.S313_HighThresholdOfTheExternalSensor(parser.uint16BI())
					.S314_HysteresisOfTheHighThresholdOfTheExternalSensor(parser.uint8())
					.S315_LowThresholdOfTheExternalSensor(parser.uint16BI())
					.S316_HysteresisOfTheLowThresholdOfTheExternalSensor(parser.uint8())
					.build();
	}

	public UplinkFrame0x20 parse0x20(ByteParser parser) {
		return UplinkFrame0x20.builder()
					.adr(parser.uint8() == 1)
					.conectionMode(ConnectionMode.parse(parser.uint8()))
					.build();
	}

	public UplinkFrame0x43 parse0x300x43(ByteParser parser) {
		return UplinkFrame0x43.builder()
					.internalSensorIdentifier(InternalSensorIdentifier.parse(parser.uint4()))
					.S302_UserIdentifier(parser.uint4())
					.valueInternalSensor10thDeg(parser.sint16BI())
					.externalSensorIdentifier(ExternalSensorIdentifier.parse(parser.uint4()))
					.S304_UserIdentifier(parser.uint4())
					.valueExternalSensor10thDeg(parser.sint16BI())
					.build();
	}

	public AdeunisARF8180BAFrame.Status parseStatus(ByteParser parser) {
		return Status.builder()
				.lowBat(parser.skipBits(1).bit() == 1?true: false)
				.hwError(parser.bit() == 1?true: false)
				.frameCounter(parser.skipBits(2).uint(3))
				.build();
	}

	public List<BaseMessage> normalize(BaseMessage message) {
		return null;
	}
}
