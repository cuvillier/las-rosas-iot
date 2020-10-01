package com.lasrosas.iot.ingestor.parser.impl.elsys;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lasrosas.iot.ingestor.MessageHolder;
import com.lasrosas.iot.ingestor.parser.PayloadParser;

public class ElsysErsParser implements PayloadParser {

	@Autowired
	private ElsysGenericParser parser;

	@Override
	public List<MessageHolder> decode(byte[] data) {
		return parser.decode(data);
	}

	@Override
	public List<MessageHolder> normalize(MessageHolder decodedMessage) {
		return parser.normalize(decodedMessage);
	}

	public String getManufacturer() {
		return "Elsys";
	}

	public String getModel() {
		return "ERS";
	}

}
