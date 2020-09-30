package com.lasrosas.iot.services.ontology;

public class BatteryLevel extends Ontology {
	public static final String SCHEMA = "BatteryLevel";
	private Double voltage;
	private Integer percentage;

	public void computePercentageFromVoltage(double minVoltage, double maxVoltage) {
		if(voltage == null) 
			percentage = null;
		else {
			if( voltage < minVoltage ) voltage = minVoltage;
			if( voltage > maxVoltage) voltage = maxVoltage;

			percentage = (int)(100.0 * (voltage - minVoltage / (maxVoltage - minVoltage)) + 0.5);
		}
	}

	public Double getVoltage() {
		return voltage;
	}
	public void setVoltage(Double voltage) {
		this.voltage = voltage;
	}
	public int getPercentage() {
		return percentage;
	}
	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}
}
