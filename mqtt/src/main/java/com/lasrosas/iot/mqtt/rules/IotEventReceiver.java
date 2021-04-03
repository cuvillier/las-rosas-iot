package com.lasrosas.iot.mqtt.rules;

public class IotEventReceiver {
	private final DataChangeEngine ruleManager;

	public IotEventReceiver(DataChangeEngine ruleManager) {
		this.ruleManager = ruleManager;
	}

	public void handle(IotEvent event) {
		if(event instanceof DataChangeEvent)
			handleDataChanged((DataChangeEvent)event);
		else
			throw new RuntimeException();
	}

	private void handleDataChanged(DataChangeEvent event) {
		ruleManager.handleDataChange(event.getDataChange());
	}
}
