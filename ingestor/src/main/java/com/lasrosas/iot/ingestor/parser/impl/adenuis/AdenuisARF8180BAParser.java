package com.lasrosas.iot.ingestor.parser.impl.adenuis;

import java.util.ArrayList;
import java.util.List;

import com.lasrosas.iot.ingestor.ThingMessageHolder;
import com.lasrosas.iot.ingestor.parser.PayloadParser;
import com.lasrosas.iot.ingestor.parser.impl.adenuis.AdenuisTempFrame.BaseFrame;
import com.lasrosas.iot.ingestor.parser.impl.adenuis.AdenuisTempFrame.Frame0x30x43;
import com.lasrosas.iot.shared.ontology.AirEnvironment;
import com.lasrosas.iot.shared.ontology.BatteryLevel;

public class AdenuisARF8180BAParser implements PayloadParser {
	private AdenuisTempParser parser = new AdenuisTempParser();

	@Override
	public List<ThingMessageHolder> decode(byte[] payload) {

		var result = new ArrayList<ThingMessageHolder>();
		result.add(parser.parse(payload));

		return result;
	}

	@Override
	public List<ThingMessageHolder> normalize(ThingMessageHolder messageHolder) {
		var result = new ArrayList<ThingMessageHolder>();
		Object message = messageHolder.getMessage();

		if(message instanceof Frame0x30x43 ) {
			Frame0x30x43 frame = (Frame0x30x43)message;

			var temp10thdeg = frame.getValueInternalSensor10thDeg();
			if(temp10thdeg != null) {

				var norm = new AirEnvironment();
				norm.setTemperature(temp10thdeg / 10.0);

				result.add(new ThingMessageHolder(AirEnvironment.SCHEMA, "InternalSensor", norm));
			}

			temp10thdeg = frame.getValueExternalSensor10thDeg();
			if(temp10thdeg != null) {

				var norm = new AirEnvironment();
				norm.setTemperature(temp10thdeg / 10.0);

				result.add(new ThingMessageHolder(AirEnvironment.SCHEMA, "ExternalSensor", norm));
			}
		}

		if(message instanceof Frame0x30x43 ) {
			BaseFrame frame = (BaseFrame)message;
			var normalized = new BatteryLevel(frame.getStatus().isLowBat());

			result.add(new ThingMessageHolder(BatteryLevel.SCHEMA, null, normalized));
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
