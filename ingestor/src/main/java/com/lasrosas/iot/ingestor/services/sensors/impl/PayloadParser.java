package com.lasrosas.iot.ingestor.services.sensors.impl;

import java.util.List;

import com.lasrosas.iot.ingestor.services.sensors.api.ThingDataMessage;
import com.lasrosas.iot.shared.telemetry.Telemetry;

public interface PayloadParser {
	String getManufacturer();
	String getModel();
	ThingDataMessage decodeUplink(byte[] data);
	byte[] encodeDownlink(Object frame);
	List<Telemetry> telemetries(ThingDataMessage message);
}
