package com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.dragino;

import com.lasrosas.iot.ingestor.domain.model.message.BaseMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import com.lasrosas.iot.ingestor.shared.exceptions.NotFoundException;

public class DraginoLHT65Frame {

	@Getter
	@Setter
	@SuperBuilder
	public static class UplinkTempHumRequest extends BaseMessage {

		@Getter
		@AllArgsConstructor
		public enum BatteryStatus {			
			UltraLow(0b00),
			Low(0b01),
			OK(0b10),
			Good(0b11);

			private final int code;

			public static BatteryStatus getByCode(int code) {
				for(var s: BatteryStatus.values() ) 
					if(s.getCode() == code ) return s;
				throw new NotFoundException("BatteryStatus with code " + code);
			}
		}

		private final BatteryStatus batteryStatus;
		private final double batteryVoltage;
		private final Double temperatureEXT;
		private final double temperatureINT;
		private final double humidityINT;
		private final int sensorExt;
	}
}
