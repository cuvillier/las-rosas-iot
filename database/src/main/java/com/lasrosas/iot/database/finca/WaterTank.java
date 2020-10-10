package com.lasrosas.iot.database.finca;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.GsonBuilder;
import com.lasrosas.iot.database.entities.dtw.DigitalTwin;
import com.lasrosas.iot.database.entities.tsr.TimeSeriePoint;
import com.lasrosas.iot.database.entities.tsr.TimeSerieType;
import com.lasrosas.iot.shared.ontology.AirEnvironment;
import com.lasrosas.iot.shared.ontology.DistanceMeasurement;

@Entity
@DiscriminatorValue(WaterTank.DISCRIMINATOR)
public class WaterTank extends DigitalTwin {
	public static final Logger log = LoggerFactory.getLogger(WaterTank.class);
	
	public static final String PREFIX = "WAT_";
	public static final String DISCRIMINATOR = "WAT";

	public static final String COL_LENGTH = PREFIX + "LENGTH";
	public static final String COL_RADIUS = PREFIX + "RADIUS";
	public static final String COL_VOLUME_MAX = PREFIX + "VOLUME_MAX";
	public static final String COL_LEVEL = PREFIX + "LEVEL";
	public static final String COL_VOLUME = PREFIX + "VOLUME";
	public static final String COL_PERCENTAGE = PREFIX + "PERCENTAGE";
	public static final String COL_SENSOR_ALT = PREFIX + "SENSOR_ALT";

	@Column(name=COL_LENGTH)
	private Double length;

	@Column(name=COL_RADIUS)
	private Double radius;

	@Column(name=COL_SENSOR_ALT)
	private Double sensorAltitude;

	@Column(name=COL_LEVEL)
	private Double level;

	@Column(name=COL_VOLUME)
	private Double volume;

	@Column(name=COL_PERCENTAGE)
	private Integer percentageFill;

	public WaterTank() {
	}

	public WaterTank(double length, double radius, double sensorAltitude) {
		this.length = length;
		this.radius = radius;
		this.sensorAltitude = sensorAltitude;
	}

	@Override
	protected void handleNewPoint(TimeSeriePoint point) {
		if( point.getTimeSerie().getType().getSchema().contentEquals(DistanceMeasurement.SCHEMA)) {
			var gson = new GsonBuilder().create();
			var distanceMeasure = gson.fromJson(point.getValue(), DistanceMeasurement.class);
			if(distanceMeasure.getDistance() != null) {
				setLevel(distanceMeasure.getDistance());
			}
		}
	}

	@Override
	protected boolean isInterestedBy(TimeSerieType tst) {
		return	tst.getSchema().equals(DistanceMeasurement.SCHEMA) ||
				tst.getSchema().equals(AirEnvironment.SCHEMA);
	}

	public Double getVolumeMax() {
		return Math.PI * Math.pow(radius, 2) * length;
	}

	public Double getVolume() {
		return volume;
	}

	private void onLevelChanged() {
		this.volume = computeVolume();
		this.percentageFill = computeFillPercent();
	}

	private Double computeVolume() {
		if( level == null) return null;

		double L = length;
		double D = radius * 2;
		double H = radius * 2 - (level - sensorAltitude);

		if(H < 0 ) H = 0;
		if(H > D) H = D;

		return (Math.acos(1-2*H/D)-Math.sin(2*Math.acos(1-2*H/D))/2)*Math.pow(D,2)*L/4;
		
	}

	public Integer computeFillPercent() {
		if( level == null)
			return null;
		else {
			var percentage = (int)(100*(this.volume/ getVolumeMax()) + 0.5);
			log.info("WaterTank volume=" + this.volume + " volumeMax=" + getVolumeMax() + " %=" + percentage);
			return percentage;
		}
	}

	public Integer getPercentageFill() {
		return percentageFill;
	}

	public Double getLevel() {
		return level;
	}

	public void setLevel(Double levelMeasured) {
		this.level = levelMeasured;
		
		onLevelChanged();
	}

	public Double getLength() {
		return length;
	}

	public void setLength(Double length) {
		this.length = length;
	}

	public Double getRadius() {
		return radius;
	}

	public void setRadius(Double radius) {
		this.radius = radius;
	}

	public Double getSensorAltitude() {
		return sensorAltitude;
	}

	public void setSensorAltitude(Double sensorAltitude) {
		this.sensorAltitude = sensorAltitude;
	}
}
