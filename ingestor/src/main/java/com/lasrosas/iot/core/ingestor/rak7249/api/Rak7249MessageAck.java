package com.lasrosas.iot.core.ingestor.rak7249.api;

/*
	topic: "application/3/device/70b3d58ff10184b8/ack"
	{
		"applicationID": "3",
		"applicationName": "las-rosas-iot",
		"deviceName": "MFC88-LW13IO-70b3d58ff10184b8",
		"devEUI": "70b3d58ff10184b8",
		"acknowledged": true,
		"fCnt": 54
	}
 */
public class Rak7249MessageAck extends Rak7249Message {
	public String applicationID;
	public String applicationName;
	public String deviceName;
	public String devEUI;
	public boolean acknowledge;
	public int fCnt;

	public String getApplicationID() {
		return applicationID;
	}
	public void setApplicationID(String applicationID) {
		this.applicationID = applicationID;
	}
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getDevEUI() {
		return devEUI;
	}
	public void setDevEUI(String devEUI) {
		this.devEUI = devEUI;
	}
	public boolean isAcknowledge() {
		return acknowledge;
	}
	public void setAcknowledge(boolean acknowledge) {
		this.acknowledge = acknowledge;
	}
	public int getfCnt() {
		return fCnt;
	}
	public void setfCnt(int fCnt) {
		this.fCnt = fCnt;
	}
}
