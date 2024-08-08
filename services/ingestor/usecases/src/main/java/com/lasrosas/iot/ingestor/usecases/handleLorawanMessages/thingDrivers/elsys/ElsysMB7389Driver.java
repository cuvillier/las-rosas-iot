package com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.elsys;

import com.lasrosas.iot.ingestor.domain.model.message.BaseMessage;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.LorawanMessageUplinkRx;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.ThingDriver;

import java.util.List;

public class ElsysMB7389Driver implements ThingDriver {

	private ElsysGenericDriver parser ;

	public ElsysMB7389Driver(ElsysGenericDriver parser ) {
		this.parser = parser;
	}

	@Override
	public BaseMessage decodeUplink(LorawanMessageUplinkRx uplink) {
		return parser.decodeUplink(uplink);
	}

	@Override
	public List<BaseMessage> normalize(BaseMessage message) {
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
