package com.lasrosas.iot.shared.rules;

public class DataChangedEvent extends IotEvent {
	private DataChange dataChange;
	
	public DataChangedEvent(DataChange dataChange) {
		super();
		this.dataChange = dataChange;
	}

	public DataChange getDataChange() {
		return dataChange;
	}
}
