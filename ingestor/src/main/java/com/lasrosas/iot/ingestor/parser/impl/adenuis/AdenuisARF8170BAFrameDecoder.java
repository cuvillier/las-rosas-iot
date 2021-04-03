package com.lasrosas.iot.ingestor.parser.impl.adenuis;

import com.google.gson.JsonObject;
import com.lasrosas.iot.ingestor.ThingMessageHolder;
import com.lasrosas.iot.ingestor.parser.impl.adenuis.AdenuisARF8170BAFrame.BaseFrame;
import com.lasrosas.iot.ingestor.parser.impl.adenuis.AdenuisARF8170BAFrame.BaseUplinkFrame;
import com.lasrosas.iot.ingestor.parser.impl.adenuis.AdenuisARF8170BAFrame.ChannelConfiguration;
import com.lasrosas.iot.ingestor.parser.impl.adenuis.AdenuisARF8170BAFrame.ChannelState;
import com.lasrosas.iot.ingestor.parser.impl.adenuis.AdenuisARF8170BAFrame.ChannelType;
import com.lasrosas.iot.ingestor.parser.impl.adenuis.AdenuisARF8170BAFrame.LoraWanOptions;
import com.lasrosas.iot.ingestor.parser.impl.adenuis.AdenuisARF8170BAFrame.ProvisioningMode;
import com.lasrosas.iot.ingestor.parser.impl.adenuis.AdenuisARF8170BAFrame.RequestStatus;
import com.lasrosas.iot.ingestor.parser.impl.adenuis.AdenuisARF8170BAFrame.Status;
import com.lasrosas.iot.ingestor.parser.impl.adenuis.AdenuisARF8170BAFrame.UplinkFrame0x10;
import com.lasrosas.iot.ingestor.parser.impl.adenuis.AdenuisARF8170BAFrame.UplinkFrame0x20;
import com.lasrosas.iot.ingestor.parser.impl.adenuis.AdenuisARF8170BAFrame.UplinkFrame0x2F;
import com.lasrosas.iot.ingestor.parser.impl.adenuis.AdenuisARF8170BAFrame.UplinkFrame0x30;
import com.lasrosas.iot.ingestor.parser.impl.adenuis.AdenuisARF8170BAFrame.UplinkFrame0x40;
import com.lasrosas.iot.ingestor.parser.impl.adenuis.AdenuisARF8170BAFrame.UplinkFrame0x59;
import com.lasrosas.iot.shared.utils.ByteParser;

public class AdenuisARF8170BAFrameDecoder {

	private static final long TIMESTAMP_1_1_1970 = 1356998400;

	public ThingMessageHolder decode(byte[] payload) {
		ByteParser parser = new ByteParser(payload);

		int code = parser.uint8();
		Status status = parseStatus(parser);

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
		default:
			throw new RuntimeException("Unknown frame code.");
		}

		if(frame instanceof BaseUplinkFrame ) {
			BaseUplinkFrame uplink = (BaseUplinkFrame)frame;
			uplink.setStatus(status);
		}

		return new ThingMessageHolder("Adenuis.Temp.frame." + frame.getClass().getSimpleName(), null, frame);
	}

	public long parseEpoch2013(ByteParser parser) {
		return (parser.uint32BI() + TIMESTAMP_1_1_1970) * 1000;
	}

	public ChannelConfiguration parseChannelConfiguration(ByteParser parser) {
		var channelType = ChannelType.parse( parser.uint(4));
		var debounceDuration = parseDebounceDuration(parser.uint(4));
		var channelConfiguration = new ChannelConfiguration();

		channelConfiguration.setDebounceDurationMs(debounceDuration);
		channelConfiguration.setType(channelType);

		return channelConfiguration;
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
		frame.setRequestStatus(RequestStatus.parse(parser.uint(8)));
		
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

	public JsonObject normalize(Object message) {
		return null;
	}
}
