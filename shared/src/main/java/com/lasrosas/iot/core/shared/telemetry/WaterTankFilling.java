package com.lasrosas.iot.core.shared.telemetry;

import com.google.gson.JsonObject;

public class WaterTankFilling extends Telemetry {
	public static String SCHEMA = "WaterTankFilling";
	private Double volume;
	private Double percentage;
	private Double waterFlow;

	public WaterTankFilling(double volume, Double percentage) {
		this(volume, percentage, null);
	}

	public WaterTankFilling(double volume, Double percentage, Double waterFlow) {
		super();

		this.volume = Math.floor(volume * 100D) / 100D;
		this.percentage = Math.floor(percentage * 100D) / 100D;
		this.waterFlow = waterFlow == null? null: Math.floor(waterFlow * 100D) / 100D;
	}

	public JsonObject toJsonObject() {
		var json = new JsonObject();

		if( volume != null) json.addProperty("volume", volume);
		if( percentage != null) json.addProperty("percentage", percentage);
		if( waterFlow != null) json.addProperty("waterFlow", waterFlow);

		return json;
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

}
