package com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.adeunis;

import com.lasrosas.iot.ingestor.domain.message.BaseMessage;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

public class AdeunisARF8180BAFrame {

	@Getter
	@Setter
	@SuperBuilder
	public static class Status {
		private int frameCounter;
		private boolean hwError;
		private boolean lowBat;
	}

	@Getter
	@Setter
	@SuperBuilder
	public static class UplinkFrame extends BaseMessage {
		private int code;
		private Status status;

		public UplinkFrame(int code) {
			this.code = code;
		}
	}

	public enum ProductMode {
		PARK,
		PRODUCTION,
		TEST,
		REPLI;

		public static ProductMode parse306(int s306) {
			return switch(s306) {
				case 0-> PARK;
				case 1-> PRODUCTION;
				case 2-> TEST;
				case 3-> REPLI;
				default -> throw new RuntimeException("Invalid ProductMode S306 value=" + s306);
			};
		}
	}

	public enum TypeOfExternalSensor {
		DISABLED,
		E_NTC_APP_1_5P7,
		FANB57863_400_1;
		
		public static TypeOfExternalSensor parse(int code) {
			return switch(code) {
				case 0 -> DISABLED;
				case 1 -> E_NTC_APP_1_5P7;
				case 2 -> FANB57863_400_1;
				default -> throw new RuntimeException("Invalid TypeOfExternalSensor value=" + code);
			};
		}
	}

	@Getter
	@Setter
	@SuperBuilder
	public static class UplinkFrame0x10 extends UplinkFrame {
		private int S300_PeriodicityOfKeepAlive10mn;
		private int S301_PeriodicityOfTransmission10mn;
		private int S302_ConfigurationOfTheInternalSensor;
		private int S303_ConfigurationOfTheEventsOfTheInternalSensor;
		private int S304_ConfigurationOfTheExternalSensor;
		private int S305_ConfigurationOfTheEventsOfTheExternalSensor;
		private ProductMode S306_ProductMode;
		private TypeOfExternalSensor typeOfExternalSensor;
		private int S317_PeriodicityOfTheAcquisitionMn;

		public UplinkFrame0x10() {
			super(0x10);
		}
	}

	@Getter
	@Setter
	@SuperBuilder
	public static class UplinkFrame0x11 extends UplinkFrame {
		private int S309_HighThresholdOfTheInternalSensor;
		private int S310_HysteresisOfTheHighThresholdOfTheInternalSensor;
		private int S311_LowThresholdOfTheInternalSensor;
		private int S312_HysteresisOfTheLowThresholdOfTheInternalSensor;
		private int S318_SuperSamplingFactor;

		public UplinkFrame0x11() {
			super(0x11);
		}
	}

	@Getter
	@Setter
	@SuperBuilder
	public static class UplinkFrame0x12 extends UplinkFrame {
		private int S313_HighThresholdOfTheExternalSensor;
		private int S314_HysteresisOfTheHighThresholdOfTheExternalSensor;
		private int S315_LowThresholdOfTheExternalSensor;
		private int S316_HysteresisOfTheLowThresholdOfTheExternalSensor;

		public UplinkFrame0x12() {
			super(0x12);
		}
	}
	
	public enum ConnectionMode {
		ABP,
		OTAA;

		public static ConnectionMode parse(int code) {
			return switch(code) {
				case 0 -> ABP;
				case 1 -> OTAA;
				default-> throw new RuntimeException("Invalid connection mode=" + code);
			};
		}
	}

	@Getter
	@Setter
	@SuperBuilder
	public static class UplinkFrame0x20 extends UplinkFrame {
		private boolean adr;
		private ConnectionMode conectionMode;

		public UplinkFrame0x20() {
			super(0x20);
		}
}
	
	public enum InternalSensorIdentifier {
		UnknownAbsentError,
		B57863S0303F040;

		public static InternalSensorIdentifier parse(int code) {
			return switch(code) {
				case 0 -> UnknownAbsentError;
				case 1 -> B57863S0303F040;
				default -> throw new RuntimeException("Invalid InternalSensorIdentifier code=" + code);
			};
		}
	}

	public enum ExternalSensorIdentifier {
		UnknownAbsentError,
		E_NTC_APP_1_5P7,
		FANB57863_400_1;

		public static ExternalSensorIdentifier parse(int code) {
			return switch(code) {
				case 0 -> UnknownAbsentError;
				case 1 -> E_NTC_APP_1_5P7;
				case 2 -> FANB57863_400_1;
				default -> throw new RuntimeException("Invalid ExternalSensorIdentifier code=" + code);
			};
		}
	}

	@Getter
	@Setter
	@SuperBuilder
	public static abstract class UplinkFrame0x30x43 extends UplinkFrame {
		private InternalSensorIdentifier internalSensorIdentifier;
		private int S302_UserIdentifier;
		private Integer valueInternalSensor10thDeg;
		private ExternalSensorIdentifier externalSensorIdentifier;
		private int S304_UserIdentifier;
		private Integer valueExternalSensor10thDeg;

		public UplinkFrame0x30x43(int code) {
			super(code);
		}

		public void setValueInternalSensor10thDeg(Integer valueInternalSensor10thDeg) {
			if(valueInternalSensor10thDeg == 32768)
				this.valueInternalSensor10thDeg = null;
			else
				this.valueInternalSensor10thDeg = valueInternalSensor10thDeg;
		}
		public void setValueExternalSensor10thDeg(Integer valueExternalSensor10thDeg) {
			if(valueExternalSensor10thDeg == 32768)
				this.valueExternalSensor10thDeg = null;
			else
				this.valueExternalSensor10thDeg = valueExternalSensor10thDeg;
		}
	}

	@Getter
	@Setter
	@SuperBuilder
	public static class UplinkFrame0x30 extends UplinkFrame0x30x43 {
		public UplinkFrame0x30() {
			super(0x30);
		}
	}

	@Getter
	@Setter
	@SuperBuilder
	public static class UplinkFrame0x43 extends UplinkFrame0x30x43 {
		public UplinkFrame0x43() {
			super(0x43);
		}
	}
}
