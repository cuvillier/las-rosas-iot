package com.lasrosas.iot.core.shared.telemetry;

public class AirEnvironment extends Telemetry {
	public static final String SCHEMA = "AirEnvironment";

	private String sensor;
	private Double temperature;
	private Double humidity;
	private Double light;

	public AirEnvironment() {
	}

	public AirEnvironment(String sensor, Double temperature, Double humidity, Double light) {
		super();
		this.sensor = sensor;
		this.temperature = temperature;
		this.humidity = humidity;
		this.light = light;
	}
	public String getSensor() {
		return sensor;
	}
	public void setSensor(String sensor) {
		this.sensor = sensor;
	}
	public Double getTemperature() {
		return temperature;
	}
	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}
	public Double getHumidity() {
		return humidity;
	}
	public void setHumidity(Double humidity) {
		this.humidity = humidity;
	}
	public Double getLight() {
		return light;
	}
	public void setLight(Double light) {
		this.light = light;
	}
}
