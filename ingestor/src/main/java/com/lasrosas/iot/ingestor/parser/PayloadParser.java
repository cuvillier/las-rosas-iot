package com.lasrosas.iot.ingestor.parser;

import java.util.List;

import com.lasrosas.iot.ingestor.ThingMessageHolder;

public interface PayloadParser {
	String getManufacturer();
	String getModel();
	List<ThingMessageHolder> parse(byte[] data);
	List<ThingMessageHolder> normalize(ThingMessageHolder decodedMessage);
}
