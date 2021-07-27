package com.lasrosas.iot.ingestor.services.lora.api;

public interface DeviceNameParser {

	public class DeviceInfo {
		private final String manufacturer;
		private final String model;

		public String getManufacturer() {
			return manufacturer;
		}

		public DeviceInfo(String manufacturer, String model) {
			super();
			this.manufacturer = manufacturer;
			this.model = model;
		}
		public String getModel() {
			return model;
		}
	}

	DeviceInfo parse(String deviceName);
}
