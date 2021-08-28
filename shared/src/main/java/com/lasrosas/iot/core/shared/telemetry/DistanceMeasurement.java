package com.lasrosas.iot.core.shared.telemetry;

public class DistanceMeasurement extends Telemetry {
	public static final String SCHEMA = "com.lasrosas.iot.shared.ontology.DistanceMeasurement";

	private Double distance;

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}
}
