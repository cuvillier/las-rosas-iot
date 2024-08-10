package com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.mfc88;

import com.lasrosas.iot.ingestor.domain.message.BaseMessage;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

public class MFC88LW13IOFrame {

	@Setter
	@Getter
	@SuperBuilder
	@NoArgsConstructor
	public abstract static class BaseFrame extends BaseMessage {
		public abstract int getCode();
	}

	@Setter
	@Getter
	@SuperBuilder
	@NoArgsConstructor
	public abstract static class UplinkFrame extends BaseFrame {
	}

	@Setter
	@Getter
	@SuperBuilder
	@NoArgsConstructor
	public abstract static class DownlinkFrame extends BaseFrame {
	}

	@Setter
	@Getter
	@SuperBuilder
	@NoArgsConstructor
	public static class UplinkTimeSyncRequest extends UplinkFrame {
		public static final int CODE = 0x01;
		private long syncId;
		private MFC88LW13IOFrameDecoder.MFC88Version version;
		private int applicationType;

		public static enum UplinkTimeSyncRequestOption {
			GOING_DOWN,
			AFTER_BOOT
		};

		private UplinkTimeSyncRequestOption option;
		private byte[] rfu;
		public int getCode() { return CODE;}
	}

	@Setter
	@Getter
	@SuperBuilder
	@NoArgsConstructor
	public static class UplinkIO extends UplinkFrame {
		public static final int CODE = 0x0A;

		private LocalDateTime dateTime;
		private long inputs;
		private long outputs;
		private long events;

		public int getCode() { return CODE;}
	}

	@Setter
	@Getter
	@SuperBuilder
	@NoArgsConstructor
	public static abstract class UplinkDigitalData extends UplinkFrame {
		public static final int CODE = 0x10;

		public int getCode() { return CODE;}
	}

	@Setter
	@Getter
	@SuperBuilder
	@NoArgsConstructor
	public static class UplinkInputCounterType1 extends UplinkDigitalData {

		@Builder.Default
		private int inputCounters[] = new int[16];

		public void setInputCounters(int[] inputCounters) {
			for(var i = 0; i < inputCounters.length; i++)
				this.inputCounters[i] = inputCounters[i];
		}
	}

	@Setter
	@Getter
	@SuperBuilder
	@NoArgsConstructor
	public static class UplinkLengthError extends UplinkFrame {
		public static final int CODE = 0xEE;
		private int type;

		@Builder.Default
		private byte[] data = new byte[9];

		public int getCode() { return CODE;}

		public void setData(byte[] data) {
			this.data = data;
			for(var i = 0; i < data.length; i++)
				this.data[i] = data[i];
		}
	}

	@Setter
	@Getter
	@SuperBuilder
	@NoArgsConstructor
	public static class DownlinkTimeSyncAnswer extends DownlinkFrame {
		public static final int CODE = 0x00;

		private long syncID;
		private LocalDateTime dateTime;

		public int getCode() { return CODE;}
	}

	@Setter
	@Getter
	@SuperBuilder
	@NoArgsConstructor
	public static class DownlinkIOMessage extends DownlinkFrame {
		public static final int CODE = 0x0400;

		private final boolean [] toBeEnabled = {false, false, false, false};
		private final boolean [] toBeDisabled = {false, false, false, false};

		public int getCode() { return CODE;}

		public void requestChannelStateChange(int channel, boolean enable) {
			if(enable)
				toBeEnabled[channel] = true;
			else
				toBeDisabled[channel] = false;
		}

		public boolean isToBeDisabled(int channel) {
			return toBeDisabled[channel];
		}

		public boolean isToBeEnabled(int channel) {
			return toBeEnabled[channel];
		}
	}

	@Setter
	@Getter
	@SuperBuilder
	@NoArgsConstructor
	public static class DownlinkSetPeriod extends DownlinkFrame {
		public static final int CODE = 0x040104005;

		public int getCode() { return CODE;}
	}

	@Setter
	@Getter
	@SuperBuilder
	@NoArgsConstructor
	public static class DownlinkOffCommand extends DownlinkFrame {
		public static final int CODE = 0x04FF;

		public int getCode() { return CODE;}
	}
}
