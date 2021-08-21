package com.lasrosas.iot.ingestor.services.sensors.impl.adeunis;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.lasrosas.iot.ingestor.ThingMessageHolder;
import com.lasrosas.iot.ingestor.services.sensors.api.ThingDataMessage;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8170BAFrame.BaseFrame;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8170BAFrame.ChannelConfiguration;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8170BAFrame.ChannelState;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8170BAFrame.ChannelType;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8170BAFrame.DownlinkFrame0x01;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8170BAFrame.DownlinkFrame0x02;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8170BAFrame.DownlinkFrame0x05;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8170BAFrame.DownlinkFrame0x07;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8170BAFrame.DownlinkFrame0x40;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8170BAFrame.DownlinkFrame0x41;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8170BAFrame.DownlinkFrame0x48;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8170BAFrame.DownlinkFrame0x49;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8170BAFrame.LoraWanOptions;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8170BAFrame.ProvisioningMode;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8170BAFrame.RegisterValue;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8170BAFrame.Request2FStatus;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8170BAFrame.Request33Status;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8170BAFrame.Status;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8170BAFrame.UplinkFrame0x10;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8170BAFrame.UplinkFrame0x20;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8170BAFrame.UplinkFrame0x2F;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8170BAFrame.UplinkFrame0x30;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8170BAFrame.UplinkFrame0x31;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8170BAFrame.UplinkFrame0x33;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8170BAFrame.UplinkFrame0x40;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8170BAFrame.UplinkFrame0x59;
import com.lasrosas.iot.shared.utils.ByteParser;
import com.lasrosas.iot.shared.utils.ByteParser.ByteOrder;

public class AdeunisARF8170BAFrameDecoder {

	public static final long TIMESTAMP_1_1_1970 = 1356998400;

	public static class Register {
		private int number;
		private int size;
		private boolean signed;

		public Register(int number, int size, boolean signed) {
			super();
			this.number = number;
			this.size = size;
			this.signed = signed;
		}
		public int getNumber() {
			return number;
		}
		public int getSize() {
			return size;
		}
		public boolean isSigned() {
			return signed;
		}
		public int getSizeBits() {
			return size * 8;
		}
	}

	public static Map<Integer, Register> registers = new HashMap<>();

	static {
		addRegister(304, 2);
		addRegister(306, 1);
		addRegister(300, 2);
		addRegister(301, 2);
		addRegister(308, 4);
		addRegister(315, 1, true);
		addRegister(316, 1);
		addRegister(318, 1);
		addRegister(319, 1, true);
		addRegister(320, 1);
		addRegister(321, 1);
		addRegister(322, 1);
		addRegister(323, 1);
		addRegister(324, 2);
		addRegister(325, 2);
		addRegister(326, 2);
		addRegister(327, 2);
		addRegister(328, 2);
		addRegister(330, 1);
		addRegister(331, 1);
		addRegister(332, 1);
		addRegister(333, 1);
		addRegister(390, 2);
		addRegister(391, 2);
		addRegister(392, 2);
		addRegister(393, 2);
		addRegister(394, 4);
		addRegister(395, 4);
		addRegister(396, 4);
		addRegister(397, 4);
		addRegister(303, 1);
		addRegister(312, 4);
		addRegister(313, 2);
		addRegister(314, 1);
		addRegister(307, 2);
		addRegister(317, 1);
	}

	private static void addRegister(int number, int size) {
		addRegister(number, size, false);
	}

	private static void addRegister(int number, int size, boolean signed) {
		registers.put(number, new Register(number, size, signed));
	}

	public ThingDataMessage decodeUplink(byte[] payload) {
		ByteParser parser = new ByteParser(payload);

		int code = parser.uint8();

		BaseFrame frame = null;
		switch(code) {
		case 0x10 :
			frame = parseUplinkFrame0x10(parser);
			break;
		case 0x20 :
			frame = parseUplinkFrame0x20(parser);
			break;
		case 0x2F :
			frame = parseUplinkFrame0x2F(parser);
			break;
		case 0x30 :
			frame = parseUplinkFrame0x30(parser);
			break;
/*
		case 0x31 :
			frame = parseUplinkFrame0x31(parser);
			break;
*/
		case 0x33 :
			frame = parseUplinkFrame0x33(parser);
			break;
		case 0x40 :
			frame = parseUplinkFrame0x40(parser);
			break;
		case 0x59 :
			frame = parseUplinkFrame0x59(parser);
			break;
		default:
			throw new RuntimeException("Unknown frame code: " + code);
		}

		return frame;
	}

	public long parseEpoch2013(ByteParser parser) {
		return (parser.uint32BI() + TIMESTAMP_1_1_1970) * 1000;
	}

	public ChannelConfiguration parseChannelConfiguration(ByteParser parser) {
		var channelType = ChannelType.parse( parser.uint(4));
		var debounceDuration = parseDebounceDuration(parser.uint(4));
		return new ChannelConfiguration(channelType, debounceDuration);
	}

	private Integer parseDebounceDuration(int code) {
		switch(code) {
			case 0x0: return null;
			case 0x1: return 10;
			case 0x2: return 20;
			case 0x3: return 50;
			case 0x4: return 100;
			case 0x5: return 200;
			case 0x6: return 500;
			case 0x7: return 1*1000;
			case 0x8: return 2*1000;
			case 0x9: return 5*1000;
			case 0xA: return 10*1000;
			case 0xB: return 20*1000;
			case 0xC: return 40*1000;
			case 0xD: return 60*1000;
			case 0xE: return 5*60*1000;
			case 0xF: return 10*60*1000;		
			default: throw new RuntimeException("Invalid DebounceDuration: " + code);
		}
	}


	public Status parseStatus(ByteParser parser) {
		Status status = new Status();

		status.setConfig(parser.bit() == 1);
		status.setLowBat(parser.bit() == 1);
		status.setTimestamp(parser.bit() == 1);
		parser.skipBits(2);
		status.setFrameCounter(parser.uint(3));

		return status;
	}

	public UplinkFrame0x10 parseUplinkFrame0x10(ByteParser parser) {

		var frame = new UplinkFrame0x10();

		frame.setStatus(parseStatus(parser));
		frame.setS300_TransmissionPeriodOfKeepAlive10sec(parser.uintBI(16));
		frame.setS301_TransmissionPeriodOfPeriodicFrame10sec(parser.uintBI(16));

		frame.setS320_Channel1Configuration(parseChannelConfiguration(parser));
		frame.setS321_Channel2Configuration(parseChannelConfiguration(parser));
		frame.setS322_Channel3Configuration(parseChannelConfiguration(parser));
		frame.setS323_Channel4Configuration(parseChannelConfiguration(parser));

		parser.assertEmpty();

		return frame;
	}

	public UplinkFrame0x20 parseUplinkFrame0x20(ByteParser parser) {

		var frame = new UplinkFrame0x20();

		frame.setStatus(parseStatus(parser));

		frame.setS220_lorawanOptions(parseLorawanOptions(parser));
		frame.setS221_ProvisioningMode(ProvisioningMode.parse(parser.uint(8)));

		parser.assertEmpty();

		return frame;
	}


	private LoraWanOptions parseLorawanOptions(ByteParser parser) {

		var loraWanOptions = new LoraWanOptions();
		loraWanOptions.setActivatedByADR(parser.bit() == 1);
		parser.skipBits(1);
		loraWanOptions.setDutyCycle(parser.bit() == 1);
		parser.skipBits(2);
		loraWanOptions.setClassA(parser.bit() == 0);
		parser.skipBits(2);

		return loraWanOptions;
	}

	public UplinkFrame0x2F parseUplinkFrame0x2F(ByteParser parser) {

		var frame = new UplinkFrame0x2F();

		frame.setStatus(parseStatus(parser));

		frame.setDownlinkFrameCode(parser.uint(8));
		frame.setRequestStatus(Request2FStatus.parse(parser.uint(8)));
		
		parser.assertEmpty();

		return frame;
	}

	public UplinkFrame0x30 parseUplinkFrame0x30(ByteParser parser) {

		var frame = new UplinkFrame0x30();

		frame.setStatus(parseStatus(parser));

		frame.setChannel1EventCounter(parser.uint16BI());
		frame.setChannel2EventCounter(parser.uint16BI());
		frame.setChannel3EventCounter(parser.uint16BI());
		frame.setChannel4EventCounter(parser.uint16BI());

		frame.setChannel1State(parser.bit() == 1? ChannelState.CLOSED_ON: ChannelState.OPEN_OFF);
		frame.setChannel2State(parser.bit() == 1? ChannelState.CLOSED_ON: ChannelState.OPEN_OFF);
		frame.setChannel3State(parser.bit() == 1? ChannelState.CLOSED_ON: ChannelState.OPEN_OFF);
		frame.setChannel4State(parser.bit() == 1? ChannelState.CLOSED_ON: ChannelState.OPEN_OFF);

		parser.skipBits(4);

		if(frame.getStatus().isTimestamp()) frame.setTimestamp(parseEpoch2013(parser));

		parser.assertEmpty();

		return frame;
	}

	public UplinkFrame0x40 parseUplinkFrame0x40(ByteParser parser) {

		var frame = new UplinkFrame0x40();

		frame.setStatus(parseStatus(parser));

		frame.setChannel1EventCounter(parser.uint16BI());
		frame.setChannel2EventCounter(parser.uint16BI());
		frame.setChannel3EventCounter(parser.uint16BI());
		frame.setChannel4EventCounter(parser.uint16BI());

		frame.setChannel1CurrentState(parser.bit() == 1? ChannelState.CLOSED_ON: ChannelState.OPEN_OFF);
		frame.setChannel1PreviousFrameState(parser.bit() == 1? ChannelState.CLOSED_ON: ChannelState.OPEN_OFF);
		frame.setChannel2CurrentState(parser.bit() == 1? ChannelState.CLOSED_ON: ChannelState.OPEN_OFF);
		frame.setChannel2PreviousFrameState(parser.bit() == 1? ChannelState.CLOSED_ON: ChannelState.OPEN_OFF);
		frame.setChannel3CurrentState(parser.bit() == 1? ChannelState.CLOSED_ON: ChannelState.OPEN_OFF);
		frame.setChannel3PreviousFrameState(parser.bit() == 1? ChannelState.CLOSED_ON: ChannelState.OPEN_OFF);
		frame.setChannel4CurrentState(parser.bit() == 1? ChannelState.CLOSED_ON: ChannelState.OPEN_OFF);
		frame.setChannel4PreviousFrameState(parser.bit() == 1? ChannelState.CLOSED_ON: ChannelState.OPEN_OFF);

		if(frame.getStatus().isTimestamp()) frame.setTimestamp(parseEpoch2013(parser));

		parser.assertEmpty();

		return frame;
	}

	public UplinkFrame0x59 parseUplinkFrame0x59(ByteParser parser) {

		var frame = new UplinkFrame0x59();

		frame.setStatus(parseStatus(parser));
		var hasTimeCounter1 = parser.bit() == 1;
		var hasTimeCounter2 = parser.bit() == 1;
		var hasTimeCounter3 = parser.bit() == 1;
		var hasTimeCounter4 = parser.bit() == 1;

		parser.skipBits(4);

		if(hasTimeCounter1) frame.setChannel1TimeCounter(parser.uint32BI());
		if(hasTimeCounter2) frame.setChannel2TimeCounter(parser.uint32BI());
		if(hasTimeCounter3) frame.setChannel3TimeCounter(parser.uint32BI());
		if(hasTimeCounter4) frame.setChannel4TimeCounter(parser.uint32BI());

		if(frame.getStatus().isTimestamp()) frame.setTimestamp(parseEpoch2013(parser));

		parser.assertEmpty();

		return frame;
	}

	public UplinkFrame0x31 parseUplinkFrame0x31(ByteParser parser, DownlinkFrame0x40 downlink) {

		var frame = new UplinkFrame0x31();

		frame.setStatus(parseStatus(parser));

		for(var registerNumber: downlink.registerNumbers()) {
			var registerDescriptor = registers.get(registerNumber);
			if(registerDescriptor == null) throw new RuntimeException("Register not found");

			RegisterValue registerValue;
			if(  registerDescriptor.getSize() <= 2 )
				registerValue = new RegisterValue(registerDescriptor.getNumber(), parser.suint(registerDescriptor.getSizeBits(), ByteOrder.BI, registerDescriptor.isSigned()));
			else
				registerValue = new RegisterValue(registerDescriptor.getNumber(), parser.sulong(registerDescriptor.getSizeBits(), ByteOrder.BI, registerDescriptor.isSigned()));

			frame.addValue(registerValue);
		}

		return frame;
	}
	public UplinkFrame0x33 parseUplinkFrame0x33(ByteParser parser) {

		var frame = new UplinkFrame0x33();

		frame.setStatus(parseStatus(parser));
		frame.setRequestStatus(Request33Status.parse(parser.uint8()));

		frame.setRegsiterIdIfError(parser.suint(16, ByteOrder.BI, false));

		return frame;
	}

	public byte[] encodeDownlinkFrame0x01(DownlinkFrame0x01 frame) {
		return new byte[] {0x01};
	}

	public byte[] encodeDownlinkFrame0x02(DownlinkFrame0x02 frame) {
		return new byte[] {0x02};
	}

	public byte[] encodeDownlinkFrame0x05(DownlinkFrame0x05 frame) {
		return new byte[] {0x05};
	}

	public byte[] encodeDownlinkFrame0x07(DownlinkFrame0x07 frame) {
		return new byte[] {
				0x07, 
				(byte)frame.getChannel1PulseDuration(),
				(byte)frame.getChannel2PulseDuration(),
				(byte)frame.getChannel3PulseDuration(),
				(byte)frame.getChannel4PulseDuration(),
				(byte)(frame.isAckRequired()?1: 0)
		};
	}

	public byte[] encodeDownlinkFrame0x40(DownlinkFrame0x40 frame) {
		var bytes = new byte[frame.registerNumbers().size() + 1];
		bytes[0] = 0x40;
		int b = 1;
		for(var registerNumber: frame.registerNumbers()) {
			bytes[b++] = (byte)(registerNumber.intValue() - 300);	// Confid
		}

		return bytes;
	}

	public byte[] encodeDownlinkFrame0x41(DownlinkFrame0x41 frame) {
		int bytesSize = 1;
		for(var registerValue: frame.getRegisterValues()) {
			var registerDescriptor = registers.get(registerValue.getRegisterNumber());
			bytesSize += registerDescriptor.getSize();
		}

		var bytes = new byte[bytesSize];

		bytes[0] = 0x41;
		int b = 1;
		for(var registerValue: frame.getRegisterValues()) {
			var registerDescriptor = registers.get(registerValue.getRegisterNumber());

			long value;
			if(registerDescriptor.getSize() <= 2)
				value = registerValue.getAsInt().intValue();
			else
				value = registerValue.getAsLong().longValue();

			// Big endian
			for(int s = registerDescriptor.getSize()-1; s >= 0 ; s--) {
				bytes[b++] = (byte)((value >> (s*8)) & 0xFF);
			}
		}

		return bytes;
	}

	public byte[] encodeDownlinkFrame0x48(DownlinkFrame0x48 frame) {
		return new byte[] {
					0x48,
					(byte)((frame.getMinutesBeforeReboot() >> 8)&0xFF),
					(byte)(frame.getMinutesBeforeReboot()&0xFF),
				};
	}

	public byte[] encodeDownlinkFrame0x49(DownlinkFrame0x49 frame) {
		var bytes = new byte[6];
		bytes[0] = 0x49;
		if(frame.getTimestamp() == null) {
			bytes[1] = (byte)0xFF;
			bytes[2] = (byte)0xFF;
			bytes[3] = (byte)0xFF;
			bytes[4] = (byte)0xFF;
		} else {
			var timestamp = frame.getTimestamp().longValue() - TIMESTAMP_1_1_1970;
			bytes[1] = (byte)((timestamp >> 24)&0xFF);
			bytes[2] = (byte)((timestamp >> 16)&0xFF);
			bytes[3] = (byte)((timestamp >> 8)&0xFF);
			bytes[4] = (byte)((timestamp)&0xFF);
		};
		
		if( frame.getClockDriftCompensation() == null)
			bytes[5] = (byte)0x80;
		else
			bytes[5] = (byte)frame.getClockDriftCompensation().intValue();
		
		return bytes;
	}

	public JsonObject normalize(Object message) {
		return null;
	}
}
