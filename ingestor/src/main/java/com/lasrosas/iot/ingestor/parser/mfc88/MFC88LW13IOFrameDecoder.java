package com.lasrosas.iot.ingestor.parser.mfc88;

import java.time.LocalDateTime;

import com.lasrosas.iot.ingestor.ThingMessageHolder;
import com.lasrosas.iot.ingestor.parser.mfc88.MFC88LW13IOFrame.BaseFrame;
import com.lasrosas.iot.ingestor.parser.mfc88.MFC88LW13IOFrame.DownlinkIOMessage;
import com.lasrosas.iot.ingestor.parser.mfc88.MFC88LW13IOFrame.DownlinkOffCommand;
import com.lasrosas.iot.ingestor.parser.mfc88.MFC88LW13IOFrame.DownlinkSetPeriod;
import com.lasrosas.iot.ingestor.parser.mfc88.MFC88LW13IOFrame.DownlinkTimeSyncAnswer;
import com.lasrosas.iot.ingestor.parser.mfc88.MFC88LW13IOFrame.UplinkDigitalData;
import com.lasrosas.iot.ingestor.parser.mfc88.MFC88LW13IOFrame.UplinkIO;
import com.lasrosas.iot.ingestor.parser.mfc88.MFC88LW13IOFrame.UplinkLengthError;
import com.lasrosas.iot.ingestor.parser.mfc88.MFC88LW13IOFrame.UplinkTimeSyncRequest;
import com.lasrosas.iot.ingestor.parser.mfc88.MFC88LW13IOFrame.UplinkTimeSyncRequest.UplinkTimeSyncRequestOption;
import com.lasrosas.iot.shared.utils.ByteParser;

public class MFC88LW13IOFrameDecoder {

	public ThingMessageHolder decode(byte[] payload) {
		ByteParser parser = new ByteParser(payload);

		int code = parser.uint8();

		BaseFrame frame = null;
		switch(code) {
		case UplinkTimeSyncRequest.CODE :
			frame = parseUplinkTimeSyncRequest(parser);
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

	public UplinkTimeSyncRequest parseUplinkTimeSyncRequest(ByteParser parser) {
		var frame = new UplinkTimeSyncRequest();

		frame.setSyncId(parser.uint32BI());
		frame.setVersion(parseVersion(parser));
		frame.setApplicationType(parser.uint16BI());
		frame.setOption(parser.uint8() == 1 ? UplinkTimeSyncRequestOption.AFTER_BOOT: UplinkTimeSyncRequestOption.GOING_DOWN);
		frame.setRfu(parser.bytes());

		return frame;
	}

	private MFC88Version parseVersion(ByteParser parser) {
		int major = parser.uint8();
		int minor = parser.uint8();
		int build = parser.uint8();

		return new MFC88Version(major, minor, build);
	}

	public LocalDateTime parseDateTime(ByteParser parser) {
	 	var bytes = parser.bytes(4);

		// Reverse bytes MSB on the right
		var swap = bytes[0];
		bytes[0] = bytes[3];
		bytes[3] = swap;
		swap = bytes[2];
		bytes[2] = bytes[1];
		bytes[1] = swap;

		var year = 2000 + (bytes[0] >> 1);
		var month = ((bytes[0] & 1) << 3) | ((bytes[1]) >> 5);
		var dayOfMonth = bytes[1] & 0x1F;
		var houres= (bytes[2] >> 3)& 0x1F;
		var minutes = ((bytes[2] & 0x7) << 3) | ((bytes[3] >> 5) & 0x7);
		var seconds = 2 * (bytes[3] & 0x1F);

		return LocalDateTime.of(year, month, dayOfMonth, houres, minutes, seconds);
	}

	public UplinkIO parseUplinkIO(ByteParser parser) {
		var frame = new UplinkIO();

		frame.setDateTime(parseDateTime(parser));
		frame.setInputs(parser.uint(4));
		frame.setOutputs(parser.uint(4));
		frame.setEvents(parser.uint(4));

		return frame;
	}

	public UplinkDigitalData parseUplinkDigitlData(ByteParser parser) {
		var frame = new UplinkDigitalData();
		return frame;
	}

	public UplinkLengthError parseUplinkLengthError(ByteParser parser) {
		var frame = new UplinkLengthError();
		return frame;
	}
	public DownlinkTimeSyncAnswer parseDownlinkTimeSyncAnswer(ByteParser parser) {
		var frame = new DownlinkTimeSyncAnswer();
		return frame;
	}
	public DownlinkIOMessage parseDownlinkIOMessage(ByteParser parser) {
		var frame = new DownlinkIOMessage();
		return frame;
	}
	public DownlinkSetPeriod parseDownlinkSetPeriod(ByteParser parser) {
		var frame = new DownlinkSetPeriod();
		return frame;
	}
	public DownlinkOffCommand parseDownlinkOffCommand(ByteParser parser) {
		var frame = new DownlinkOffCommand();
		return frame;
	}
}
