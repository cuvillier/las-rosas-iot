package com.lasrosas.iot.services.lora.parser.impl.adenuis;

import java.util.ArrayList;
import java.util.List;

import com.lasrosas.iot.services.lora.MessageHolder;
import com.lasrosas.iot.services.lora.parser.PayloadParser;
import com.lasrosas.iot.services.lora.parser.impl.adenuis.AdenuisTempFrame.BaseFrame;
import com.lasrosas.iot.services.lora.parser.impl.adenuis.AdenuisTempFrame.Frame0x30x43;
import com.lasrosas.iot.services.ontology.AirEnvironment;
import com.lasrosas.iot.services.ontology.BatteryLevel;

public class AdenuisARF8180BAParser implements PayloadParser {
	private AdenuisTempParser parser = new AdenuisTempParser();

	@Override
	public List<MessageHolder> decode(byte[] payload) {

		var result = new ArrayList<MessageHolder>();
		result.add(parser.parse(payload));

		return result;
	}

	@Override
	public List<MessageHolder> normalize(MessageHolder messageHolder) {
		var result = new ArrayList<MessageHolder>();
		Object message = messageHolder.getMessage();

		if(message instanceof Frame0x30x43 ) {
			Frame0x30x43 frame = (Frame0x30x43)message;

			var temp10thdeg = frame.getValueInternalSensor10thDeg();
			if(temp10thdeg != null) {

				var norm = new AirEnvironment();
				norm.setTemperature(temp10thdeg / 10.0);

				result.add(new MessageHolder(AirEnvironment.SCHEMA, "InternalSensor", norm));
			}

			temp10thdeg = frame.getValueExternalSensor10thDeg();
			if(temp10thdeg != null) {

				var norm = new AirEnvironment();
				norm.setTemperature(temp10thdeg / 10.0);

				result.add(new MessageHolder(AirEnvironment.SCHEMA, "ExternalSensor", norm));
			}
		}

		if(message instanceof Frame0x30x43 ) {
			BaseFrame frame = (BaseFrame)message;
			var normalized = new BatteryLevel();

			normalized.setPercentage(frame.getStatus().isLowBat()?0:1);

			result.add(new MessageHolder(BatteryLevel.SCHEMA, null, normalized));
		}

		return result;
	}

	public String getManufacturer() {
		return "Adenuis";
	}

	public String getModel() {
		return "ARF8180BA";
	}
}