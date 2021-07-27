package com.lasrosas.iot.ingestor.services.sensors.impl.mfc88;

import java.time.LocalDateTime;

import com.lasrosas.iot.ingestor.ThingMessageHolder;
import com.lasrosas.iot.ingestor.services.sensors.impl.mfc88.MFC88LW13IOFrame.BaseFrame;
import com.lasrosas.iot.ingestor.services.sensors.impl.mfc88.MFC88LW13IOFrame.DownlinkFrame;
import com.lasrosas.iot.ingestor.services.sensors.impl.mfc88.MFC88LW13IOFrame.DownlinkIOMessage;
import com.lasrosas.iot.ingestor.services.sensors.impl.mfc88.MFC88LW13IOFrame.DownlinkOffCommand;
import com.lasrosas.iot.ingestor.services.sensors.impl.mfc88.MFC88LW13IOFrame.DownlinkSetPeriod;
import com.lasrosas.iot.ingestor.services.sensors.impl.mfc88.MFC88LW13IOFrame.DownlinkTimeSyncAnswer;
import com.lasrosas.iot.ingestor.services.sensors.impl.mfc88.MFC88LW13IOFrame.UplinkDigitalData;
import com.lasrosas.iot.ingestor.services.sensors.impl.mfc88.MFC88LW13IOFrame.UplinkIO;
import com.lasrosas.iot.ingestor.services.sensors.impl.mfc88.MFC88LW13IOFrame.UplinkInputCounterType1;
import com.lasrosas.iot.ingestor.services.sensors.impl.mfc88.MFC88LW13IOFrame.UplinkLengthError;
import com.lasrosas.iot.ingestor.services.sensors.impl.mfc88.MFC88LW13IOFrame.UplinkTimeSyncRequest;
import com.lasrosas.iot.ingestor.services.sensors.impl.mfc88.MFC88LW13IOFrame.UplinkTimeSyncRequest.UplinkTimeSyncRequestOption;
import com.lasrosas.iot.shared.utils.ByteParser;

public class MFC88LW13IOFrameDecoder {

	public ThingMessageHolder decodeUplink(byte[] payload) {
		ByteParser parser = new ByteParser(payload);

		int code = parser.uint8();

		BaseFrame frame = null;
		switch(code) {
		case UplinkTimeSyncRequest.CODE :
			frame = decodeUplinkTimeSyncRequest(parser);
			break;
		case UplinkIO.CODE :
			frame = decodeUplinkIO(parser);
			break;
		default:
			throw new RuntimeException("Unknown frame code: " + code);
		}

		return new ThingMessageHolder("MFC88.Temp.frame." + frame.getClass().getSimpleName(), null, frame);		
	}

	public static class MFC88Version {
		private int major;
		private int minor;
		private int build;

		public MFC88Version(int major, int minor, int build) {
			super();
			this.major = major;
			this.minor = minor;
			this.build = build;
		}

		public int getMajor() {
			return major;
		}
		public int getMinor() {
			return minor;
		}
		public int getBuild() {
			return build;
		}
	}

	public UplinkTimeSyncRequest decodeUplinkTimeSyncRequest(ByteParser parser) {
		var frame = new UplinkTimeSyncRequest();

		frame.setSyncId(parser.uint32BI());
		frame.setVersion(decodeVersion(parser));
		frame.setApplicationType(parser.uint16BI());
		frame.setOption(parser.uint8() == 1 ? UplinkTimeSyncRequestOption.AFTER_BOOT: UplinkTimeSyncRequestOption.GOING_DOWN);
		frame.setRfu(parser.bytes());

		return frame;
	}

	private MFC88Version decodeVersion(ByteParser parser) {
		var major = parser.uint8();
		var minor = parser.uint8();
		var build = parser.uint8();

		return new MFC88Version(major, minor, build);
	}

	public LocalDateTime decodeDateTime(ByteParser parser) {
		var bytes = new int [] {parser.uint8(), parser.uint8(), parser.uint8(), parser.uint8()};

		// Reverse bytes MSB on the right
		var swap = bytes[0];
		bytes[0] = bytes[3];
		bytes[3] = swap;
		swap = bytes[2];
		bytes[2] = bytes[1];
		bytes[1] = swap;

		var year = 2000 + (bytes[0] >> 1);
		var month = ((bytes[0] & 1) << 3) | (((bytes[1]) >> 5)&0x1f);
		var dayOfMonth = bytes[1] & 0x1F;
		var houres= (bytes[2] >> 3)& 0x1F;
		var minutes = ((bytes[2] & 0x7) << 3) | ((bytes[3] >> 5) & 0x7);
		var seconds = 2 * (bytes[3] & 0x1F);

		return LocalDateTime.of(year, month, dayOfMonth, houres, minutes, seconds);
	}

	public UplinkIO decodeUplinkIO(ByteParser parser) {
		var frame = new UplinkIO();

		frame.setDateTime(decodeDateTime(parser));
		frame.setInputs(parser.uint(4));
		frame.setOutputs(parser.uint(4));
		frame.setEvents(parser.uint(4));

		return frame;
	}

	public UplinkDigitalData decodeUplinkDigitlData(ByteParser parser) {
		var type = parser.uint8();

		switch(type) {
		case 00: 
			var frame = new UplinkInputCounterType1();
			
			var inputCounters = new int[16];
			for(var i = 0; i < 16; i++) {
				inputCounters[i] = parser.uint16BI();
			}
			frame.setInputCounters(inputCounters);
			return frame;

		case 01: case 02:
			throw new RuntimeException("Digital data type " + type + " is not supported. Fix the code.");
		default:
			throw new RuntimeException("Invalid DigitalData type " + type);
		}
	}

	public UplinkLengthError decodeUplinkLengthError(ByteParser parser) {
		var frame = new UplinkLengthError();
		frame.setType(parser.uint8());
		frame.setData(parser.bytes(9));

		return frame;
	}

	public byte[] encodeDownlink(DownlinkFrame frame) {
		switch(frame.getCode()) {
		case DownlinkTimeSyncAnswer.CODE:
			return encodeDownlinkTimeSyncAnswer((DownlinkTimeSyncAnswer)frame);
		case DownlinkIOMessage.CODE:
			return encodeDownlinkIOMessage((DownlinkIOMessage)frame);
		case DownlinkSetPeriod.CODE:
			return encodeDownlinkSetPeriod((DownlinkSetPeriod)frame);
		case DownlinkOffCommand.CODE:
			return encodeDownlinkOffCommand((DownlinkOffCommand)frame);
		default:
			throw new RuntimeException("Downlink frame code not supported: " + frame.getCode());
		}
	}

	public byte[] encodeDownlinkTimeSyncAnswer(DownlinkTimeSyncAnswer frame) {
		return null;
	}

	public byte[] encodeDownlinkIOMessage(DownlinkIOMessage frame) {
		return null;
	}

	public byte[] encodeDownlinkSetPeriod(DownlinkSetPeriod frame) {
		return null;
	}

	public byte[] encodeDownlinkOffCommand(DownlinkOffCommand frame) {
		return null;
	}
}
