package com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers;

import com.lasrosas.iot.ingestor.domain.model.message.ThingMessage;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.LorawanMessageUplinkRx;

import java.util.List;

public interface ThingDriver {
	String getManufacturer();
	String getModel();

	ThingMessage decodeUplink(LorawanMessageUplinkRx uplink);
	List<ThingMessage> normalize(ThingMessage message);
}
