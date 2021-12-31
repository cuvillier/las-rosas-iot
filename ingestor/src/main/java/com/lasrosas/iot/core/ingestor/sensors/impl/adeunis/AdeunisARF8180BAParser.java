package com.lasrosas.iot.core.ingestor.sensors.impl.adeunis;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import com.lasrosas.iot.core.ingestor.sensors.api.ThingDataMessage;
import com.lasrosas.iot.core.ingestor.sensors.api.ThingEncodedMessage;
import com.lasrosas.iot.core.ingestor.sensors.impl.PayloadParser;
import com.lasrosas.iot.core.ingestor.sensors.impl.adeunis.AdeunisARF8180BAFrame.UplinkFrame;
import com.lasrosas.iot.core.ingestor.sensors.impl.adeunis.AdeunisARF8180BAFrame.UplinkFrame0x30x43;
import com.lasrosas.iot.core.shared.telemetry.AirEnvironment;
import com.lasrosas.iot.core.shared.telemetry.BatteryLevel;
import com.lasrosas.iot.core.shared.telemetry.Order;
import com.lasrosas.iot.core.shared.telemetry.Telemetry;

public class AdeunisARF8180BAParser implements PayloadParser {
	private AdeunisTempFrameDecoder decoder = new AdeunisTempFrameDecoder();

	@Override
	public Message<ThingDataMessage> decodeUplink(Message<ThingEncodedMessage> imessage) {
		return decoder.decodeUplink(imessage);
	}

	@Override
	public byte[] encodeOrder(Order order) {
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
		return "Adeunis";
	}

	@Override
	public String getModel() {
		return "ARF8180BA";
	}
}
