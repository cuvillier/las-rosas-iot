package com.lasrosas.iot.ingestor.services.sensors.impl.adeunis;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import com.google.gson.JsonObject;
import com.lasrosas.iot.ingestor.services.sensors.api.ThingDataMessage;
import com.lasrosas.iot.ingestor.services.sensors.api.ThingEncodedMessage;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8180BAFrame.ConnectionMode;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8180BAFrame.ExternalSensorIdentifier;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8180BAFrame.InternalSensorIdentifier;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8180BAFrame.ProductMode;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8180BAFrame.Status;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8180BAFrame.TypeOfExternalSensor;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8180BAFrame.UplinkFrame;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8180BAFrame.UplinkFrame0x10;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8180BAFrame.UplinkFrame0x11;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8180BAFrame.UplinkFrame0x12;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8180BAFrame.UplinkFrame0x20;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8180BAFrame.UplinkFrame0x43;
import com.lasrosas.iot.shared.utils.ByteParser;

public class AdeunisTempFrameDecoder {

	public Message<ThingDataMessage> decodeUplink(Message<ThingEncodedMessage> imessage) {
		ByteParser parser = new ByteParser(imessage.getPayload().decodeData());

		int code = parser.uint8();
		Status status = parseStatus(parser);

		UplinkFrame frame = null;
		switch(code) {
		case 0x10 :
			frame = parse0x10(parser);
			break;
		case 0x11:
			frame = parse0x11(parser);
			break;
		case 0x12:
			frame = parse0x12(parser);
			break;
		case 0x20:
			frame = parse0x20(parser);
			break;
		case 0x30:
			frame = parse0x300x43(parser);
			break;
		case 0x43:
			frame = parse0x300x43(parser);
			break;
		default:
			throw new RuntimeException("Unknown frame code.");
		}

		frame.setStatus(status);

		return MessageBuilder.createMessage(frame, imessage.getHeaders());
	}

	public UplinkFrame0x10 parse0x10(ByteParser parser) {

		UplinkFrame0x10 frame = new UplinkFrame0x10();

		frame.setS300_PeriodicityOfKeepAlive10mn(parser.uint8());
		frame.setS301_PeriodicityOfTransmission10mn(parser.uint8());
		frame.setS302_ConfigurationOfTheInternalSensor(parser.uint8());
		frame.setS303_ConfigurationOfTheEventsOfTheInternalSensor(parser.uint8());
		frame.setS304_ConfigurationOfTheExternalSensor(parser.uint8());
		frame.setS305_ConfigurationOfTheEventsOfTheExternalSensor(parser.uint8());
		frame.setS306_ProductMode(ProductMode.parse306(parser.uint8()));
		frame.setTypeOfExternalSensor(TypeOfExternalSensor.parse(parser.uint8()));
		frame.setS317_PeriodicityOfTheAcquisitionMn(parser.uint8());

		return frame;
	}

	public UplinkFrame0x11 parse0x11(ByteParser parser) {
		UplinkFrame0x11 frame = new UplinkFrame0x11();

		frame.setS309_HighThresholdOfTheInternalSensor(parser.uint16BI());
		frame.setS310_HysteresisOfTheHighThresholdOfTheInternalSensor(parser.uint8());
		frame.setS311_LowThresholdOfTheInternalSensor(parser.uint16BI());
		frame.setS312_HysteresisOfTheLowThresholdOfTheInternalSensor(parser.uint8());
		frame.setS318_SuperSamplingFactor(parser.uint8());

		return frame;
	}

	public UplinkFrame0x12 parse0x12(ByteParser parser) {
		UplinkFrame0x12 frame = new UplinkFrame0x12();

		frame.setS313_HighThresholdOfTheExternalSensor(parser.uint16BI());
		frame.setS314_HysteresisOfTheHighThresholdOfTheExternalSensor(parser.uint8());
		frame.setS315_LowThresholdOfTheExternalSensor(parser.uint16BI());
		frame.setS316_HysteresisOfTheLowThresholdOfTheExternalSensor(parser.uint8());

		return frame;
	}

	public UplinkFrame0x20 parse0x20(ByteParser parser) {
		UplinkFrame0x20 frame = new UplinkFrame0x20();

		frame.setAdr(parser.uint8() == 1);
		frame.setConectionMode(ConnectionMode.parse(parser.uint8()));
		return frame;
	}

	public UplinkFrame0x43 parse0x300x43(ByteParser parser) {
		UplinkFrame0x43 frame = new UplinkFrame0x43();

		frame.setInternalSensorIdentifier(InternalSensorIdentifier.parse(parser.uint4()));
		frame.setS302_UserIdentifier(parser.uint4());
		frame.setValueInternalSensor10thDeg(parser.sint16BI());
		frame.setExternalSensorIdentifier(ExternalSensorIdentifier.parse(parser.uint4()));
		frame.setS304_UserIdentifier(parser.uint4());
		frame.setValueExternalSensor10thDeg(parser.sint16BI());

		return frame;
	}

	public Status parseStatus(ByteParser parser) {
		Status status = new Status();

		parser.skipBits(1);
		status.setLowBat(parser.bit() == 1?true: false);
		status.setHwError(parser.bit() == 1?true: false);
		parser.skipBits(2);
		status.setFrameCounter(parser.uint(3));

		return status;
	}

	public JsonObject normalize(Object message) {
		return null;
	}
}
