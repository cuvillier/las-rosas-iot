package com.lasrosas.iot.ingestor.domain.model.message;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class BatteryLevel extends BaseMessage {
	private Double voltage;
	private Integer percentage;
	private Boolean alarm;

	public static BatteryLevel from(double voltage, double minVoltage, double maxVoltage) {
		return BatteryLevel.builder()
				.voltage(voltage)
				.percentage(computePercentageFromVoltage(voltage, minVoltage, maxVoltage))
				.build();
	}

	public static Integer computePercentageFromVoltage(Double voltage, double minVoltage, double maxVoltage) {
		if(voltage == null) return null;

		if( voltage < minVoltage ) voltage = minVoltage;
		if( voltage > maxVoltage) voltage = maxVoltage;
		return (int)(100.0 * ((voltage - minVoltage) / (maxVoltage - minVoltage)) + 0.5);
	}
}
