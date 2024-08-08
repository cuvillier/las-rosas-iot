package com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.elsys;

import com.lasrosas.iot.ingestor.domain.message.BaseMessage;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.LorawanMessageUplinkRx;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.ThingDriver;

import java.util.List;

public class ElsysErsDriver implements ThingDriver {

	private ElsysGenericDriver parser = new ElsysGenericDriver();

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
		return "ERS";
	}

}
