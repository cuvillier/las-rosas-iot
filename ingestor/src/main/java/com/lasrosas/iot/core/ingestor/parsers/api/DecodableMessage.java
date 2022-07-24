package com.lasrosas.iot.core.ingestor.parsers.api;

public interface DecodableMessage {
	String getManufacturer();
	String getModel();
	String getEncodedData();
	String getDataEncoding();
}
