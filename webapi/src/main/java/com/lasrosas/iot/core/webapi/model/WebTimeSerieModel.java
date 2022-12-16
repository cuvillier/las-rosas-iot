package com.lasrosas.iot.core.webapi.model;

import com.lasrosas.iot.core.webapi.model.WebDigitalTwinModel.WebDigitalTwinSummary;

public record WebTimeSerieModel() {
	public static record WebTimeSerieSummary (String type, String sensor, WebDigitalTwinSummary digitalTwinSummary, int count) {}	
}
