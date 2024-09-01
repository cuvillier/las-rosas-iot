package com.lasrosas.iot.ingestor.domain.model.digitalTwin.twins;

import com.lasrosas.iot.ingestor.domain.model.digitalTwin.DigitalTwin;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@SuperBuilder
@Getter
@Setter
public class WaterTank extends DigitalTwin {
	public static final Logger log = LoggerFactory.getLogger(WaterTank.class);

	private LocalDateTime updateTime;
	private Double length;
	private Double radius;
	private Double sensorAltitude;
	private Double level;
	private Double volume;
	private Double percentageFill;
	private Double waterFlow;
	private Double maxWaterFlow;
	private Double humidity;
	private Double temperature;

	@Builder.Default
	private WaterTankStatus status = WaterTankStatus.UNKNOWN;

	public Double getVolumeMax() {
		return Math.PI * Math.pow(radius, 2) * length;
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

	public void updateLevel(LocalDateTime time, Double levelMeasured) {

		var newVolume = computeVolume(levelMeasured);
		this.waterFlow = computeWaterFlow(this.updateTime, this.volume, time, newVolume);

		this.volume = newVolume; 
		this.updateTime = time;
		this.percentageFill = computeFillPercent(this.volume);
		this.level = levelMeasured;

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

	public void setLevel(Double levelMeasured) {
		this.level = levelMeasured;
		
		onLevelChanged();
	}
}
