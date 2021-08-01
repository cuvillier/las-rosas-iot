package com.lasrosas.iot.ingestor.services.lora.api;

public class LoraMessageJoin extends LoraMessage  {
	private String manufacturer;
	private String model;
	private String gatewayId;

	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getGatewayId() {
		return gatewayId;
	}
	public void setGatewayId(String gatewayId) {
		this.gatewayId = gatewayId;
	}

}
