package com.lasrosas.iot.services.lora.parser.impl.elsys;

import java.util.ArrayList;
import java.util.List;

import com.lasrosas.iot.services.lora.MessageHolder;
import com.lasrosas.iot.services.lora.parser.PayloadParser;

public class ElsysERSParser implements PayloadParser {

	@Override
	public List<MessageHolder> decode(byte [] data) {
		var result = new ArrayList<MessageHolder>();
		return result;
	}

	@Override
	public List<MessageHolder> normalize(MessageHolder decodedMessage) {
		var result = new ArrayList<MessageHolder>();
		return result;
	}

	public String getManufacturer() {
		return "Elsys";
	}

	public String getModel() {
		return "ERS";
	}

}
