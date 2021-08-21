package com.lasrosas.iot.ingestor.services.sensors.impl.adeunis;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.cfg.NotYetImplementedException;

import com.lasrosas.iot.ingestor.services.sensors.api.ThingDataMessage;
import com.lasrosas.iot.ingestor.services.sensors.impl.PayloadParser;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8180BAFrame.UplinkFrame;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8180BAFrame.UplinkFrame0x30x43;
import com.lasrosas.iot.shared.telemetry.AirEnvironment;
import com.lasrosas.iot.shared.telemetry.BatteryLevel;
import com.lasrosas.iot.shared.telemetry.Telemetry;

public class AdeunisARF8180BAParser implements PayloadParser {
	private AdeunisTempFrameDecoder decoder = new AdeunisTempFrameDecoder();

	@Override
	public ThingDataMessage decodeUplink(byte[] payload) {
		return decoder.decodeUplink(payload);
	}

	@Override
	public byte[] encodeDownlink(Object frame) {
		throw new NotYetImplementedException();
	}


	@Override
	public List<Telemetry> telemetries(ThingDataMessage message) {
		var result = new ArrayList<Telemetry>();

		if(message instanceof UplinkFrame0x30x43 ) {
			UplinkFrame0x30x43 frame = (UplinkFrame0x30x43)message;

			var temp10thdeg = frame.getValueInternalSensor10thDeg();
			if(temp10thdeg != null) {

				var norm = new AirEnvironment();
				norm.setTemperature(temp10thdeg / 10.0);

				result.add(norm);
			}

			temp10thdeg = frame.getValueExternalSensor10thDeg();
			if(temp10thdeg != null) {

				var norm = new AirEnvironment();
				norm.setTemperature(temp10thdeg / 10.0);

				result.add(norm);
			}
		}

		if(message instanceof UplinkFrame0x30x43 ) {
			UplinkFrame frame = (UplinkFrame)message;
			var normalized = new BatteryLevel(frame.getStatus().isLowBat());

			result.add(normalized);
		}

		return result;
	}

	public String getManufacturer() {
		return "Adeunis";
	}

	public String getModel() {
		return "ARF8180BA";
	}
}
