package com.lasrosas.iot.core.database.twins;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lasrosas.iot.core.database.entities.dtw.DigitalTwin;
import com.lasrosas.iot.core.shared.telemetry.WaterTankStatus;

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
	public static final String COL_UPDATE_TIME = PREFIX + "update_time";
	public static final String COL_PERCENTAGE = PREFIX + "percentage";
	public static final String COL_SENSOR_ALT = PREFIX + "sensor_alt";
	public static final String COL_WATER_FLOW = PREFIX + "water_flow";
	public static final String COL_MAX_WATER_FLOW = PREFIX + "max_water_flow";
	public static final String COL_STATUS = PREFIX + "status";
	public static final String COL_TEMPERATURE = PREFIX + "temperature";
	public static final String COL_HUMIDITY = PREFIX + "humidity";

	public static final double LEVEL_NORMAL_MAX = 95;
	public static final double LEVEL_WARNING_MAX = 50;
	public static final double LEVEL_ALARM_MAX = 25;
	public static final double LEVEL_EMPTY_MAX = 5;

	@Column(name=COL_UPDATE_TIME)
	private LocalDateTime updateTime;

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
	private Double percentageFill;

	@Column(name=COL_WATER_FLOW)
	private Double waterFlow;

	@Column(name=COL_MAX_WATER_FLOW)
	private Double maxWaterFlow;

	@Column(name=COL_HUMIDITY)
	private Double humidity;

	@Column(name=COL_TEMPERATURE)
	private Double temperature;

	
	@Column(name=COL_STATUS)
	@Enumerated(EnumType.STRING)
	private WaterTankStatus status;

	public WaterTank() {
		this.status = WaterTankStatus.UNKNOWN;
	}

	public WaterTank(double length, double radius, double sensorAltitude) {
		this();
		this.length = length;
		this.radius = radius;
		this.sensorAltitude = sensorAltitude;
	}

	public LocalDateTime getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
	}

	public Double getVolumeMax() {
		return Math.PI * Math.pow(radius, 2) * length;
	}

	public Double getVolume() {
		return volume;
	}

	private void onLevelChanged() {
		this.volume = computeVolume(level);
		this.percentageFill = computeFillPercent(this.volume);
	}

	private Double computeVolume(Double level) {
		double L = length;
		double D = radius * 2;
		double H = radius * 2 - (level - sensorAltitude);

		if(H < 0 ) H = 0;
		if(H > D) H = D;

		return (Math.acos(1-2*H/D)-Math.sin(2*Math.acos(1-2*H/D))/2)*Math.pow(D,2)*L/4;	
	}

	public Double computeFillPercent(Double volume) {
		var percentage = 100D * (volume/ getVolumeMax());
		return percentage;
	}

	public Double getPercentageFill() {
		return percentageFill;
	}

	public Double getLevel() {
		return level;
	}

	public void updateLevel(LocalDateTime time, Double levelMeasured) {

		var newVolume = computeVolume(levelMeasured);
		this.waterFlow = computeWaterFlow(this.updateTime, this.volume, time, newVolume);

		this.volume = newVolume; 
		this.updateTime = time;
		this.percentageFill = computeFillPercent(this.volume);
		this.level = levelMeasured;

		this.status = computeStatus();

		log.info("WaterTank status=" + this.status +"volume=" + this.volume + ", level=" + this.level + ", fill=" + percentageFill );
	}

	private static Double computeWaterFlow(LocalDateTime previousTime,Double previousVolume, LocalDateTime time, Double volume) {
		if(time == null || volume == null || previousTime == null || previousVolume == null) return null;

		var seconds = time.until(previousTime, ChronoUnit.SECONDS);
		var houres = seconds / (double)(60*60);	// Waterflow per hour
		if(Math.abs(houres) < 0.0000001) return null;

		var waterflow = (previousVolume - volume) / houres;

		log.debug("Compute waterFlow. waterflow=" + waterflow + " time frame=" + seconds + "s");

		return waterflow;
	}

	public WaterTankStatus computeStatus() {
		if(this.percentageFill == null) return WaterTankStatus.UNKNOWN;

		if(percentageFill > LEVEL_NORMAL_MAX)
			return WaterTankStatus.FULL;
		else if(percentageFill > LEVEL_WARNING_MAX)
			return  WaterTankStatus.NORMAL;
		else if(percentageFill > LEVEL_ALARM_MAX)
			return WaterTankStatus.WARNING;
		else if(percentageFill > LEVEL_EMPTY_MAX)
			return WaterTankStatus.ALARM;
		else
			return WaterTankStatus.EMPTY;
	}

	public WaterTankStatus getStatus() {
		return status;
	}

	public void setStatus(WaterTankStatus status) {
		this.status = status;
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

	public Double getHumidity() {
		return humidity;
	}

	public void setHumidity(Double humidity) {
		this.humidity = humidity;
	}

	public Double getTemperature() {
		return temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}
}
