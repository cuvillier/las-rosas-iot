package com.lasrosas.iot.services.lora;

import com.google.gson.JsonObject;

public abstract class LoraServer {
	private final String gatewayId;

	public LoraServer(String gatewayId) {
		this.gatewayId = gatewayId;
	}

	public String getGatewayId() {
		return gatewayId;
	}

	public String getgetType() {
		return getClass().getSimpleName();
	}

	public JsonObject normalize(JsonObject message) {
		return null;
	}

}
