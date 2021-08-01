package com.lasrosas.iot.ingestor.services.sensors.impl.adeunis;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.cfg.NotYetImplementedException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lasrosas.iot.ingestor.ThingMessageHolder;
import com.lasrosas.iot.ingestor.services.sensors.api.ThingDataMessage;
import com.lasrosas.iot.ingestor.services.sensors.impl.PayloadParser;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8170BAFrame.UplinkFrame0x10;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8180BAFrame.BaseFrame;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8180BAFrame.Frame0x30x43;
import com.lasrosas.iot.shared.telemetry.AirEnvironment;
import com.lasrosas.iot.shared.telemetry.BatteryLevel;
import com.lasrosas.iot.shared.telemetry.Telemetry;

public class AdeunisARF8170BAParser implements PayloadParser {
	public static final String MANUFACTURER = "Adeunis";
	public static final String MODEL = "ARF8170BA";

	public static Gson gson = new GsonBuilder().create();

	private AdeunisARF8170BAFrameDecoder decoder = new AdeunisARF8170BAFrameDecoder();

	public void updateConfig(Object data ) {
		
	}
	public Adeunis8170BAConfiguration decodeConfig(String json) {
		return gson.fromJson(json, Adeunis8170BAConfiguration.class);
	}

	public void updateConfig(Adeunis8170BAConfiguration config, UplinkFrame0x10 frame) {
		config.setChannel1Type(frame.getS320_Channel1Configuration().getType());
		config.setChannel2Type(frame.getS321_Channel2Configuration().getType());
		config.setChannel3Type(frame.getS322_Channel3Configuration().getType());
		config.setChannel4Type(frame.getS323_Channel4Configuration().getType());
	}

	@Override
	public ThingDataMessage decodeUplink(byte[] payload) {
		return decoder.decode(payload);
	}

	@Override
	public byte[] encodeDownlink(Object frame) {
		throw new NotYetImplementedException();
	}

	@Override
	public List<Telemetry> telemetries(ThingDataMessage message) {
		var result = new ArrayList<Telemetry>();

		if(message instanceof Frame0x30x43 ) {
			Frame0x30x43 frame = (Frame0x30x43)message;

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

		if(message instanceof Frame0x30x43 ) {
			BaseFrame frame = (BaseFrame)message;
			var normalized = new BatteryLevel(frame.getStatus().isLowBat());
			result.add(normalized);
		}

		return result;
	}

	public String getManufacturer() {
		return MANUFACTURER;
	}

	public String getModel() {
		return MODEL;
	}
}
