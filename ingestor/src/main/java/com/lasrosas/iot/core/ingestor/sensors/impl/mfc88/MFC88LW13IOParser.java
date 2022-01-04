package com.lasrosas.iot.core.ingestor.sensors.impl.mfc88;

import java.util.ArrayList;
import java.util.List;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lasrosas.iot.core.ingestor.sensors.api.ThingDataMessage;
import com.lasrosas.iot.core.ingestor.sensors.api.ThingEncodedMessage;
import com.lasrosas.iot.core.ingestor.sensors.impl.PayloadParser;
import com.lasrosas.iot.core.ingestor.sensors.impl.mfc88.MFC88LW13IOFrame.UplinkIO;
import com.lasrosas.iot.core.shared.telemetry.Order;
import com.lasrosas.iot.core.shared.telemetry.Switched;
import com.lasrosas.iot.core.shared.telemetry.Telemetry;

public class MFC88LW13IOParser implements PayloadParser {
		public static final String MANUFACTURER = "MFC88";
		public static final String MODEL = "LW13IO";

		public static Gson gson = new GsonBuilder().create();

		private MFC88LW13IOFrameDecoder decoder = new MFC88LW13IOFrameDecoder();

		@Override
		public Message<ThingDataMessage> decodeUplink(Message<ThingEncodedMessage> imessage) {
			return decoder.decodeUplink(imessage);
		}

		@Override
		public byte[] encodeOrder(Order order) {
			return decoder.encodeOrder(order);
		}

		@Override
		public List<Message<Telemetry>> telemetries(Message<ThingDataMessage> imessage) {
			var result = new ArrayList<Message<Telemetry>>();

			var payload = imessage.getPayload();
			if( payload instanceof UplinkIO) {
				var uplinkIO = (UplinkIO)payload;
				var switched = new Switched(uplinkIO.getOutputs() != 0?1: 0);
				result.add(MessageBuilder.withPayload((Telemetry)switched).copyHeaders(imessage.getHeaders()).build());
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
