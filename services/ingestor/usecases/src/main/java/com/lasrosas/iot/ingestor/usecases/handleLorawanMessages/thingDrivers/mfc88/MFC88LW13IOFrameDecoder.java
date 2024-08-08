package com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.mfc88;

import com.lasrosas.iot.ingestor.domain.model.message.BaseMessage;
import com.lasrosas.iot.ingestor.shared.ByteParser;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.LorawanMessageUplinkRx;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

public class MFC88LW13IOFrameDecoder {

	public BaseMessage decodeUplink(LorawanMessageUplinkRx uplink) {
		ByteParser parser = new ByteParser(uplink.decodeData());

		int code = parser.uint8();

		MFC88LW13IOFrame.BaseFrame frame = null;
		switch(code) {
		case MFC88LW13IOFrame.UplinkTimeSyncRequest.CODE :
			frame = decodeUplinkTimeSyncRequest(parser);
			break;
		case MFC88LW13IOFrame.UplinkIO.CODE :
			frame = decodeUplinkIO(parser);
			break;
		default:
			throw new RuntimeException("Unknown frame code: " + code);
		}

		frame.setOrigin(uplink);
		return frame;
	}

	@Setter
	@Getter
	@SuperBuilder
	@NoArgsConstructor
	public static class MFC88Version {
		private int major;
		private int minor;
		private int build;
	}

	public MFC88LW13IOFrame.UplinkTimeSyncRequest decodeUplinkTimeSyncRequest(ByteParser parser) {

		var f = MFC88LW13IOFrame.UplinkTimeSyncRequest.builder().build();
		var f2 = new MFC88LW13IOFrame.UplinkTimeSyncRequest();

		return MFC88LW13IOFrame.UplinkTimeSyncRequest.builder()
				.syncId(parser.uint32BI())
				.version(decodeVersion(parser))
				.applicationType(parser.uint16BI())
				.option(
						switch(parser.uint8()) {
							case 1 -> MFC88LW13IOFrame.UplinkTimeSyncRequest.UplinkTimeSyncRequestOption.AFTER_BOOT;
							case 2 -> MFC88LW13IOFrame.UplinkTimeSyncRequest.UplinkTimeSyncRequestOption.GOING_DOWN;
							default -> null;
						}
				)
				.rfu(parser.bytes())
				.build();
	}

	private MFC88Version decodeVersion(ByteParser parser) {

		return MFC88Version.builder()
						.major(parser.uint8())
						.minor(parser.uint8())
						.build(parser.uint8())
						.build();
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
		var hours = (bytes[2] >> 3)& 0x1F;
		var minutes = ((bytes[2] & 0x7) << 3) | ((bytes[3] >> 5) & 0x7);
		var seconds = 2 * (bytes[3] & 0x1F);

		return LocalDateTime.of(year, month, dayOfMonth, hours, minutes, seconds);
	}

	public MFC88LW13IOFrame.UplinkIO decodeUplinkIO(ByteParser parser) {

		return MFC88LW13IOFrame.UplinkIO.builder()
						.dateTime(decodeDateTime(parser))
						.inputs(parser.uint32LI())
						.outputs(parser.uint32LI())
						.events(parser.uint32LI())
						.build();
	}

	public MFC88LW13IOFrame.UplinkDigitalData decodeUplinkDigitlData(ByteParser parser) {
		var type = parser.uint8();

		return switch(type) {
		case 0 -> {
			var inputCounters = new int[16];
			for (var i = 0; i < 16; i++) {
				inputCounters[i] = parser.uint16BI();
			}

			yield MFC88LW13IOFrame.UplinkInputCounterType1.builder()
					.inputCounters(inputCounters)
					.build();
		}
		case 1, 2->
			throw new RuntimeException("Digital data type " + type + " is not supported. Fix the code.");
		default ->
			throw new RuntimeException("Invalid DigitalData type " + type);
		};
	}

	public MFC88LW13IOFrame.UplinkLengthError decodeUplinkLengthError(ByteParser parser) {

		return MFC88LW13IOFrame.UplinkLengthError.builder()
					.type(parser.uint8())
					.data(parser.bytes(9))
					.build();
	}
/*
	public byte[] encodeOrder(Order order) {

		if( order instanceof MultiSwitchOrder) {
			var onOffOrder = (MultiSwitchOrder)order;
			var downlink = new DownlinkIOMessage();
			var channel = Integer.parseInt(onOffOrder.getPart());
			downlink.requestChannelStateChange(channel, onOffOrder.getState() == 1);
			return encodeDownlinkIOMessage(downlink);

		} else if( order instanceof RestartOrder) {
			return encodeDownlinkOffCommand(new DownlinkOffCommand());

		} else
			throw new RuntimeException("Order type " + order.getClass().getSimpleName() + "  not supported");
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
		var bytes = new byte[10];

		bytes[0] = 0x04;		// Downlink ID
		bytes[1] = 0x00;		// option

		for(var channel = 0; channel < 4; channel++) {
			bytes[channel + 2] = (byte)(frame.isToBeEnabled(channel)? 1: 0);
			bytes[channel + 2 + 4] = (byte)(frame.isToBeDisabled(channel)? 1: 0);
		}

		return bytes;
	}

	public byte[] encodeDownlinkSetPeriod(DownlinkSetPeriod frame) {
		return null;
	}

	public byte[] encodeDownlinkOffCommand(DownlinkOffCommand frame) {
		return new byte[] {0x04, (byte)0xFF, 0x00};
	}
 */

}
