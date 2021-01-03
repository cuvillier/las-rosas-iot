package com.lasrosas.iot.shared.rules;

public class IotEventReceiver {
	private final DataChangeRuleManager ruleManager;

	public IotEventReceiver(DataChangeRuleManager ruleManager) {
		this.ruleManager = ruleManager;
	}

	public void handle(IotEvent event) {
		if(event instanceof DataChangedEvent)
			handleDataChanged((DataChangedEvent)event);
		else
			throw new RuntimeException();
	}

	private void handleDataChanged(DataChangedEvent event) {
		ruleManager.evaluate(event.getDataChange());
	}
}
