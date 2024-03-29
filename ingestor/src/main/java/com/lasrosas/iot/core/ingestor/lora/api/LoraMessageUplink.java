package com.lasrosas.iot.core.ingestor.lora.api;

public class LoraMessageUplink extends LoraMessage {
	public String data;
	public String dataEncoding;

	
	public LoraMessageUplink() {
		super();
	}

	public LoraMessageUplink(String deveui, String data, String dataEncoding, int port, int cnt, Integer rssi,
			Float snr, Long frequency) {
		super(deveui);
		this.data = data;
		this.dataEncoding = dataEncoding;
		this.port = port;
		this.cnt = cnt;
		this.rssi = rssi;
		this.snr = snr;
		this.frequency = frequency;
	}



	public int port;

	public int cnt;

	public Integer rssi;

	public Float snr;

	public Long frequency;

	public Long getFrequency() {
		return frequency;
	}
	public void setFrequency(Long frequency) {
		this.frequency = frequency;
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
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getDataEncoding() {
		return dataEncoding;
	}
	public void setDataEncoding(String dataEncoding) {
		this.dataEncoding = dataEncoding;
	}
}
