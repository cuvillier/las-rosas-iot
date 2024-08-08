package com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.adeunis;

import com.lasrosas.iot.ingestor.domain.model.message.BaseMessage;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

public class AdeunisARF8170BAFrame {

	@Setter
	@Getter
	@SuperBuilder
	@NoArgsConstructor
	public static class Status {
		private int frameCounter;
		private boolean lowBat;
		private boolean timestamp;
		private boolean config;
	}

	@SuperBuilder
	@NoArgsConstructor
	public static abstract class BaseFrame extends BaseMessage {
		public abstract int getCode();
	}

	@Setter
	@Getter
	@SuperBuilder
	@NoArgsConstructor
	public static abstract class BaseUplinkFrame extends BaseFrame {
		private Status status;
	}

	@Setter
	@Getter
	@SuperBuilder
	@NoArgsConstructor
	public static abstract class BaseDownlinkFrame extends BaseFrame {
	}

	// Uplink
	public enum ProductMode {
		PARK, PRODUCTION, TEST, REPLI;

		public static ProductMode parse306(int s306) {
			switch (s306) {
			case 0:
				return PARK;
			case 1:
				return PRODUCTION;
			case 2:
				return TEST;
			case 3:
				return REPLI;
			default:
				throw new RuntimeException("Invalid ProductMode S306 value=" + s306);
			}
		}
	}

	public enum ChannelType {
		DEACTIVTED, INPUT_EVENT_ON, INPUT_EVENT_OFF, INPUT_EVENT_ON_OFF, OUTPUT;

		public static ChannelType parse(int channelType) {
			switch (channelType) {
			case 0:
				return DEACTIVTED;
			case 1:
				return INPUT_EVENT_ON;
			case 2:
				return INPUT_EVENT_OFF;
			case 3:
				return INPUT_EVENT_ON_OFF;
			case 4:
				return OUTPUT;
			default:
				throw new RuntimeException("Invalid ChannelType value=" + channelType);
			}
		}
	}

	@Setter
	@Getter
	@SuperBuilder
	@NoArgsConstructor
	public static class ChannelConfiguration {
		private ChannelType type;
		private Integer debounceDurationMs;
	}

	@Setter
	@Getter
	@SuperBuilder
	@NoArgsConstructor
	public static class UplinkFrame0x10 extends BaseUplinkFrame {

		private int S300_TransmissionPeriodOfKeepAlive10sec;
		private int S301_TransmissionPeriodOfPeriodicFrame10sec;
		private ChannelConfiguration S320_Channel1Configuration;
		private ChannelConfiguration S321_Channel2Configuration;
		private ChannelConfiguration S322_Channel3Configuration;
		private ChannelConfiguration S323_Channel4Configuration;

		public int getCode() { return 0x10; }
	}

	public enum ProvisioningMode {
		ABP, OTAA;

		public static ProvisioningMode parse(int code) {
			switch (code) {
			case 0:
				return ABP;
			case 1:
				return OTAA;
			default:
				throw new RuntimeException("Invalid ProvisioningMode mode=" + code);
			}
		}
	}

	@Setter
	@Getter
	@SuperBuilder
	@NoArgsConstructor
	public static class LoraWanOptions {
		private boolean activatedByADR;
		private boolean dutyCycle;
		private boolean classA;
	}

	@Setter
	@Getter
	@SuperBuilder
	@NoArgsConstructor
	public static class UplinkFrame0x20 extends BaseUplinkFrame {
		private LoraWanOptions S220_lorawanOptions;
		private ProvisioningMode S221_ProvisioningMode;

		public int getCode() { return 0x20; }
	}

	public enum Request2FStatus {
		NA, SUCSESS, ERROR_GENERIC, ERROR_WRONG_STATE, ERROR_INVALID_REQUEST, OTHER;

		public static Request2FStatus parse(int code) {
			switch (code) {
			case 0:
				return NA;
			case 1:
				return SUCSESS;
			case 2:
				return ERROR_GENERIC;
			case 3:
				return ERROR_WRONG_STATE;
			case 4:
				return ERROR_INVALID_REQUEST;
			case 5:
				return OTHER;

			default:
				throw new RuntimeException("Invalid Request2FStatus mode=" + code);
			}
		}
	}

	@Setter
	@Getter
	@SuperBuilder
	@NoArgsConstructor
	public static class UplinkFrame0x2F extends BaseUplinkFrame {
		private int downlinkFrameCode;
		private Request2FStatus requestStatus;

		public int getCode() { return 0x2F; }
	}

	public enum ChannelState {
		OPEN_OFF, CLOSED_ON;

		public static ChannelState parse(int code) {
			switch (code) {
			case 0:
				return OPEN_OFF;
			case 1:
				return CLOSED_ON;
			default:
				throw new RuntimeException("Invalid ChannelState mode=" + code);
			}
		}
	}

	@Setter
	@Getter
	@SuperBuilder
	@NoArgsConstructor
	public static class UplinkFrame0x30 extends BaseUplinkFrame  {
		private Integer channel1EventCounter;
		private Integer channel2EventCounter;
		private Integer channel3EventCounter;
		private Integer channel4EventCounter;
		private ChannelState channel1State;
		private ChannelState channel2State;
		private ChannelState channel3State;
		private ChannelState channel4State;
		private Long timestamp;

		public int getCode() { return 0x30; }
	}

	@Setter
	@Getter
	@SuperBuilder
	@NoArgsConstructor
	public static class UplinkFrame0x40 extends BaseUplinkFrame {
		private Integer channel1EventCounter;
		private Integer channel2EventCounter;
		private Integer channel3EventCounter;
		private Integer channel4EventCounter;
		private ChannelState channel1OutputState;
		private ChannelState channel2OutputState;
		private ChannelState channel3OutputState;
		private ChannelState channel4OutputState;
		private ChannelState channel1CurrentState;
		private ChannelState channel1PreviousFrameState;
		private ChannelState channel2CurrentState;
		private ChannelState channel2PreviousFrameState;
		private ChannelState channel3CurrentState;
		private ChannelState channel3PreviousFrameState;
		private ChannelState channel4CurrentState;
		private ChannelState channel4PreviousFrameState;
		private Long timestamp;

		public int getCode() { return 0x40; }
	}

	@Setter
	@Getter
	@SuperBuilder
	@NoArgsConstructor
	public static class UplinkFrame0x59 extends BaseUplinkFrame {

		private Long channel1TimeCounter;
		private Long channel2TimeCounter;
		private Long channel3TimeCounter;
		private Long channel4TimeCounter;
		private Long timestamp;

		public int getCode() { return 0x59; }
	}

	public static class RegisterValue {
		private int registerNumber;
		private Boolean asBoolean;
		private Integer asInt;
		private Long asLong;

		public RegisterValue(int registerNumber, boolean value) {
			this.registerNumber = registerNumber;
			this.asBoolean = value;
		}

		public RegisterValue(int registerNumber, int value) {
			super();
			this.registerNumber = registerNumber;
			this.asInt = value;
		}

		public RegisterValue(int registerNumber, long value) {
			super();
			this.registerNumber = registerNumber;
			this.asLong = value;
		}

		public int getRegisterNumber() {
			return registerNumber;
		}

		public Boolean getAsBoolean() {
			if (asBoolean == null)
				throw new RuntimeException("Value of register " + registerNumber + " is not a boolean");
			return asBoolean;
		}

		public Integer getAsInt() {
			if (asInt == null)
				throw new RuntimeException("Value of register " + registerNumber + " is not an integer");
			return asInt;
		}

		public Long getAsLong() {
			if (asLong == null)
				throw new RuntimeException("Value of register " + registerNumber + " is not a long");
			return asLong;
		}
	}

	@Setter
	@Getter
	@SuperBuilder
	@NoArgsConstructor
	public static class UplinkFrame0x31 extends BaseUplinkFrame {
		public static final int CODE = 0x31;

		@Builder.Default
		private List<RegisterValue> values = new ArrayList<>();

		public UplinkFrame0x31(List<RegisterValue> values) {
			this.values = values;
		}

		public int getCode() { return CODE; }
	}

	public enum Request33Status {
		NA, SUCSESS, SUCCESS_NO_UPDATE, ERROR_COHERENCY, ERROR_INVALID_REGISTER, ERROR_INVALID_VALUE,
		ERROR_TRUNCATED_VALUE, ERROR_ACCESS_NOT_ALLOWED, ERROR_OTHER;

		public static Request33Status parse(int code) {
			switch (code) {
			case 0:
				return NA;
			case 1:
				return SUCSESS;
			case 2:
				return SUCCESS_NO_UPDATE;
			case 3:
				return ERROR_COHERENCY;
			case 4:
				return ERROR_INVALID_REGISTER;
			case 5:
				return ERROR_INVALID_VALUE;
			case 6:
				return ERROR_TRUNCATED_VALUE;
			case 7:
				return ERROR_ACCESS_NOT_ALLOWED;
			case 8:
				return ERROR_OTHER;

			default:
				throw new RuntimeException("Invalid RequestStatus mode=" + code);
			}
		}
	}

	@Setter
	@Getter
	@SuperBuilder
	@NoArgsConstructor
	public static class UplinkFrame0x33 extends BaseUplinkFrame {
		private Request33Status requestStatus;
		private Integer regsiterIdIfError;

		public int getCode() { return 0x33; }
	}

	@Setter
	@Getter
	@SuperBuilder
	@NoArgsConstructor
	public static class DownlinkFrame0x01 extends BaseDownlinkFrame {
		public int getCode() { return 0x01; }
	}

	@Setter
	@Getter
	@SuperBuilder
	@NoArgsConstructor
	public static class DownlinkFrame0x02 extends BaseDownlinkFrame {
		public int getCode() { return 0x02; }
	}

	@Setter
	@Getter
	@SuperBuilder
	@NoArgsConstructor
	public static class DownlinkFrame0x05 extends BaseDownlinkFrame {
		public int getCode() { return 0x05; }
	}

	@Setter
	@Getter
	@SuperBuilder
	@NoArgsConstructor
	public static class DownlinkFrame0x06 extends BaseDownlinkFrame {
		private ChannelState channel1State;
		private ChannelState channel2State;
		private ChannelState channel3State;
		private ChannelState channel4State;
		private boolean uplinkACKRequered;

		public int getCode() { return 0x06; }
	}

	@Setter
	@Getter
	@SuperBuilder
	@NoArgsConstructor
	public static class DownlinkFrame0x07 extends BaseDownlinkFrame {
		private int channel1PulseDuration;
		private int channel2PulseDuration;
		private int channel3PulseDuration;
		private int channel4PulseDuration;
		private boolean ackRequired;

		public int getCode() { return 0x07; }
	}

	@Setter
	@Getter
	@SuperBuilder
	public static class DownlinkFrame0x40 extends BaseDownlinkFrame {
		public static final int  CODE = 0x40;
		private List<Integer> registerNumbers;

		public DownlinkFrame0x40() {
			registerNumbers = new ArrayList<>();
		}

		public void addRegisters(int... registers) {
			for (var register : registers) {
				registerNumbers.add(register);
			}
		}
		public int getCode() { return CODE; }
	}

	@Setter
	@Getter
	@SuperBuilder
	@NoArgsConstructor
	public static class DownlinkFrame0x41 extends BaseDownlinkFrame {
		private RegisterValue[] registerValues;

		public int getCode() { return 0x41; }
	}

	@Setter
	@Getter
	@SuperBuilder
	@NoArgsConstructor
	public static class DownlinkFrame0x48 extends BaseDownlinkFrame {
		private int minutesBeforeReboot;

		public int getCode() { return 0x48; }
	}

	@Setter
	@Getter
	@SuperBuilder
	@NoArgsConstructor
	public static class DownlinkFrame0x49 extends BaseDownlinkFrame {
		private Long timestamp;
		private Integer clockDriftCompensation;

		public int getCode() { return 0x49; }
	}
}
