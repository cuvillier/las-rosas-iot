package com.lasrosas.iot.core.shared.telemetry;

import com.google.gson.JsonObject;

public class WaterTankFilling extends Telemetry {
	public static String SCHEMA = "WaterTankFilling";
	private Double volume;
	private Integer percentage;
	private Double waterFlow;

	public WaterTankFilling(double volume, int percentage, Double waterFlow) {
		super();
		this.volume = volume;
		this.percentage = percentage;
		this.waterFlow = waterFlow;
	}

	public JsonObject toJsonObject() {
		var json = new JsonObject();

		if( volume != null) json.addProperty("volume", volume);
		if( percentage != null) json.addProperty("percentage", percentage);
		if( waterFlow != null) json.addProperty("waterFlow", waterFlow);

		return json;
	}
}
