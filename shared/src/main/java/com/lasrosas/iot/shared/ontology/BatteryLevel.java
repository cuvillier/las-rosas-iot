package com.lasrosas.iot.shared.ontology;

public class BatteryLevel extends Ontology {
	public static final String SCHEMA = "BatteryLevel";
	private Double voltage;
	private Integer percentage;
	private Boolean alarm;

	public BatteryLevel(boolean alarm) {
		this.alarm = alarm;
		this.percentage = alarm?0:100;
	}

	public BatteryLevel(int percentage) {
		this.percentage = percentage;
	}

	public BatteryLevel(double voltage, double minVoltage, double maxVoltage) {
		setVoltage(voltage);
		this.percentage = computePercentageFromVoltage(minVoltage,  maxVoltage);
	}

	public Integer computePercentageFromVoltage(double minVoltage, double maxVoltage) {
		if(voltage == null) 
			return null;
		else {
			if( voltage < minVoltage ) voltage = minVoltage;
			if( voltage > maxVoltage) voltage = maxVoltage;
			return (int)(100.0 * (voltage - minVoltage / (maxVoltage - minVoltage)) + 0.5);
		}
	}

	public Double getVoltage() {
		return voltage;
	}
	public void setVoltage(Double voltage) {
		this.voltage = voltage;
	}
	public Integer getPercentage() {
		return percentage;
	}
	public void setPercentage(Integer percentage) {
		this.percentage = percentage;
	}

	public Boolean getAlarm() {
		return alarm;
	}

	public void setAlarm(Boolean alarm) {
		this.alarm = alarm;
	}
}
