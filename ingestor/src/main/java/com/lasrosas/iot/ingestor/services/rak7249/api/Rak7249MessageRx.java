package com.lasrosas.iot.ingestor.services.rak7249.api;

import java.time.LocalDateTime;

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
	public Integer applicationID;
	public String applicationName;
	public String devEUI;
	public String deviceName;
	public long timestamp;
	public int fCnt;
	public int fPort;
	public String data;
	public String dataEncode;

	public class RxInfo {
		public String gatewayID;
		public Float loRaSNR;
		public Integer rssi;
		public LocalDateTime time;
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
		public LocalDateTime getTime() {
			return time;
		}
		public void setTime(LocalDateTime time) {
			this.time = time;
		}
	}

	public RxInfo rxInfo;
	
	public class TxInfo {
		public Long frequency;
		public Integer dr;
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
	
	public TxInfo txInfo;

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

	public String getDataEncode() {
		return dataEncode;
	}

	public void setDataEncode(String dataEncode) {
		this.dataEncode = dataEncode;
	}

	public RxInfo getRxInfo() {
		return rxInfo;
	}

	public void setRxInfo(RxInfo rxInfo) {
		this.rxInfo = rxInfo;
	}

	public TxInfo getTxInfo() {
		return txInfo;
	}

	public void setTxInfo(TxInfo txInfo) {
		this.txInfo = txInfo;
	}
}
