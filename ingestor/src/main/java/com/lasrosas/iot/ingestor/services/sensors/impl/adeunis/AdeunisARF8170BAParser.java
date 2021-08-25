package com.lasrosas.iot.ingestor.services.sensors.impl.adeunis;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lasrosas.iot.ingestor.services.sensors.api.ThingDataMessage;
import com.lasrosas.iot.ingestor.services.sensors.api.ThingEncodedMessage;
import com.lasrosas.iot.ingestor.services.sensors.impl.PayloadParser;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8170BAFrame.UplinkFrame0x10;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8180BAFrame.UplinkFrame;
import com.lasrosas.iot.ingestor.services.sensors.impl.adeunis.AdeunisARF8180BAFrame.UplinkFrame0x30x43;
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
	public Message<? extends ThingDataMessage> decodeUplink(Message<ThingEncodedMessage> imessage) {
		return decoder.decodeUplink(imessage);
	}

	@Override
	public byte[] encodeDownlink(Message<?> frame) {
		throw new NotYetImplementedException();
	}

	@Override
	public List<Message<Telemetry>> telemetries(Message<ThingDataMessage> imessage) {
		var result = new ArrayList<Message<Telemetry>>();
		var message = imessage.getPayload();

		if(message instanceof UplinkFrame0x30x43 ) {
			UplinkFrame0x30x43 frame = (UplinkFrame0x30x43)message;

			var temp10thdeg = frame.getValueInternalSensor10thDeg();
			if(temp10thdeg != null) {

				var norm = new AirEnvironment();
				norm.setTemperature(temp10thdeg / 10.0);

				result.add(MessageBuilder.createMessage(norm, imessage.getHeaders()));
			}

			temp10thdeg = frame.getValueExternalSensor10thDeg();
			if(temp10thdeg != null) {

				var norm = new AirEnvironment();
				norm.setTemperature(temp10thdeg / 10.0);

				result.add(MessageBuilder.createMessage(norm, imessage.getHeaders()));
			}
		}

		if(message instanceof UplinkFrame0x30x43 ) {
			UplinkFrame frame = (UplinkFrame)message;
			var normalized = new BatteryLevel(frame.getStatus().isLowBat());
			result.add(MessageBuilder.createMessage(normalized, imessage.getHeaders()));
		}

		return result;
	}

	@Override
	public String getManufacturer() {
		return MANUFACTURER;
	}

	@Override
	public String getModel() {
		return MODEL;
	}
}
