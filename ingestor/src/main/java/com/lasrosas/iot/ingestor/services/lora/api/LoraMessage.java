package com.lasrosas.iot.ingestor.services.lora.api;

public abstract class LoraMessage {
	private boolean duplicate;
	private String topic;
	public boolean isDuplicate() {
		return duplicate;
	}
	public void setDuplicate(boolean duplicate) {
		this.duplicate = duplicate;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}

}

