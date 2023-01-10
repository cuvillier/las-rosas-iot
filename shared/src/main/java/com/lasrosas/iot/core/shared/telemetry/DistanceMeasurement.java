package com.lasrosas.iot.core.shared.telemetry;

public class DistanceMeasurement extends Telemetry {
	public static final String SCHEMA = "DistanceMeasurement";

	private Double distance;

	public DistanceMeasurement(Double distance) {
		this.distance = distance;
	}

	public DistanceMeasurement() {
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}
}
