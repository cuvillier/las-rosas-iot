package com.lasrosas.iot.ingestor.services.sensors.impl.mfc88;

import java.time.LocalDateTime;

import com.lasrosas.iot.ingestor.services.sensors.impl.mfc88.MFC88LW13IOFrameDecoder.MFC88Version;

public class MFC88LW13IOFrame {

	public abstract static class BaseFrame {
		private int code;

		public BaseFrame(int code) {
			this.code = code;
		}

		public int getCode() {
			return code;
		}
	}
	
	public abstract static class UplinkFrame extends BaseFrame {

		public UplinkFrame(int code) {
			super(code);
		}
	}

	public abstract static class DownlinkFrame extends BaseFrame {

		public DownlinkFrame(int code) {
			super(code);
		}
	}

	public static class UplinkTimeSyncRequest extends UplinkFrame {
		public static final int CODE = 0x01;

		private long syncId;
		private MFC88Version version;
		private int applicationType;

		public static enum UplinkTimeSyncRequestOption {
			GOING_DOWN,
			AFTER_BOOT
		};

		private UplinkTimeSyncRequestOption option;
		private byte[] rfu;

		public UplinkTimeSyncRequest() {
			super(CODE);
		}

		public long getSyncId() {
			return syncId;
		}

		public void setSyncId(long syncId) {
			this.syncId = syncId;
		}

		public MFC88Version getVersion() {
			return version;
		}

		public void setVersion(MFC88Version version) {
			this.version = version;
		}

		public int getApplicationType() {
			return applicationType;
		}

		public void setApplicationType(int applicationType) {
			this.applicationType = applicationType;
		}

		public UplinkTimeSyncRequestOption getOption() {
			return option;
		}

		public void setOption(UplinkTimeSyncRequestOption option) {
			this.option = option;
		}

		public byte[] getRfu() {
			return rfu;
		}

		public void setRfu(byte[] rfu) {
			this.rfu = rfu;
		}
	}

	public static class UplinkIO extends UplinkFrame {
		public static final int CODE = 0x0A;

		private LocalDateTime dateTime;
		private long inputs;
		private long outputs;
		private long events;		

		public UplinkIO() {
			super(CODE);
		}

		public LocalDateTime getDateTime() {
			return dateTime;
		}

		public void setDateTime(LocalDateTime dateTime) {
			this.dateTime = dateTime;
		}

		public long getInputs() {
			return inputs;
		}

		public void setInputs(long inputs) {
			this.inputs = inputs;
		}

		public long getOutputs() {
			return outputs;
		}

		public void setOutputs(long outputs) {
			this.outputs = outputs;
		}

		public long getEvents() {
			return events;
		}

		public void setEvents(long events) {
			this.events = events;
		}
	}

	public static abstract class UplinkDigitalData extends UplinkFrame {
		public static final int CODE = 0x10;

		public UplinkDigitalData() {
			super(CODE);
		}		
	}

	public static class UplinkInputCounterType1 extends UplinkDigitalData {
		private int inputCounters[] = new int[16];

		public int[] getInputCounters() {
			return inputCounters;
		}

		public void setInputCounters(int[] inputCounters) {
			for(var i = 0; i < inputCounters.length; i++)
				this.inputCounters[i] = inputCounters[i];
		}
	}

	public static class UplinkLengthError extends UplinkFrame {
		public static final int CODE = 0xEE;
		private int type;
		private byte[] data = new byte[9];

		public UplinkLengthError() {
			super(CODE);
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

		public byte[] getData() {
			return data;
		}

		public void setData(byte[] data) {
			this.data = data;
			for(var i = 0; i < data.length; i++)
				this.data[i] = data[i];
		}
	}

	public static class DownlinkTimeSyncAnswer extends DownlinkFrame {
		public static final int CODE = 0x00;

		private long syncID;
		private LocalDateTime dateTime;

		public DownlinkTimeSyncAnswer() {
			super(CODE);
		}

		public long getSyncID() {
			return syncID;
		}

		public void setSyncID(long syncID) {
			this.syncID = syncID;
		}

		public LocalDateTime getDateTime() {
			return dateTime;
		}

		public void setDateTime(LocalDateTime dateTime) {
			this.dateTime = dateTime;
		}
	}

	public static class DownlinkIOMessage extends DownlinkFrame {
		public static final int CODE = 0x0400;

		public DownlinkIOMessage() {
			super(CODE);
		}		
	}

	public static class DownlinkSetPeriod extends DownlinkFrame {
		public static final int CODE = 0x040104005;

		public DownlinkSetPeriod() {
			super(CODE);
		}		
	}

	public static class DownlinkOffCommand extends DownlinkFrame {
		public static final int CODE = 0x04FF;

		public DownlinkOffCommand() {
			super(CODE);
		}		
	}
}
