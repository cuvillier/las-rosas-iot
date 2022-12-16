package com.lasrosas.iot.core.webapi.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;

import com.lasrosas.iot.core.database.entities.thg.Thing;
import com.lasrosas.iot.core.database.entities.thg.Thing.AdminState;
import com.lasrosas.iot.core.database.entities.thg.ThingGateway;
import com.lasrosas.iot.core.database.entities.thg.ThingType;
import com.lasrosas.iot.core.webapi.WebAlarmState;
import com.lasrosas.iot.core.webapi.model.WebTimeSerieModel.WebTimeSerieSummary;

public abstract class WebThingModel {

	public static record WebGatewaySummary(long techid, String naturalId, String type) {}

	public static record WebThingId(long techid, String naturalId) {}

	public static record WebThingTypeId(long techid, String manufacturer, String model) {}

	public static record WebThingSummary (
			long techid,
			String kind,
			String naturalId,
			LocalDateTime lastSeen,
			AdminState adminState,
			WebAlarmState batteryState,
			boolean connected,
			Properties kindProperties,
			WebThingTypeId thingType,
			WebGatewaySummary gateway
			) {}

	public static record WebThingDetails (
			long techid,
			String kind,
			String naturalId,
			LocalDateTime lastSeen,
			AdminState adminState,
			WebAlarmState batteryState,
			boolean connected,
			String jsonValues,
			Properties kindProperties,
			WebThingTypeId thingType,
			WebGatewaySummary gateway,
			List<WebTimeSerieSummary> timeSeries
	) {}


	/* Mapping methods */

	public static WebThingSummary mapWebThingSummary(Thing thing) {
		if(thing == null) return null;

		var lastSeen = thing.getProxy() == null? null: thing.getProxy().getLastSeen();

		return new WebThingSummary(
			thing.getTechid(), 
			thing.getKind(), 
			thing.getNaturalId(), 
			lastSeen,
			thing.getAdminState(),
			WebAlarmState.OK,
			true,
			null,
			mapWebThingTypeId(thing.getType()),
			mapWebGatewaySummary(thing.getGateway())
		);
	}

	public static WebGatewaySummary mapWebGatewaySummary(ThingGateway gateway) {
		if(gateway == null) return null;
		return new WebGatewaySummary(gateway.getTechid(), gateway.getNaturalId(), gateway.getTypeName());				
	}

	public  static WebThingTypeId mapWebThingTypeId(ThingType thingType) {
		if(thingType == null) return null;
		return new WebThingTypeId(thingType.getTechid(), thingType.getManufacturer(), thingType.getModel());				
	}
}
