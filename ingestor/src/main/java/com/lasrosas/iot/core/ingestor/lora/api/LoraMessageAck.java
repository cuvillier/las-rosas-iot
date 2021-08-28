package com.lasrosas.iot.core.ingestor.lora.api;

public class LoraMessageAck extends LoraMessage {
	private String gatewayId;

	public String getGatewayId() {
		return gatewayId;
	}
	public void setGatewayId(String gatewayId) {
		this.gatewayId = gatewayId;
	}
}
