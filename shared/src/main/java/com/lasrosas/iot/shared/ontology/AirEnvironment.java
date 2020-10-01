package com.lasrosas.iot.shared.ontology;

public class AirEnvironment {
	public static final String SCHEMA = "AirEnvironment";

	private Double temperature;
	private Double humidity;
	private Double light;

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
