package com.lasrosas.iot.core.ingestor.sensors.api;

public interface DecodableMessage {
	String getManufacturer();
	String getModel();
	String getEncodedData();
	String getDataEncoding();
}
