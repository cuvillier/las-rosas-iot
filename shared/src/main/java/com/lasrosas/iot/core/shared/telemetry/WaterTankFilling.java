package com.lasrosas.iot.core.shared.telemetry;

import com.google.gson.JsonObject;

public class WaterTankFilling extends Telemetry {
	public static String SCHEMA = "WaterTankFilling";
	private final Double volume;
	private final Double percentage;
	private final Double waterFlow;
	private final WaterTankStatus status;
	private final Double temperature;
	private final Double humidity;

	public WaterTankFilling(WaterTankStatus status, double volume, Double percentage) {
		this(status, volume, percentage, null,  null, null);
	}

	public WaterTankFilling(WaterTankStatus status, double volume, Double percentage, Double waterFlow, Double temperature, Double humidity) {

		this.status = status;
		this.volume = Math.floor(volume * 100D) / 100D;
		this.percentage = Math.floor(percentage * 100D) / 100D;
		this.waterFlow = waterFlow == null? null: Math.floor(waterFlow * 100D) / 100D;
		this.temperature = temperature;
		this.humidity = humidity;
	}

	public JsonObject toJsonObject() {
		var json = new JsonObject();

		if( volume != null) json.addProperty("volume", volume);
		if( percentage != null) json.addProperty("percentage", percentage);
		if( waterFlow != null) json.addProperty("waterFlow", waterFlow);

		return json;
	}

	public static String getSCHEMA() {
		return SCHEMA;
	}

	public WaterTankStatus getStatus() {
		return status;
	}

	public Double getVolume() {
		return volume;
	}

	public Double getPercentage() {
		return percentage;
	}

	public Double getWaterFlow() {
		return waterFlow;
	}

	public Double getTemperature() {
		return temperature;
	}

	public Double getHumidity() {
		return humidity;
	}
}
