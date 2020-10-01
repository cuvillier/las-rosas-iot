package com.lasrosas.iot.shared.ontology;

public class DistanceMeasure extends Ontology {
	public static final String SCHEMA = "DistanceMeasure";

	private Double distance;

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}
}
