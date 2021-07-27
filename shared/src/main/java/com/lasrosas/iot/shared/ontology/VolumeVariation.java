package com.lasrosas.iot.shared.ontology;

public class VolumeVariation implements Ontology {
	private Double volumeDerivative;
	private Double percentageDerivation;

	public VolumeVariation(Double volumeDerivative, Double percentageDerivation) {
		super();
		this.volumeDerivative = volumeDerivative;
		this.percentageDerivation = percentageDerivation;
	}

	public Double getPercentageDerivation() {
		return percentageDerivation;
	}

	public void setPercentageDerivation(Double percentageDerivation) {
		this.percentageDerivation = percentageDerivation;
	}

	public Double getVolumeDerivative() {
		return volumeDerivative;
	}

	public void setVolumeDerivative(Double volumeDerivative) {
		this.volumeDerivative = volumeDerivative;
	}

}
