package com.lasrosas.iot.core.ingestor.lora.api;

import com.lasrosas.iot.core.shared.telemetry.Telemetry;

public class LoraMetricMessage extends Telemetry {
	public int port;
	public int cnt;
	public Integer rssi;
	public Float snr;
	public Long frequency;

	public LoraMetricMessage() {
	}

	public LoraMetricMessage(int port, int cnt, Integer rssi, Float snr, Long frequency) {
		this.port = port;
		this.cnt = cnt;
		this.rssi = rssi;
		this.snr = snr;
		this.frequency = frequency;
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
