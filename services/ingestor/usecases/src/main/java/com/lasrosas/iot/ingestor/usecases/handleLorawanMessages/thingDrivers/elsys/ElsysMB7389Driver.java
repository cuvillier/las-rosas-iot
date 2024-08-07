package com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.elsys;

import com.lasrosas.iot.ingestor.domain.model.message.ThingMessage;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.LorawanMessageUplinkRx;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.ThingDriver;

import java.util.List;

public class ElsysMB7389Driver implements ThingDriver {

	private ElsysGenericDriver parser ;

	public ElsysMB7389Driver(ElsysGenericDriver parser ) {
		this.parser = parser;
	}

	@Override
	public ThingMessage decodeUplink(LorawanMessageUplinkRx uplink) {
		return parser.decodeUplink(uplink);
	}

	@Override
	public List<ThingMessage> normalize(ThingMessage message) {
		return parser.normalize(message);
	}

	@Override
	public String getManufacturer() {
		return "Elsys";
	}

	@Override
	public String getModel() {
		return "MB7389";
	}
}
