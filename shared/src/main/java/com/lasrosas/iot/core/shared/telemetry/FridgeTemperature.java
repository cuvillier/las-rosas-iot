package com.lasrosas.iot.core.shared.telemetry;

public class FridgeTemperature extends Telemetry {
	private Double insideHumidity;
	private Double insideTemp;
	private Double outsideTemp;
	private FridgeStatus status;

	public FridgeTemperature() {
		super();
	}

	public FridgeTemperature(FridgeStatus status, Double insideTemp, Double insideHumidity, Double outsideTemp) {
		super();
		this.status = status;
		this.insideHumidity = insideHumidity;
		this.insideTemp = insideTemp;
		this.outsideTemp = outsideTemp;
	}
	public Double getInsideHumidity() {
		return insideHumidity;
	}
	public Double getInsideTemp() {
		return insideTemp;
	}
	public Double getOutsideTemp() {
		return outsideTemp;
	}

	public FridgeStatus getStatus() {
		return status;
	}
}
