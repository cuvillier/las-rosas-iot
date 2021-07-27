package com.lasrosas.iot.ingestor.services.lora.api;

public class LoraMessageAck extends LoraMessage {
	private String deveui;
	private String gatewayId;

	public String getDeveui() {
		return deveui;
	}
	public void setDeveui(String deveui) {
		this.deveui = deveui;
	}
	public String getGatewayId() {
		return gatewayId;
	}
	public void setGatewayId(String gatewayId) {
		this.gatewayId = gatewayId;
	}
}
