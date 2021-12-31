package com.lasrosas.iot.core.ingestor.gateway.impl.rak7249.api;

public class Rak7249MessageJoin extends Rak7249Message {
/*
	{
		"applicationID": "3",
		"applicationName": "las-rosas-iot",
		"deviceName": "MFC88-LW13IO-70b3d58ff10184b8",
		"devEUI": "70b3d58ff10184b8",
		"devAddr": "02000008"
	}
 */

	public String applicationID;
	public String applicationName;
	public String deviceName;
	public String devEUI;
	public String devAddr;
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
	public String getDevAddr() {
		return devAddr;
	}
	public void setDevAddr(String devAddr) {
		this.devAddr = devAddr;
	}
}
