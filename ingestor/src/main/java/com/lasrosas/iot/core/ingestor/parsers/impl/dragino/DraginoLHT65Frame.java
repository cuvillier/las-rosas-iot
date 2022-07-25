package com.lasrosas.iot.core.ingestor.parsers.impl.dragino;

import com.lasrosas.iot.core.ingestor.parsers.api.ThingDataMessage;
import com.lasrosas.iot.core.shared.utils.NotFoundException;

public class DraginoLHT65Frame {

	public static class UplinkTempHumRequest extends ThingDataMessage  {

		public enum BatteryStatus {			
			UltraLow(0b00),
			Low(0b01),
			OK(0b10),
			Good(0b11);
			
			private final int code;
			
			BatteryStatus(int code) {
				this.code = code;
			}

			public int getCode() {
				return code;
			}

			public static BatteryStatus getByCode(int code) {
				for(var s: BatteryStatus.values() ) 
					if(s.getCode() == code ) return s;
				throw new NotFoundException("BatteryStatus with code " + code);
			}
		}

		private final BatteryStatus batteryStatus;
		private final double batteryVoltage;
		private final double temperatureEXT;
		private final double temperatureINT;
		private final double humidityINT;
		private final int sensorExt;

		public UplinkTempHumRequest(BatteryStatus batteryStatus, double batteryVoltage, double temperatureINT, double humidityINT, int sensorExt, double temperatureEXT) {
			this.batteryStatus = batteryStatus;
			this.batteryVoltage = batteryVoltage;
			this.temperatureEXT = temperatureEXT;
			this.temperatureINT = temperatureINT;
			this.humidityINT = humidityINT;
			this.sensorExt = sensorExt;
		}

		public BatteryStatus getBatteryStatus() {
			return batteryStatus;
		}

		public double getBatteryVoltage() {
			return batteryVoltage;
		}

		public double getTemperatureEXT() {
			return temperatureEXT;
		}

		public double getTemperatureINT() {
			return temperatureINT;
		}

		public double getHumidityINT() {
			return humidityINT;
		}
	}
}
