package com.lasrosas.iot.core.shared.telemetry;

public class DistanceMeasurement extends Telemetry {
	public static final String SCHEMA = "DistanceMeasurement";

	private Double distance;

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}
}
