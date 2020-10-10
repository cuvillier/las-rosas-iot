package com.lasrosas.iot.shared.ontology;

public class WaterTankFilling {
	public static String SCHEMA = "WaterTankFilling";
	private double volume;
	private int percentage;
	
	public WaterTankFilling(double volume, int percentage) {
		super();
		this.volume = volume;
		this.percentage = percentage;
	}
	public double getVolume() {
		return volume;
	}
	public int getPercentage() {
		return percentage;
	}	
}
