package com.lasrosas.iot.ingestor.parser.impl.adeunis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.lasrosas.iot.ingestor.parser.ConfigFrame;

public class AdeunisARF8170BAFrame {

	public static class Status {
		private int frameCounter;
		private boolean lowBat;
		private boolean timestamp;
		private boolean config;

		public boolean isConfig() {
			return config;
		}

		public void setConfig(boolean config) {
			this.config = config;
		}

		public boolean isTimestamp() {
			return timestamp;
		}

		public void setTimestamp(boolean timestamp) {
			this.timestamp = timestamp;
		}

		public int getFrameCounter() {
			return frameCounter;
		}

		public void setFrameCounter(int frameCounter) {
			this.frameCounter = frameCounter;
		}

		public boolean isLowBat() {
			return lowBat;
		}

		public void setLowBat(boolean lowBat) {
			this.lowBat = lowBat;
		}
	}

	public static class BaseFrame {
		private int code;

		public BaseFrame(int code) {
			this.code = code;
		}

		public int getCode() {
			return code;
		}
	}

	public static class BaseUplinkFrame extends BaseFrame {
		private Status status;

		public BaseUplinkFrame(int code) {
			super(code);
		}

		public Status getStatus() {
			return status;
		}

		public void setStatus(Status status) {
			this.status = status;
		}
	}

	public static class BaseDownlinkFrame extends BaseFrame {
		public BaseDownlinkFrame(int code) {
			super(code);
		}
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

	public static enum ChannelType {
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

	public static class ChannelConfiguration {
		private ChannelType type;
		private Integer debounceDurationMs;

		public ChannelConfiguration(ChannelType type, Integer debounceDurationMs) {
			super();
			this.type = type;
			this.debounceDurationMs = debounceDurationMs;
		}

		public Integer getDebounceDurationMs() {
			return debounceDurationMs;
		}

		public void setDebounceDurationMs(Integer debounceDurationMs) {
			this.debounceDurationMs = debounceDurationMs;
		}

		public ChannelType getType() {
			return type;
		}

		public void setType(ChannelType type) {
			this.type = type;
		}
	}

	public static class UplinkFrame0x10 extends BaseUplinkFrame implements ConfigFrame {

		private int S300_TransmissionPeriodOfKeepAlive10sec;
		private int S301_TransmissionPeriodOfPeriodicFrame10sec;
		private ChannelConfiguration S320_Channel1Configuration;
		private ChannelConfiguration S321_Channel2Configuration;
		private ChannelConfiguration S322_Channel3Configuration;
		private ChannelConfiguration S323_Channel4Configuration;

		public UplinkFrame0x10() {
			super(0x10);
		}

		@Override
		public Object getConfig() {
			return this;
		}


		public int getS300_TransmissionPeriodOfKeepAlive10sec() {
			return S300_TransmissionPeriodOfKeepAlive10sec;
		}

		public void setS300_TransmissionPeriodOfKeepAlive10sec(int s300_TransmissionPeriodOfKeepAlive10sec) {
			S300_TransmissionPeriodOfKeepAlive10sec = s300_TransmissionPeriodOfKeepAlive10sec;
		}

		public int getS301_TransmissionPeriodOfPeriodicFrame10sec() {
			return S301_TransmissionPeriodOfPeriodicFrame10sec;
		}

		public void setS301_TransmissionPeriodOfPeriodicFrame10sec(int s301_TransmissionPeriodOfPeriodicFrame10sec) {
			S301_TransmissionPeriodOfPeriodicFrame10sec = s301_TransmissionPeriodOfPeriodicFrame10sec;
		}

		public ChannelConfiguration getS320_Channel1Configuration() {
			return S320_Channel1Configuration;
		}

		public void setS320_Channel1Configuration(ChannelConfiguration s320_Channel1Configuration) {
			S320_Channel1Configuration = s320_Channel1Configuration;
		}

		public ChannelConfiguration getS321_Channel2Configuration() {
			return S321_Channel2Configuration;
		}

		public void setS321_Channel2Configuration(ChannelConfiguration s321_Channel2Configuration) {
			S321_Channel2Configuration = s321_Channel2Configuration;
		}

		public ChannelConfiguration getS322_Channel3Configuration() {
			return S322_Channel3Configuration;
		}

		public void setS322_Channel3Configuration(ChannelConfiguration s322_Channel3Configuration) {
			S322_Channel3Configuration = s322_Channel3Configuration;
		}

		public ChannelConfiguration getS323_Channel4Configuration() {
			return S323_Channel4Configuration;
		}

		public void setS323_Channel4Configuration(ChannelConfiguration s323_Channel4Configuration) {
			S323_Channel4Configuration = s323_Channel4Configuration;
		}
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

	public static class LoraWanOptions {
		private boolean activatedByADR;
		private boolean dutyCycle;
		private boolean classA;

		public boolean isActivatedByADR() {
			return activatedByADR;
		}

		public void setActivatedByADR(boolean activatedByADR) {
			this.activatedByADR = activatedByADR;
		}

		public boolean isDutyCycle() {
			return dutyCycle;
		}

		public void setDutyCycle(boolean dutyCycle) {
			this.dutyCycle = dutyCycle;
		}

		public boolean isClassA() {
			return classA;
		}

		public void setClassA(boolean classA) {
			this.classA = classA;
		}

	}

	public static class UplinkFrame0x20 extends BaseUplinkFrame  implements ConfigFrame {
		private LoraWanOptions S220_lorawanOptions;
		private ProvisioningMode S221_ProvisioningMode;

		public UplinkFrame0x20() {
			super(0x20);
		}

		@Override
		public Object getConfig() {
			return this;
		}

		public LoraWanOptions getS220_lorawanOptions() {
			return S220_lorawanOptions;
		}

		public void setS220_lorawanOptions(LoraWanOptions s220_lorawanOptions) {
			S220_lorawanOptions = s220_lorawanOptions;
		}

		public ProvisioningMode getS221_ProvisioningMode() {
			return S221_ProvisioningMode;
		}

		public void setS221_ProvisioningMode(ProvisioningMode s221_ProvisioningMode) {
			S221_ProvisioningMode = s221_ProvisioningMode;
		}

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

	public static class UplinkFrame0x2F extends BaseUplinkFrame {
		private int downlinkFrameCode;
		private Request2FStatus requestStatus;

		public UplinkFrame0x2F() {
			super(0x2F);
		}

		public int getDownlinkFrameCode() {
			return downlinkFrameCode;
		}

		public void setDownlinkFrameCode(int downlinkFrameCode) {
			this.downlinkFrameCode = downlinkFrameCode;
		}

		public Request2FStatus getRequestStatus() {
			return requestStatus;
		}

		public void setRequestStatus(Request2FStatus requestStatus) {
			this.requestStatus = requestStatus;
		}
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

	public static class UplinkFrame0x30 extends BaseUplinkFrame implements ConfigFrame {
		private Integer channel1EventCounter;
		private Integer channel2EventCounter;
		private Integer channel3EventCounter;
		private Integer channel4EventCounter;
		private ChannelState channel1State;
		private ChannelState channel2State;
		private ChannelState channel3State;
		private ChannelState channel4State;
		private Long timestamp;

		public UplinkFrame0x30() {
			super(0x30);
		}

		@Override
		public Object getConfig() {
			return this;
		}

		public Integer getChannel1EventCounter() {
			return channel1EventCounter;
		}

		public void setChannel1EventCounter(Integer channel1EventCounter) {
			this.channel1EventCounter = channel1EventCounter;
		}

		public Integer getChannel2EventCounter() {
			return channel2EventCounter;
		}

		public void setChannel2EventCounter(Integer channel2EventCounter) {
			this.channel2EventCounter = channel2EventCounter;
		}

		public Integer getChannel3EventCounter() {
			return channel3EventCounter;
		}

		public void setChannel3EventCounter(Integer channel3EventCounter) {
			this.channel3EventCounter = channel3EventCounter;
		}

		public Integer getChannel4EventCounter() {
			return channel4EventCounter;
		}

		public void setChannel4EventCounter(Integer channel4EventCounter) {
			this.channel4EventCounter = channel4EventCounter;
		}

		public ChannelState getChannel1State() {
			return channel1State;
		}

		public void setChannel1State(ChannelState channel1State) {
			this.channel1State = channel1State;
		}

		public ChannelState getChannel2State() {
			return channel2State;
		}

		public void setChannel2State(ChannelState channel2State) {
			this.channel2State = channel2State;
		}

		public ChannelState getChannel3State() {
			return channel3State;
		}

		public void setChannel3State(ChannelState channel3State) {
			this.channel3State = channel3State;
		}

		public ChannelState getChannel4State() {
			return channel4State;
		}

		public void setChannel4State(ChannelState channel4State) {
			this.channel4State = channel4State;
		}

		public Long getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(Long timestamp) {
			this.timestamp = timestamp;
		}
	}

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

		public UplinkFrame0x40() {
			super(0x40);
		}

		public Integer getChannel1EventCounter() {
			return channel1EventCounter;
		}

		public void setChannel1EventCounter(Integer channel1EventCounter) {
			this.channel1EventCounter = channel1EventCounter;
		}

		public Integer getChannel2EventCounter() {
			return channel2EventCounter;
		}

		public void setChannel2EventCounter(Integer channel2EventCounter) {
			this.channel2EventCounter = channel2EventCounter;
		}

		public Integer getChannel3EventCounter() {
			return channel3EventCounter;
		}

		public void setChannel3EventCounter(Integer channel3EventCounter) {
			this.channel3EventCounter = channel3EventCounter;
		}

		public Integer getChannel4EventCounter() {
			return channel4EventCounter;
		}

		public void setChannel4EventCounter(Integer channel4EventCounter) {
			this.channel4EventCounter = channel4EventCounter;
		}

		public ChannelState getChannel1OutputState() {
			return channel1OutputState;
		}

		public void setChannel1OutputState(ChannelState channel1OutputState) {
			this.channel1OutputState = channel1OutputState;
		}

		public ChannelState getChannel2OutputState() {
			return channel2OutputState;
		}

		public void setChannel2OutputState(ChannelState channel2OutputState) {
			this.channel2OutputState = channel2OutputState;
		}

		public ChannelState getChannel3OutputState() {
			return channel3OutputState;
		}

		public void setChannel3OutputState(ChannelState channel3OutputState) {
			this.channel3OutputState = channel3OutputState;
		}

		public ChannelState getChannel4OutputState() {
			return channel4OutputState;
		}

		public void setChannel4OutputState(ChannelState channel4OutputState) {
			this.channel4OutputState = channel4OutputState;
		}

		public ChannelState getChannel1CurrentState() {
			return channel1CurrentState;
		}

		public void setChannel1CurrentState(ChannelState channel1CurrentState) {
			this.channel1CurrentState = channel1CurrentState;
		}

		public ChannelState getChannel1PreviousFrameState() {
			return channel1PreviousFrameState;
		}

		public void setChannel1PreviousFrameState(ChannelState channel1PreviousFrameState) {
			this.channel1PreviousFrameState = channel1PreviousFrameState;
		}

		public ChannelState getChannel2CurrentState() {
			return channel2CurrentState;
		}

		public void setChannel2CurrentState(ChannelState channel2CurrentState) {
			this.channel2CurrentState = channel2CurrentState;
		}

		public ChannelState getChannel2PreviousFrameState() {
			return channel2PreviousFrameState;
		}

		public void setChannel2PreviousFrameState(ChannelState channel2PreviousFrameState) {
			this.channel2PreviousFrameState = channel2PreviousFrameState;
		}

		public ChannelState getChannel3CurrentState() {
			return channel3CurrentState;
		}

		public void setChannel3CurrentState(ChannelState channel3CurrentState) {
			this.channel3CurrentState = channel3CurrentState;
		}

		public ChannelState getChannel3PreviousFrameState() {
			return channel3PreviousFrameState;
		}

		public void setChannel3PreviousFrameState(ChannelState channel3PreviousFrameState) {
			this.channel3PreviousFrameState = channel3PreviousFrameState;
		}

		public ChannelState getChannel4CurrentState() {
			return channel4CurrentState;
		}

		public void setChannel4CurrentState(ChannelState channel4CurrentState) {
			this.channel4CurrentState = channel4CurrentState;
		}

		public ChannelState getChannel4PreviousFrameState() {
			return channel4PreviousFrameState;
		}

		public void setChannel4PreviousFrameState(ChannelState channel4PreviousFrameState) {
			this.channel4PreviousFrameState = channel4PreviousFrameState;
		}

		public Long getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(Long timestamp) {
			this.timestamp = timestamp;
		}
	}

	public static class UplinkFrame0x59 extends BaseUplinkFrame {

		private Long channel1TimeCounter;
		private Long channel2TimeCounter;
		private Long channel3TimeCounter;
		private Long channel4TimeCounter;
		private Long timestamp;

		public UplinkFrame0x59() {
			super(0x59);
		}

		public Long getChannel1TimeCounter() {
			return channel1TimeCounter;
		}

		public void setChannel1TimeCounter(Long channel1TimeCounter) {
			this.channel1TimeCounter = channel1TimeCounter;
		}

		public Long getChannel2TimeCounter() {
			return channel2TimeCounter;
		}

		public void setChannel2TimeCounter(Long channel2TimeCounter) {
			this.channel2TimeCounter = channel2TimeCounter;
		}

		public Long getChannel3TimeCounter() {
			return channel3TimeCounter;
		}

		public void setChannel3TimeCounter(Long channel3TimeCounter) {
			this.channel3TimeCounter = channel3TimeCounter;
		}

		public Long getChannel4TimeCounter() {
			return channel4TimeCounter;
		}

		public void setChannel4TimeCounter(Long channel4TimeCounter) {
			this.channel4TimeCounter = channel4TimeCounter;
		}

		public Long getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(Long timestamp) {
			this.timestamp = timestamp;
		}
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

	public static class UplinkFrame0x31 extends BaseUplinkFrame {
		private List<RegisterValue> values = new ArrayList<>();

		public UplinkFrame0x31() {
			super(0x31);
		}

		public List<RegisterValue> getValues() {
			return Collections.unmodifiableList((values));
		}

		public void addValue(RegisterValue value) {
			this.values.add(value);
		}
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

	public static class UplinkFrame0x33 extends BaseUplinkFrame {
		private Request33Status requestStatus;
		private Integer regsiterIdIfError;

		public UplinkFrame0x33() {
			super(0x33);
		}

		public Request33Status getRequestStatus() {
			return requestStatus;
		}

		public void setRequestStatus(Request33Status requestStatus) {
			this.requestStatus = requestStatus;
		}

		public Integer getRegsiterIdIfError() {
			return regsiterIdIfError;
		}

		public void setRegsiterIdIfError(Integer regsiterIdIfError) {
			this.regsiterIdIfError = regsiterIdIfError;
		}

	}

	// Downlink
	public static class DownlinkFrame0x01 extends BaseDownlinkFrame {
		public DownlinkFrame0x01() {
			super(0x01);
		}
	}

	public static class DownlinkFrame0x02 extends BaseDownlinkFrame {
		public DownlinkFrame0x02() {
			super(0x02);
		}
	}

	public static class DownlinkFrame0x05 extends BaseDownlinkFrame {
		public DownlinkFrame0x05() {
			super(0x05);
		}
	}

	public static class DownlinkFrame0x06 extends BaseDownlinkFrame {
		private ChannelState channel1State;
		private ChannelState channel2State;
		private ChannelState channel3State;
		private ChannelState channel4State;
		private boolean uplinkACKRequered;

		public DownlinkFrame0x06() {
			super(0x06);
		}

		public ChannelState getChannel1State() {
			return channel1State;
		}

		public void setChannel1State(ChannelState channel1State) {
			this.channel1State = channel1State;
		}

		public ChannelState getChannel2State() {
			return channel2State;
		}

		public void setChannel2State(ChannelState channel2State) {
			this.channel2State = channel2State;
		}

		public ChannelState getChannel3State() {
			return channel3State;
		}

		public void setChannel3State(ChannelState channel3State) {
			this.channel3State = channel3State;
		}

		public ChannelState getChannel4State() {
			return channel4State;
		}

		public void setChannel4State(ChannelState channel4State) {
			this.channel4State = channel4State;
		}

		public boolean isUplinkACKRequered() {
			return uplinkACKRequered;
		}

		public void setUplinkACKRequered(boolean uplinkACKRequered) {
			this.uplinkACKRequered = uplinkACKRequered;
		}
	}

	public static class DownlinkFrame0x07 extends BaseDownlinkFrame {
		private int channel1PulseDuration;
		private int channel2PulseDuration;
		private int channel3PulseDuration;
		private int channel4PulseDuration;
		private boolean ackRequired;

		public DownlinkFrame0x07() {
			super(0x07);
		}

		public int getChannel1PulseDuration() {
			return channel1PulseDuration;
		}

		public void setChannel1PulseDuration(int channel1PulseDuration) {
			this.channel1PulseDuration = channel1PulseDuration;
		}

		public int getChannel2PulseDuration() {
			return channel2PulseDuration;
		}

		public void setChannel2PulseDuration(int channel2PulseDuration) {
			this.channel2PulseDuration = channel2PulseDuration;
		}

		public int getChannel3PulseDuration() {
			return channel3PulseDuration;
		}

		public void setChannel3PulseDuration(int channel3PulseDuration) {
			this.channel3PulseDuration = channel3PulseDuration;
		}

		public int getChannel4PulseDuration() {
			return channel4PulseDuration;
		}

		public void setChannel4PulseDuration(int channel4PulseDuration) {
			this.channel4PulseDuration = channel4PulseDuration;
		}

		public boolean isAckRequired() {
			return ackRequired;
		}

		public void setAckRequired(boolean ackRequired) {
			this.ackRequired = ackRequired;
		}

	}

	public static class DownlinkFrame0x40 extends BaseDownlinkFrame {
		private List<Integer> registerNumbers = new ArrayList<>();

		public DownlinkFrame0x40() {
			super(0x40);
		}

		public List<Integer> registerNumbers() {
			return Collections.unmodifiableList(registerNumbers);
		}

		public void addRegisters(int... registers) {
			for (var register : registers) {
				registerNumbers.add(register);
			}
		}
	}

	public static class DownlinkFrame0x41 extends BaseDownlinkFrame {
		private RegisterValue[] registerValues;

		public DownlinkFrame0x41() {
			super(0x41);
		}

		public RegisterValue[] getRegisterValues() {
			return registerValues;
		}

		public void setRegisterValues(RegisterValue[] registerValues) {
			this.registerValues = registerValues;
		}
	}

	public static class DownlinkFrame0x48 extends BaseDownlinkFrame {
		private int minutesBeforeReboot;

		public DownlinkFrame0x48() {
			super(0x48);
		}

		public int getMinutesBeforeReboot() {
			return minutesBeforeReboot;
		}

		public void setMinutesBeforeReboot(int minutesBeforeReboot) {
			this.minutesBeforeReboot = minutesBeforeReboot;
		}
	}

	public static class DownlinkFrame0x49 extends BaseDownlinkFrame {
		private Long timestamp;
		private Integer clockDriftCompensation;

		public DownlinkFrame0x49() {
			super(0x49);
		}

		public Long getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(Long timestamp) {
			this.timestamp = timestamp;
		}

		public Integer getClockDriftCompensation() {
			return clockDriftCompensation;
		}

		public void setClockDriftCompensation(Integer clockDriftCompensation) {
			this.clockDriftCompensation = clockDriftCompensation;
		}
	}
}
