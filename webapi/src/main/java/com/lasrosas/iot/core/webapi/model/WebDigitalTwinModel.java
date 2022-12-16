package com.lasrosas.iot.core.webapi.model;

import java.util.List;

import com.lasrosas.iot.core.webapi.model.WebThingModel.WebThingId;

public record WebDigitalTwinModel() {

	public static record WebDigitalTwinId(long techid, String naturalId) {
	}

	public record WebDigitalTwinSummary(long techid, String naturalId, List<WebDigitalTwinId> partOf, List<WebReceiverSummary> receivers) {
	}

	public record WebReceiverSummary(long techid, String role, WebThingId thingId, WebDigitalTwinId dtwinId) {

	}
}
