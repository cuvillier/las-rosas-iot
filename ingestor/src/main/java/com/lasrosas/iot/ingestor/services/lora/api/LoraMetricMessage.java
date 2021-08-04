package com.lasrosas.iot.ingestor.services.lora.api;

import java.time.LocalDateTime;

public class LoraMetricMessage {
	public long thingid;;
	public LocalDateTime time;;
	public int port;
	public int cnt;
	public Integer rssi;
	public Float snr;
	public Long frequency;

	public long getThingid() {
		return thingid;
	}
	public void setThingid(long thingid) {
		this.thingid = thingid;
	}
	public LocalDateTime getTime() {
		return time;
	}
	public void setTime(LocalDateTime time) {
		this.time = time;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public int getCnt() {
		return cnt;
	}
	public void setCnt(int cnt) {
		this.cnt = cnt;
	}
	public Integer getRssi() {
		return rssi;
	}
	public void setRssi(Integer rssi) {
		this.rssi = rssi;
	}
	public Float getSnr() {
		return snr;
	}
	public void setSnr(Float snr) {
		this.snr = snr;
	}
	public Long getFrequency() {
		return frequency;
	}
	public void setFrequency(Long frequency) {
		this.frequency = frequency;
	}
}
