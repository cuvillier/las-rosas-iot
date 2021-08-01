package com.lasrosas.iot.ingestor.services.lora.api;

public class LoraMetricMessage {
	public String deveui;
	public long timestamp;
	public int port;
	public int cnt;
	public Integer rssi;
	public Float snr;
	public Long frequency;

	public String getDeveui() {
		return deveui;
	}
	public void setDeveui(String deveui) {
		this.deveui = deveui;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
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
