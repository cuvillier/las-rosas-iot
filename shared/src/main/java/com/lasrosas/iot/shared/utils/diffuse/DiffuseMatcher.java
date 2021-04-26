package com.lasrosas.iot.shared.utils.diffuse;

public class DiffuseMatcher {
	private String manufacturer;
	private String model;
	private Class<?> eventDataClass;

	public DiffuseMatcher() {
	}

	public DiffuseMatcher(String manufacturer, String model, Class<?> eventDataClass) {
		this.manufacturer = manufacturer;
		this.model = model;
		this.eventDataClass = eventDataClass;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public String getModel() {
		return model;
	}

	public Class<?> getEventDataClass() {
		return eventDataClass;
	}
}
