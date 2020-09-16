package com.lasrosas.iot.services.lora.parser;

import java.util.List;

import com.lasrosas.iot.services.lora.MessageHolder;

public interface PayloadParser {
	String getManufacturer();
	String getModel();
	List<MessageHolder> decode(byte[] data);
	List<MessageHolder> normalize(MessageHolder decodedMessage);
}
