package com.lasrosas.iot.core.ingestor.sensors.impl.mfc88;

import java.util.Collections;
import java.util.List;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lasrosas.iot.core.ingestor.sensors.api.ThingDataMessage;
import com.lasrosas.iot.core.ingestor.sensors.api.ThingEncodedMessage;
import com.lasrosas.iot.core.ingestor.sensors.impl.PayloadParser;
import com.lasrosas.iot.core.ingestor.sensors.impl.mfc88.MFC88LW13IOFrame.DownlinkFrame;
import com.lasrosas.iot.core.ingestor.sensors.impl.mfc88.MFC88LW13IOFrame.DownlinkIOMessage;
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

		@SuppressWarnings("unchecked")
		@Override
		public byte[] encodeDownlink(Message<?> imessage) {
			return decoder.encodeDownlink((Message<DownlinkFrame>)imessage);
		}

		@Override
		public List<Message<Telemetry>> telemetries(Message<ThingDataMessage> imessage) {
			return Collections.emptyList();
		}

		public byte[] switchOn() {
			var frame = new DownlinkIOMessage();
			// TODO: Set values
			return decoder.encodeDownlink(MessageBuilder.withPayload(frame).build());
		}

		public byte[] switchOn(int seconds) {
			var frame = new DownlinkIOMessage();
			// TODO: Set values
			return decoder.encodeDownlink(MessageBuilder.withPayload(frame).build());
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
