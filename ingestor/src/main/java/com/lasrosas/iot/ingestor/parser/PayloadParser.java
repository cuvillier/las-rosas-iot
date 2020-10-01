package com.lasrosas.iot.ingestor.parser;

import java.util.List;

import com.lasrosas.iot.ingestor.MessageHolder;

public interface PayloadParser {
	String getManufacturer();
	String getModel();
	List<MessageHolder> decode(byte[] data);
	List<MessageHolder> normalize(MessageHolder decodedMessage);
}
