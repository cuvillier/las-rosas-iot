package com.lasrosas.iot.ingestor.services.sensors.api;

public interface DecodableMessage {
	String getManufacturer();
	String getModel();
	String getEncodedData();
	String getDataEncoding();
}
