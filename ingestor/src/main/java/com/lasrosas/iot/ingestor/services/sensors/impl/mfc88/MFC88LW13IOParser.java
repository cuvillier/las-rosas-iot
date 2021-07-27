package com.lasrosas.iot.ingestor.services.sensors.impl.mfc88;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lasrosas.iot.ingestor.ThingMessageHolder;
import com.lasrosas.iot.ingestor.services.sensors.impl.PayloadParser;
import com.lasrosas.iot.ingestor.services.sensors.impl.mfc88.MFC88LW13IOFrame.DownlinkFrame;
import com.lasrosas.iot.ingestor.services.sensors.impl.mfc88.MFC88LW13IOFrame.DownlinkIOMessage;

public class MFC88LW13IOParser implements PayloadParser {
		public static final String MANUFACTURER = "MFC88";
		public static final String MODEL = "LW13IO";

		public static Gson gson = new GsonBuilder().create();

		private MFC88LW13IOFrameDecoder decoder = new MFC88LW13IOFrameDecoder();

		@Override
		public List<ThingMessageHolder> decodeUplink(byte[] payload) {

			var result = new ArrayList<ThingMessageHolder>();
			result.add(decoder.decodeUplink(payload));

			return result;
		}

		@Override
		public byte[] encodeDownlink(Object frame) {
			return decoder.encodeDownlink((DownlinkFrame)frame);
		}

		@Override
		public List<ThingMessageHolder> normalize(ThingMessageHolder messageHolder) {
			var result = new ArrayList<ThingMessageHolder>();
//			Object message = messageHolder.getMessage();

			return result;
		}

		public byte[] switchOn() {
			var frame = new DownlinkIOMessage();
			// TODO: Set values
			return decoder.encodeDownlink(frame);
		}

		public byte[] switchOn(int seconds) {
			var frame = new DownlinkIOMessage();
			// TODO: Set values
			return decoder.encodeDownlink(frame);
		}

		public String getManufacturer() {
			return MANUFACTURER;
		}

		public String getModel() {
			return MODEL;
		}
}
