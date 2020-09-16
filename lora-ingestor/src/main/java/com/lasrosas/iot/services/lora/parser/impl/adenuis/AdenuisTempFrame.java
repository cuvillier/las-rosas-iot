package com.lasrosas.iot.services.lora.parser.impl.adenuis;

public class AdenuisTempFrame {

	public static class Status {
		private int frameCounter;
		private boolean hwError;
		private boolean lowBat;

		public int getFrameCounter() {
			return frameCounter;
		}
		public void setFrameCounter(int frameCounter) {
			this.frameCounter = frameCounter;
		}
		public boolean isHwError() {
			return hwError;
		}
		public void setHwError(boolean hwError) {
			this.hwError = hwError;
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
		private Status status;

		public BaseFrame(int code) {
			this.code = code;
		}

		public int getCode() {
			return code;
		}
		public void setCode(int code) {
			this.code = code;
		}
		public Status getStatus() {
			return status;
		}
		public void setStatus(Status status) {
			this.status = status;
		}
	}

	public enum ProductMode {
		PARK,
		PRODUCTION,
		TEST,
		REPLI;

		public static ProductMode parse306(int s306) {
			switch(s306) {
			case 0: return PARK;
			case 1: return PRODUCTION;
			case 2: return TEST;
			case 3: return REPLI;
			default: throw new RuntimeException("Invalid ProductMode S306 value=" + s306);
			}
		}
	}

	public enum TypeOfExternalSensor {
		DISABLED,
		E_NTC_APP_1_5P7,
		FANB57863_400_1;
		
		public static TypeOfExternalSensor parse(int code) {
			switch(code) {
			case 0: return DISABLED;
			case 1: return E_NTC_APP_1_5P7;
			case 2: return FANB57863_400_1;
			default: throw new RuntimeException("Invalid TypeOfExternalSensor value=" + code);
			}
		}
	}

	public static class Frame0x10 extends BaseFrame {
		private int S300_PeriodicityOfKeepAlive10mn;
		private int S301_PeriodicityOfTransmission10mn;
		private int S302_ConfigurationOfTheInternalSensor;
		private int S303_ConfigurationOfTheEventsOfTheInternalSensor;
		private int S304_ConfigurationOfTheExternalSensor;
		private int S305_ConfigurationOfTheEventsOfTheExternalSensor;
		private ProductMode S306_ProductMode;
		private TypeOfExternalSensor typeOfExternalSensor;
		private int S317_PeriodicityOfTheAcquisitionMn;

		public Frame0x10() {
			super(0x10);
		}

		public int getS300_PeriodicityOfKeepAlive10mn() {
			return S300_PeriodicityOfKeepAlive10mn;
		}
		public void setS300_PeriodicityOfKeepAlive10mn(int s300_PeriodicityOfKeepAlive10mn) {
			S300_PeriodicityOfKeepAlive10mn = s300_PeriodicityOfKeepAlive10mn;
		}
		public int getS301_PeriodicityOfTransmission10mn() {
			return S301_PeriodicityOfTransmission10mn;
		}
		public void setS301_PeriodicityOfTransmission10mn(int s301_PeriodicityOfTransmission10mn) {
			S301_PeriodicityOfTransmission10mn = s301_PeriodicityOfTransmission10mn;
		}
		public int getS302_ConfigurationOfTheInternalSensor() {
			return S302_ConfigurationOfTheInternalSensor;
		}
		public void setS302_ConfigurationOfTheInternalSensor(int s302_ConfigurationOfTheInternalSensor) {
			S302_ConfigurationOfTheInternalSensor = s302_ConfigurationOfTheInternalSensor;
		}
		public int getS303_ConfigurationOfTheEventsOfTheInternalSensor() {
			return S303_ConfigurationOfTheEventsOfTheInternalSensor;
		}
		public void setS303_ConfigurationOfTheEventsOfTheInternalSensor(int s303_ConfigurationOfTheEventsOfTheInternalSensor) {
			S303_ConfigurationOfTheEventsOfTheInternalSensor = s303_ConfigurationOfTheEventsOfTheInternalSensor;
		}
		public int getS304_ConfigurationOfTheExternalSensor() {
			return S304_ConfigurationOfTheExternalSensor;
		}
		public void setS304_ConfigurationOfTheExternalSensor(int s304_ConfigurationOfTheExternalSensor) {
			S304_ConfigurationOfTheExternalSensor = s304_ConfigurationOfTheExternalSensor;
		}
		public int getS305_ConfigurationOfTheEventsOfTheExternalSensor() {
			return S305_ConfigurationOfTheEventsOfTheExternalSensor;
		}
		public void setS305_ConfigurationOfTheEventsOfTheExternalSensor(int s305_ConfigurationOfTheEventsOfTheExternalSensor) {
			S305_ConfigurationOfTheEventsOfTheExternalSensor = s305_ConfigurationOfTheEventsOfTheExternalSensor;
		}
		public ProductMode getS306_ProductMode() {
			return S306_ProductMode;
		}
		public void setS306_ProductMode(ProductMode productMode) {
			this.S306_ProductMode = productMode;
		}
		public TypeOfExternalSensor getTypeOfExternalSensor() {
			return typeOfExternalSensor;
		}
		public void setTypeOfExternalSensor(TypeOfExternalSensor typeOfExternalSensor) {
			this.typeOfExternalSensor = typeOfExternalSensor;
		}
		public int getS317_PeriodicityOfTheAcquisitionMn() {
			return S317_PeriodicityOfTheAcquisitionMn;
		}
		public void setS317_PeriodicityOfTheAcquisitionMn(int s317_PeriodicityOfTheAcquisitionMn) {
			S317_PeriodicityOfTheAcquisitionMn = s317_PeriodicityOfTheAcquisitionMn;
		}

	}
	
	public static class Frame0x11 extends BaseFrame {
		private int S309_HighThresholdOfTheInternalSensor;
		private int S310_HysteresisOfTheHighThresholdOfTheInternalSensor;
		private int S311_LowThresholdOfTheInternalSensor;
		private int S312_HysteresisOfTheLowThresholdOfTheInternalSensor;
		private int S318_SuperSamplingFactor;

		public Frame0x11() {
			super(0x11);
		}

		public int getS309_HighThresholdOfTheInternalSensor() {
			return S309_HighThresholdOfTheInternalSensor;
		}
		public void setS309_HighThresholdOfTheInternalSensor(int s309_HighThresholdOfTheInternalSensor) {
			S309_HighThresholdOfTheInternalSensor = s309_HighThresholdOfTheInternalSensor;
		}
		public int getS310_HysteresisOfTheHighThresholdOfTheInternalSensor() {
			return S310_HysteresisOfTheHighThresholdOfTheInternalSensor;
		}
		public void setS310_HysteresisOfTheHighThresholdOfTheInternalSensor(
				int s310_HysteresisOfTheHighThresholdOfTheInternalSensor) {
			S310_HysteresisOfTheHighThresholdOfTheInternalSensor = s310_HysteresisOfTheHighThresholdOfTheInternalSensor;
		}
		public int getS311_LowThresholdOfTheInternalSensor() {
			return S311_LowThresholdOfTheInternalSensor;
		}
		public void setS311_LowThresholdOfTheInternalSensor(int s311_LowThresholdOfTheInternalSensor) {
			S311_LowThresholdOfTheInternalSensor = s311_LowThresholdOfTheInternalSensor;
		}
		public int getS312_HysteresisOfTheLowThresholdOfTheInternalSensor() {
			return S312_HysteresisOfTheLowThresholdOfTheInternalSensor;
		}
		public void setS312_HysteresisOfTheLowThresholdOfTheInternalSensor(
				int s312_HysteresisOfTheLowThresholdOfTheInternalSensor) {
			S312_HysteresisOfTheLowThresholdOfTheInternalSensor = s312_HysteresisOfTheLowThresholdOfTheInternalSensor;
		}
		public int getS318_SuperSamplingFactor() {
			return S318_SuperSamplingFactor;
		}
		public void setS318_SuperSamplingFactor(int s318_SuperSamplingFactor) {
			S318_SuperSamplingFactor = s318_SuperSamplingFactor;
		}
	}

	public static class Frame0x12 extends BaseFrame {
		private int S313_HighThresholdOfTheExternalSensor;
		private int S314_HysteresisOfTheHighThresholdOfTheExternalSensor;
		private int S315_LowThresholdOfTheExternalSensor;
		private int S316_HysteresisOfTheLowThresholdOfTheExternalSensor;

		public Frame0x12() {
			super(0x12);
		}

		public int getS313_HighThresholdOfTheExternalSensor() {
			return S313_HighThresholdOfTheExternalSensor;
		}
		public void setS313_HighThresholdOfTheExternalSensor(int s313_HighThresholdOfTheExternalSensor) {
			S313_HighThresholdOfTheExternalSensor = s313_HighThresholdOfTheExternalSensor;
		}
		public int getS314_HysteresisOfTheHighThresholdOfTheExternalSensor() {
			return S314_HysteresisOfTheHighThresholdOfTheExternalSensor;
		}
		public void setS314_HysteresisOfTheHighThresholdOfTheExternalSensor(
				int s314_HysteresisOfTheHighThresholdOfTheExternalSensor) {
			S314_HysteresisOfTheHighThresholdOfTheExternalSensor = s314_HysteresisOfTheHighThresholdOfTheExternalSensor;
		}
		public int getS315_LowThresholdOfTheExternalSensor() {
			return S315_LowThresholdOfTheExternalSensor;
		}
		public void setS315_LowThresholdOfTheExternalSensor(int s315_LowThresholdOfTheExternalSensor) {
			S315_LowThresholdOfTheExternalSensor = s315_LowThresholdOfTheExternalSensor;
		}
		public int getS316_HysteresisOfTheLowThresholdOfTheExternalSensor() {
			return S316_HysteresisOfTheLowThresholdOfTheExternalSensor;
		}
		public void setS316_HysteresisOfTheLowThresholdOfTheExternalSensor(
				int s316_HysteresisOfTheLowThresholdOfTheExternalSensor) {
			S316_HysteresisOfTheLowThresholdOfTheExternalSensor = s316_HysteresisOfTheLowThresholdOfTheExternalSensor;
		}
	}
	
	public enum ConnectionMode {
		ABP,
		OTAA;

		public static ConnectionMode parse(int code) {
			switch(code) {
			case 0:
				return ABP;
			case 1:
				return OTAA;
			default:
				throw new RuntimeException("Invalid connection mode=" + code);
			}
		}
	}

	public static class Frame0x20 extends BaseFrame {
		private boolean adr;
		private ConnectionMode conectionMode;

		public Frame0x20() {
			super(0x20);
		}

		public boolean isAdr() {
			return adr;
		}
		public void setAdr(boolean adr) {
			this.adr = adr;
		}
		public ConnectionMode getConectionMode() {
			return conectionMode;
		}
		public void setConectionMode(ConnectionMode conectionMode) {
			this.conectionMode = conectionMode;
		}
	}
	
	public enum InternalSensorIdentifier {
		UnknownAbsentError,
		B57863S0303F040;

		public static InternalSensorIdentifier parse(int code) {
			switch(code) {
			case 0: return UnknownAbsentError;
			case 1: return B57863S0303F040;
			default: throw new RuntimeException("Invalid InternalSensorIdentifier code=" + code);
			}
		}
	}

	public enum ExternalSensorIdentifier {
		UnknownAbsentError,
		E_NTC_APP_1_5P7,
		FANB57863_400_1;

		public static ExternalSensorIdentifier parse(int code) {
			switch(code) {
			case 0: return UnknownAbsentError;
			case 1: return E_NTC_APP_1_5P7;
			case 2: return FANB57863_400_1;
			default: throw new RuntimeException("Invalid ExternalSensorIdentifier code=" + code);
			}
		}
	}

	public static abstract class Frame0x30x43 extends BaseFrame {
		private InternalSensorIdentifier internalSensorIdentifier;
		private int S302_UserIdentifier;
		private Integer valueInternalSensor10thDeg;
		private ExternalSensorIdentifier externalSensorIdentifier;
		private int S304_UserIdentifier;
		private Integer valueExternalSensor10thDeg;

		public Frame0x30x43(int code) {
			super(code);
		}

		public InternalSensorIdentifier getInternalSensorIdentifier() {
			return internalSensorIdentifier;
		}
		public void setInternalSensorIdentifier(InternalSensorIdentifier internalSensorIdentifier) {
			this.internalSensorIdentifier = internalSensorIdentifier;
		}
		public int getS302_UserIdentifier() {
			return S302_UserIdentifier;
		}
		public void setS302_UserIdentifier(int s302_UserIdentifier) {
			S302_UserIdentifier = s302_UserIdentifier;
		}
		public Integer getValueInternalSensor10thDeg() {
			return valueInternalSensor10thDeg;
		}
		public void setValueInternalSensor10thDeg(Integer valueInternalSensor10thDeg) {
			if(valueInternalSensor10thDeg == 32768)
				this.valueInternalSensor10thDeg = null;
			else
				this.valueInternalSensor10thDeg = valueInternalSensor10thDeg;
		}
		public ExternalSensorIdentifier getExternalSensorIdentifier() {
			return externalSensorIdentifier;
		}
		public void setExternalSensorIdentifier(ExternalSensorIdentifier externalSensorIdentifier) {
			this.externalSensorIdentifier = externalSensorIdentifier;
		}
		public int getS304_UserIdentifier() {
			return S304_UserIdentifier;
		}
		public void setS304_UserIdentifier(int s304_UserIdentifier) {
			S304_UserIdentifier = s304_UserIdentifier;
		}
		public Integer getValueExternalSensor10thDeg() {
			return valueExternalSensor10thDeg;
		}
		public void setValueExternalSensor10thDeg(Integer valueExternalSensor10thDeg) {
			if(valueExternalSensor10thDeg == 32768)
				this.valueExternalSensor10thDeg = null;
			else
				this.valueExternalSensor10thDeg = valueExternalSensor10thDeg;
		}
	}	

	public static class Frame0x30 extends Frame0x30x43 {
		public Frame0x30() {
			super(0x30);
		}
	}

	public static class Frame0x43 extends Frame0x30x43 {
		public Frame0x43() {
			super(0x43);
		}
	}
}
