package com.lasrosas.iot.services.lora.ontology;

public class AirEnvironment {
	public static final String SCHEMA = "AirEnvironment";

	private Float temperature;
	private Float humidity;
	public Float getTemperature() {
		return temperature;
	}
	public void setTemperature(Float temperature) {
		this.temperature = temperature;
	}
	public Float getHumidity() {
		return humidity;
	}
	public void setHumidity(Float humidity) {
		this.humidity = humidity;
	}
}
