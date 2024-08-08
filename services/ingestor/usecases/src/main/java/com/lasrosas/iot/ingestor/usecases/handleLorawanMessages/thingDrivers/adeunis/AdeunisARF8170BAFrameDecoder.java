package com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.adeunis;

import com.lasrosas.iot.ingestor.domain.model.message.BaseMessage;
import com.lasrosas.iot.ingestor.shared.ByteParser;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.LorawanMessageUplinkRx;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.adeunis.AdeunisARF8170BAFrame.*;
import static com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.adeunis.AdeunisARF8170BAFrame.ChannelState.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.Map;

public class AdeunisARF8170BAFrameDecoder {

	public static final long TIMESTAMP_1_1_1970 = 1356998400;

	@Setter
	@Getter
	@SuperBuilder
	public static class Register {
		private int number;
		private int size;
		private boolean signed;

		public int getSizeBits() {
			return size * 8;
		}
	}

	public static Map<Integer, Register> registers = new HashMap<>();

	static {
		addRegister(304, 2, false);
		addRegister(306, 1, false);
		addRegister(300, 2, false);
		addRegister(301, 2, false);
		addRegister(308, 4, false);
		addRegister(315, 1, true);
		addRegister(316, 1, false);
		addRegister(318, 1, false);
		addRegister(319, 1, true);
		addRegister(320, 1, false);
		addRegister(321, 1, false);
		addRegister(322, 1, false);
		addRegister(323, 1, false);
		addRegister(324, 2, false);
		addRegister(325, 2, false);
		addRegister(326, 2, false);
		addRegister(327, 2, false);
		addRegister(328, 2, false);
		addRegister(330, 1, false);
		addRegister(331, 1, false);
		addRegister(332, 1, false);
		addRegister(333, 1, false);
		addRegister(390, 2, false);
		addRegister(391, 2, false);
		addRegister(392, 2, false);
		addRegister(393, 2, false);
		addRegister(394, 4, false);
		addRegister(395, 4, false);
		addRegister(396, 4, false);
		addRegister(397, 4, false);
		addRegister(303, 1, false);
		addRegister(312, 4, false);
		addRegister(313, 2, false);
		addRegister(314, 1, false);
		addRegister(307, 2, false);
		addRegister(317, 1, false);
	}

	private static void addRegister(int number, int size, boolean signed) {
		var register = Register.builder().number(number).size(size).signed(signed).build();
		registers.put(number, register);
	}

	public BaseMessage decodeUplink(LorawanMessageUplinkRx uplink) {
		ByteParser parser = new ByteParser(uplink.decodeData());
		int code = parser.uint8();

		AdeunisARF8170BAFrame.BaseFrame frame =
			switch(code) {
				case 0x10 -> parseUplinkFrame0x10(parser);
				case 0x20 -> parseUplinkFrame0x20(parser);
				case 0x2F -> parseUplinkFrame0x2F(parser);
				case 0x30 -> parseUplinkFrame0x30(parser);
		/*
				case 0x31 :
					frame = parseUplinkFrame0x31(parser);
					break;
		*/
				case 0x33 -> parseUplinkFrame0x33(parser);
				case 0x40 -> parseUplinkFrame0x40(parser);
				case 0x59 -> parseUplinkFrame0x59(parser);
				default -> throw new RuntimeException("Unknown frame code: " + code);
			};

		frame.setOrigin(uplink);

		return frame;
	}

	public long parseEpoch2013(ByteParser parser) {
		return (parser.uint32BI() + TIMESTAMP_1_1_1970) * 1000;
	}

	public ChannelConfiguration parseChannelConfiguration(ByteParser parser) {
		var channelType = ChannelType.parse( parser.uint(4));
		var debounceDuration = parseDebounceDuration(parser.uint(4));
		return ChannelConfiguration.builder()
						.type(channelType)
						.debounceDurationMs(debounceDuration)
						.build();
	}

	private Integer parseDebounceDuration(int code) {
		return switch(code) {
			case 0x0 -> null;
			case 0x1-> 10;
			case 0x2-> 20;
			case 0x3-> 50;
			case 0x4-> 100;
			case 0x5-> 200;
			case 0x6-> 500;
			case 0x7-> 1*1000;
			case 0x8-> 2*1000;
			case 0x9-> 5*1000;
			case 0xA-> 10*1000;
			case 0xB-> 20*1000;
			case 0xC-> 40*1000;
			case 0xD-> 60*1000;
			case 0xE-> 5*60*1000;
			case 0xF-> 10*60*1000;
			default -> throw new RuntimeException("Invalid DebounceDuration: " + code);
		};
	}

	public Status parseStatus(ByteParser parser) {
		return Status.builder()
						.config(parser.bit() == 1)
						.lowBat(parser.bit() == 1)
						.timestamp(parser.bit() == 1)
						.frameCounter(parser.skipBits(2).uint(3))
						.build();
	}

	public AdeunisARF8170BAFrame.UplinkFrame0x10 parseUplinkFrame0x10(ByteParser parser) {

		var frame = AdeunisARF8170BAFrame.UplinkFrame0x10.builder()
						.status(parseStatus(parser))
						.S300_TransmissionPeriodOfKeepAlive10sec(parser.uintBI(16))
						.S301_TransmissionPeriodOfPeriodicFrame10sec(parser.uintBI(16))
						.S320_Channel1Configuration(parseChannelConfiguration(parser))
						.S321_Channel2Configuration(parseChannelConfiguration(parser))
						.S322_Channel3Configuration(parseChannelConfiguration(parser))
						.S323_Channel4Configuration(parseChannelConfiguration(parser))
						.build();

		parser.assertEmpty();

		return frame;
	}

	public AdeunisARF8170BAFrame.UplinkFrame0x20 parseUplinkFrame0x20(ByteParser parser) {

		var frame = AdeunisARF8170BAFrame.UplinkFrame0x20.builder()
						.status(parseStatus(parser))
						.S220_lorawanOptions(parseLorawanOptions(parser))
						.S221_ProvisioningMode(AdeunisARF8170BAFrame.ProvisioningMode.parse(parser.uint(8)))
						.build();

		parser.assertEmpty();

		return frame;
	}


	private AdeunisARF8170BAFrame.LoraWanOptions parseLorawanOptions(ByteParser parser) {

		var loraWanOptions = LoraWanOptions.builder()
				.activatedByADR(parser.bit() == 1)
				.dutyCycle(parser.skipBits(1).bit() == 1)
				.classA(parser.skipBits(2).bit() == 0)
				.build();

		parser.skipBits(2);

		return loraWanOptions;
	}

	public AdeunisARF8170BAFrame.UplinkFrame0x2F parseUplinkFrame0x2F(ByteParser parser) {

		var frame = AdeunisARF8170BAFrame.UplinkFrame0x2F.builder()
						.status(parseStatus(parser))
						.downlinkFrameCode(parser.uint(8))
						.requestStatus(AdeunisARF8170BAFrame.Request2FStatus.parse(parser.uint(8)))
						.build();
		
		parser.assertEmpty();

		return frame;
	}

	public UplinkFrame0x30 parseUplinkFrame0x30(ByteParser parser) {

		var frame = UplinkFrame0x30.builder()
				.status(parseStatus(parser))
				.channel1EventCounter(parser.uint16BI())
				.channel2EventCounter(parser.uint16BI())
				.channel3EventCounter(parser.uint16BI())
				.channel4EventCounter(parser.uint16BI())
				.channel1State(parser.bit() == 1? CLOSED_ON: OPEN_OFF)
				.channel2State(parser.bit() == 1? CLOSED_ON: OPEN_OFF)
				.channel3State(parser.bit() == 1? CLOSED_ON: OPEN_OFF)
				.channel4State(parser.bit() == 1? CLOSED_ON: OPEN_OFF)
				.build();

		parser.skipBits(4);

		if(frame.getStatus().isTimestamp()) frame.setTimestamp(parseEpoch2013(parser));

		parser.assertEmpty();

		return frame;
	}

	public UplinkFrame0x40 parseUplinkFrame0x40(ByteParser parser) {

		var frame = UplinkFrame0x40.builder()
						.status(parseStatus(parser))
						.channel1EventCounter(parser.uint16BI())
						.channel2EventCounter(parser.uint16BI())
						.channel3EventCounter(parser.uint16BI())
						.channel4EventCounter(parser.uint16BI())
						.channel1CurrentState(		parser.bit() == 1? CLOSED_ON: OPEN_OFF)
						.channel1PreviousFrameState(parser.bit() == 1? CLOSED_ON: OPEN_OFF)
						.channel2CurrentState(		parser.bit() == 1? CLOSED_ON: OPEN_OFF)
						.channel2PreviousFrameState(parser.bit() == 1? CLOSED_ON: OPEN_OFF)
						.channel3CurrentState(		parser.bit() == 1? CLOSED_ON: OPEN_OFF)
						.channel3PreviousFrameState(parser.bit() == 1? CLOSED_ON: OPEN_OFF)
						.channel4CurrentState(		parser.bit() == 1? CLOSED_ON: OPEN_OFF)
						.channel4PreviousFrameState(parser.bit() == 1? CLOSED_ON: OPEN_OFF)
				.build();

		if(frame.getStatus().isTimestamp()) frame.setTimestamp(parseEpoch2013(parser));

		parser.assertEmpty();

		return frame;
	}

	public UplinkFrame0x59 parseUplinkFrame0x59(ByteParser parser) {

		var frame = UplinkFrame0x59.builder().status(parseStatus(parser)).build();

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

		var frame = UplinkFrame0x31.builder().status(parseStatus(parser)).build();

		for(var registerNumber: downlink.getRegisterNumbers()) {
			var registerDescriptor = registers.get(registerNumber);
			if(registerDescriptor == null) throw new RuntimeException("Register not found");

			RegisterValue registerValue;
			if(  registerDescriptor.getSize() <= 2 )
				registerValue = new RegisterValue(registerDescriptor.getNumber(), parser.suint(registerDescriptor.getSizeBits(), ByteParser.ByteOrder.BI, registerDescriptor.isSigned()));
			else
				registerValue = new RegisterValue(registerDescriptor.getNumber(), parser.sulong(registerDescriptor.getSizeBits(), ByteParser.ByteOrder.BI, registerDescriptor.isSigned()));

			frame.getValues().add(registerValue);
		}

		return frame;
	}
	public UplinkFrame0x33 parseUplinkFrame0x33(ByteParser parser) {

		return  UplinkFrame0x33.builder()
						.status(parseStatus(parser))
						.requestStatus(Request33Status.parse(parser.uint8()))
						.regsiterIdIfError(parser.suint(16, ByteParser.ByteOrder.BI, false))
				.build();
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
		var bytes = new byte[frame.getRegisterNumbers().size() + 1];
		bytes[0] = 0x40;
		int b = 1;
		for(var registerNumber: frame.getRegisterNumbers()) {
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
}
