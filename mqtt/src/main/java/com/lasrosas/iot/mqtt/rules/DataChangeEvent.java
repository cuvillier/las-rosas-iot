package com.lasrosas.iot.mqtt.rules;

public class DataChangeEvent extends IotEvent {
	private DataChange dataChange;
	
	public DataChangeEvent(DataChange dataChange) {
		super();
		this.dataChange = dataChange;
	}

	public DataChange getDataChange() {
		return dataChange;
	}
}
