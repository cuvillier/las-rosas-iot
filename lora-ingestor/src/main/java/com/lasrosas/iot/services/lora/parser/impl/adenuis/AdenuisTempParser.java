package com.lasrosas.iot.services.lora.parser.impl.adenuis;

import com.google.gson.JsonObject;
import com.lasrosas.iot.services.lora.MessageHolder;
import com.lasrosas.iot.services.lora.parser.impl.adenuis.AdenuisTempFrame.BaseFrame;
import com.lasrosas.iot.services.lora.parser.impl.adenuis.AdenuisTempFrame.ConnectionMode;
import com.lasrosas.iot.services.lora.parser.impl.adenuis.AdenuisTempFrame.ExternalSensorIdentifier;
import com.lasrosas.iot.services.lora.parser.impl.adenuis.AdenuisTempFrame.Frame0x10;
import com.lasrosas.iot.services.lora.parser.impl.adenuis.AdenuisTempFrame.Frame0x11;
import com.lasrosas.iot.services.lora.parser.impl.adenuis.AdenuisTempFrame.Frame0x12;
import com.lasrosas.iot.services.lora.parser.impl.adenuis.AdenuisTempFrame.Frame0x20;
import com.lasrosas.iot.services.lora.parser.impl.adenuis.AdenuisTempFrame.Frame0x43;
import com.lasrosas.iot.services.lora.parser.impl.adenuis.AdenuisTempFrame.InternalSensorIdentifier;
import com.lasrosas.iot.services.lora.parser.impl.adenuis.AdenuisTempFrame.ProductMode;
import com.lasrosas.iot.services.lora.parser.impl.adenuis.AdenuisTempFrame.Status;
import com.lasrosas.iot.services.lora.parser.impl.adenuis.AdenuisTempFrame.TypeOfExternalSensor;
import com.lasrosas.iot.services.utils.ByteParser;

public class AdenuisTempParser {

	public MessageHolder parse(byte[] payload) {
		ByteParser parser = new ByteParser(payload);

		int code = parser.uint8();
		Status status = parseStatus(parser);

		BaseFrame frame = null;
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

		return new MessageHolder("Adenuis.Temp.frame." + getClass().getSimpleName(), null, frame);
	}

	public Frame0x10 parse0x10(ByteParser parser) {

		Frame0x10 frame = new Frame0x10();

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

	public Frame0x11 parse0x11(ByteParser parser) {
		Frame0x11 frame = new Frame0x11();

		frame.setS309_HighThresholdOfTheInternalSensor(parser.uint16BI());
		frame.setS310_HysteresisOfTheHighThresholdOfTheInternalSensor(parser.uint8());
		frame.setS311_LowThresholdOfTheInternalSensor(parser.uint16BI());
		frame.setS312_HysteresisOfTheLowThresholdOfTheInternalSensor(parser.uint8());
		frame.setS318_SuperSamplingFactor(parser.uint8());

		return frame;
	}

	public Frame0x12 parse0x12(ByteParser parser) {
		Frame0x12 frame = new Frame0x12();

		frame.setS313_HighThresholdOfTheExternalSensor(parser.uint16BI());
		frame.setS314_HysteresisOfTheHighThresholdOfTheExternalSensor(parser.uint8());
		frame.setS315_LowThresholdOfTheExternalSensor(parser.uint16BI());
		frame.setS316_HysteresisOfTheLowThresholdOfTheExternalSensor(parser.uint8());

		return frame;
	}

	public Frame0x20 parse0x20(ByteParser parser) {
		Frame0x20 frame = new Frame0x20();

		frame.setAdr(parser.uint8() == 1);
		frame.setConectionMode(ConnectionMode.parse(parser.uint8()));
		return frame;
	}

	public Frame0x43 parse0x300x43(ByteParser parser) {
		Frame0x43 frame = new Frame0x43();

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
