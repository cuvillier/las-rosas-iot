package com.lasrosas.iot.core.ingestor.gateway.impl.rak7249.api;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import com.lasrosas.iot.core.shared.telemetry.PartOfState;

public class Rak7249MessageRx extends Rak7249Message {
	/*
		{
			"applicationID": "3",
			"applicationName": "las-rosas-iot",
			"devEUI": "0018b2200000093c",
			"deviceName": "Adenuis/ARF8180BA/0018B2200000093C",
			"timestamp": 1620078842,
			"fCnt": 6694,
			"fPort": 1,
			"data": "Q6ABAP8CAPw=",
			"data_encode": "base64",
			"adr": true,
			"rxInfo": [
				{
					"gatewayID": "60c5a8fffe76f8b2",
					"loRaSNR": 8.3,
					"rssi": -70,
					"location": {
						"latitude": 36.825600,
						"longitude": -5.579390,
						"altitude": 279
					},
					"time": "2021-07-18T07:50:17.653651Z"
				}
			],
			"txInfo": {
				"frequency": 868500000,
				"dr": 0
			}
		}
*/
	private Integer applicationID;
	private String applicationName;
	private String devEUI;
	private String deviceName;
	private long timestamp;

	private int fCnt;

	private int fPort;
	private String data;
	private String data_encode;

	public static class RxInfo {
		private String gatewayID;
		private Float loRaSNR;
		private Integer rssi;

		public RxInfo() {
		}

		public RxInfo(String gatewayID, Float loRaSNR, Integer rssi) {
			super();
			this.gatewayID = gatewayID;
			this.loRaSNR = loRaSNR;
			this.rssi = rssi;
		}

		public String getGatewayID() {
			return gatewayID;
		}
		public void setGatewayID(String gatewayID) {
			this.gatewayID = gatewayID;
		}
		public Float getLoRaSNR() {
			return loRaSNR;
		}
		public void setLoRaSNR(Float loRaSNR) {
			this.loRaSNR = loRaSNR;
		}
		public Integer getRssi() {
			return rssi;
		}
		public void setRssi(Integer rssi) {
			this.rssi = rssi;
		}
	}

	@PartOfState
	private List<RxInfo> rxInfo = new ArrayList<>();

	public static class TxInfo {

		private Long frequency;

		private Integer dr;

		public TxInfo(Long frequency, Integer dr) {
			this.frequency = frequency;
			this.dr = dr;
		}

		public TxInfo() {
		}

		public Long getFrequency() {
			return frequency;
		}
		public void setFrequency(Long frequency) {
			this.frequency = frequency;
		}
		public Integer getDr() {
			return dr;
		}
		public void setDr(Integer dr) {
			this.dr = dr;
		}
	}
	
	public Rak7249MessageRx() {
		super();
	}

	@PartOfState
	private TxInfo txInfo;

	public Integer getApplicationID() {
		return applicationID;
	}

	public void setApplicationID(Integer applicationID) {
		this.applicationID = applicationID;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getDevEUI() {
		return devEUI;
	}

	public void setDevEUI(String devEUI) {
		this.devEUI = devEUI;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public int getFCnt() {
		return fCnt;
	}

	public void setFCnt(int fCnt) {
		this.fCnt = fCnt;
	}

	public int getFPort() {
		return fPort;
	}

	public void setFPort(int fPort) {
		this.fPort = fPort;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getData_encode() {
		return data_encode;
	}

	public void setData_encode(String data_encode) {
		this.data_encode = data_encode;
	}

	public List<RxInfo> getRxInfo() {
		return rxInfo;
	}

	public void addRxInfo(RxInfo rxInfo) {
		this.rxInfo.add(rxInfo);
	}

	public void setRxInfo(List<RxInfo> rxInfo) {
		this.rxInfo = rxInfo;
	}

	public TxInfo getTxInfo() {
		return txInfo;
	}

	public void setTxInfo(TxInfo txInfo) {
		this.txInfo = txInfo;
	}
}
