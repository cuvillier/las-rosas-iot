package com.lasrosas.iot.core.ingestor.parsers.impl.elsys;

import com.lasrosas.iot.core.ingestor.parsers.api.ThingDataMessage;

public class ElsysGenericUplinkFrame extends ThingDataMessage {
    private Double temperature;
    private Integer humidity;
    private Integer accelerationX;
    private Integer accelerationY;
    private Integer accelerationZ;
    private Integer light;
    private Integer motion;
    private Integer co2;
    private Integer vdd;
    private Integer analog1;
    private Integer gpsLatitude;
    private Integer gpsLongitude;
    private Integer pulse1;
    private Integer pulseAbs;
    private Double externalTemperature;
    private Integer digital;
    private Integer distance;
    private Integer accMotion;
    private Double irInternalTemperature;
    private Double irExternalTemperature;
    private Integer occupancy;
    private Integer waterleak;
    private Double pressure;
    private Integer soundPeak;
    private Integer soundAvg;
    private Integer pulse2;
    private Integer pulseAbs2;
    private Integer analog2;
    private Double externalTemperature2;
    private double [] grideye;
    private Integer analogUV;

    public Double getTemperature() {
		return temperature;
	}
	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}
	public Integer getHumidity() {
		return humidity;
	}
	public void setHumidity(Integer humidity) {
		this.humidity = humidity;
	}
	public Integer getAccelerationX() {
		return accelerationX;
	}
	public void setAccelerationX(Integer accelerationX) {
		this.accelerationX = accelerationX;
	}
	public Integer getAccelerationY() {
		return accelerationY;
	}
	public void setAccelerationY(Integer accelerationY) {
		this.accelerationY = accelerationY;
	}
	public Integer getAccelerationZ() {
		return accelerationZ;
	}
	public void setAccelerationZ(Integer accelerationZ) {
		this.accelerationZ = accelerationZ;
	}
	public Integer getLight() {
		return light;
	}
	public void setLight(Integer light) {
		this.light = light;
	}
	public Integer getMotion() {
		return motion;
	}
	public void setMotion(Integer motion) {
		this.motion = motion;
	}
	public Integer getCo2() {
		return co2;
	}
	public void setCo2(Integer co2) {
		this.co2 = co2;
	}
	public Integer getVdd() {
		return vdd;
	}
	public void setVdd(Integer vdd) {
		this.vdd = vdd;
	}
	public Integer getAnalog1() {
		return analog1;
	}
	public void setAnalog1(Integer analog1) {
		this.analog1 = analog1;
	}
	public Integer getGpsLatitude() {
		return gpsLatitude;
	}
	public void setGpsLatitude(Integer gpsLatitude) {
		this.gpsLatitude = gpsLatitude;
	}
	public Integer getGpsLongitude() {
		return gpsLongitude;
	}
	public void setGpsLongitude(Integer gpsLongitude) {
		this.gpsLongitude = gpsLongitude;
	}
	public Integer getPulse1() {
		return pulse1;
	}
	public void setPulse1(Integer pulse1) {
		this.pulse1 = pulse1;
	}
	public Integer getPulseAbs() {
		return pulseAbs;
	}
	public void setPulseAbs(Integer pulseAbs) {
		this.pulseAbs = pulseAbs;
	}
	public Double getExternalTemperature() {
		return externalTemperature;
	}
	public void setExternalTemperature(Double externalTemperature) {
		this.externalTemperature = externalTemperature;
	}
	public Integer getDigital() {
		return digital;
	}
	public void setDigital(Integer digital) {
		this.digital = digital;
	}
	public Integer getDistance() {
		return distance;
	}
	public void setDistance(Integer distance) {
		this.distance = distance;
	}
	public Integer getAccMotion() {
		return accMotion;
	}
	public void setAccMotion(Integer accMotion) {
		this.accMotion = accMotion;
	}
	public Double getIrInternalTemperature() {
		return irInternalTemperature;
	}
	public void setIrInternalTemperature(Double irIntegerernalTemperature) {
		irInternalTemperature = irIntegerernalTemperature;
	}
	public Double getIrExternalTemperature() {
		return irExternalTemperature;
	}
	public void setIrExternalTemperature(Double irExternalTemperature) {
		this.irExternalTemperature = irExternalTemperature;
	}
	public Integer getOccupancy() {
		return occupancy;
	}
	public void setOccupancy(Integer occupancy) {
		this.occupancy = occupancy;
	}
	public Integer getWaterleak() {
		return waterleak;
	}
	public void setWaterleak(Integer waterleak) {
		this.waterleak = waterleak;
	}
	public Double getPressure() {
		return pressure;
	}
	public void setPressure(Double pressure) {
		this.pressure = pressure;
	}
	public Integer getSoundPeak() {
		return soundPeak;
	}
	public void setSoundPeak(Integer soundPeak) {
		this.soundPeak = soundPeak;
	}
	public Integer getSoundAvg() {
		return soundAvg;
	}
	public void setSoundAvg(Integer soundAvg) {
		this.soundAvg = soundAvg;
	}
	public Integer getPulse2() {
		return pulse2;
	}
	public void setPulse2(Integer pulse2) {
		this.pulse2 = pulse2;
	}
	public Integer getPulseAbs2() {
		return pulseAbs2;
	}
	public void setPulseAbs2(Integer pulseAbs2) {
		this.pulseAbs2 = pulseAbs2;
	}
	public Integer getAnalog2() {
		return analog2;
	}
	public void setAnalog2(Integer analog2) {
		this.analog2 = analog2;
	}
	public Double getExternalTemperature2() {
		return externalTemperature2;
	}
	public void setExternalTemperature2(Double externalTemperature2) {
		this.externalTemperature2 = externalTemperature2;
	}
	public double[] getGrideye() {
		return grideye;
	}
	public void setGrideye(double[] grideye) {
		this.grideye = grideye;
	}
	public Integer getAnalogUV() {
		return analogUV;
	}
	public void setAnalogUV(Integer analogUV) {
		this.analogUV = analogUV;
	}
}
