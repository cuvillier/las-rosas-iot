package com.lasrosas.iot.core.database.finca;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lasrosas.iot.core.database.entities.dtw.DigitalTwin;

@Entity
@Table(name=WaterTank.TABLE)
@PrimaryKeyJoinColumn(name=WaterTank.COL_TECHID)
@DiscriminatorValue(WaterTank.DISCRIMINATOR)
public class WaterTank extends DigitalTwin {
	public static final Logger log = LoggerFactory.getLogger(WaterTank.class);
	
	public static final String TABLE = "t_dtw_water_tank";
	public static final String PREFIX = "wat_";
	public static final String DISCRIMINATOR = "wat";

	public static final String COL_LENGTH = PREFIX + "length";
	public static final String COL_RADIUS = PREFIX + "radius";
	public static final String COL_LEVEL = PREFIX + "level";
	public static final String COL_VOLUME = PREFIX + "volume";
	public static final String COL_PERCENTAGE = PREFIX + "percentage";
	public static final String COL_SENSOR_ALT = PREFIX + "sensor_alt";
	public static final String COL_WATER_FLOW = PREFIX + "water_flow";
	public static final String COL_MAX_WATER_FLOW = PREFIX + "max_water_flow";

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

	@Column(name=COL_WATER_FLOW)
	private Double waterFlow;

	@Column(name=COL_MAX_WATER_FLOW)
	private Double maxWaterFlow;

	public WaterTank() {
	}

	public WaterTank(double length, double radius, double sensorAltitude) {
		this.length = length;
		this.radius = radius;
		this.sensorAltitude = sensorAltitude;
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

	public void setLevel(LocalDateTime previousTime, Double currentVolume, LocalDateTime time, Double levelMeasured) {
		log.debug("Update watertank level to " + level + " m. volume=" + currentVolume);

		this.level = levelMeasured;

		// Compute volume
		onLevelChanged();

		// UPdate waterflow
		this.waterFlow = computeWaterFlow(time, this.volume, previousTime, currentVolume);
	}

	private static Double computeWaterFlow(LocalDateTime previousTime,Double previousVolume, LocalDateTime time, Double volume) {
		if(time == null || volume == null || previousTime == null || previousVolume == null) return null;

		var seconds = time.until(previousTime, ChronoUnit.SECONDS);
		var houres = seconds / (double)(60*60);	// Waterflow per hour
		if(houres < 0.0000001) return null;
		var waterflow = (previousVolume - volume) / houres;
		log.debug("Compute waterFlow. waterflow=" + waterflow + " time frame=" + seconds + "s");
		return waterflow;
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

	public Double getWaterFlow() {
		return waterFlow;
	}

	public Double getMaxWaterFlow() {
		return maxWaterFlow;
	}

	public void setMaxWaterFlow(Double maxWaterFlow) {
		this.maxWaterFlow = maxWaterFlow;
	}
}
