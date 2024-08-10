package com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers;

import com.lasrosas.iot.ingestor.domain.message.BaseMessage;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.LorawanMessageUplinkRx;

import java.util.List;

public interface ThingDriver {
	String getManufacturer();
	String getModel();

	BaseMessage decodeUplink(LorawanMessageUplinkRx uplink);
	List<BaseMessage> normalize(BaseMessage message);
}
