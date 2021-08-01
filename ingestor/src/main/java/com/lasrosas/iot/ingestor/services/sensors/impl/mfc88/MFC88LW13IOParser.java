package com.lasrosas.iot.ingestor.services.sensors.impl.mfc88;

import java.util.Collections;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lasrosas.iot.ingestor.services.sensors.api.ThingDataMessage;
import com.lasrosas.iot.ingestor.services.sensors.impl.PayloadParser;
import com.lasrosas.iot.ingestor.services.sensors.impl.mfc88.MFC88LW13IOFrame.DownlinkFrame;
import com.lasrosas.iot.ingestor.services.sensors.impl.mfc88.MFC88LW13IOFrame.DownlinkIOMessage;
import com.lasrosas.iot.shared.telemetry.Telemetry;

public class MFC88LW13IOParser implements PayloadParser {
		public static final String MANUFACTURER = "MFC88";
		public static final String MODEL = "LW13IO";

		public static Gson gson = new GsonBuilder().create();

		private MFC88LW13IOFrameDecoder decoder = new MFC88LW13IOFrameDecoder();

		@Override
		public ThingDataMessage decodeUplink(byte[] payload) {
			return decoder.decodeUplink(payload);
		}

		@Override
		public byte[] encodeDownlink(Object frame) {
			return decoder.encodeDownlink((DownlinkFrame)frame);
		}

		@Override
		public List<Telemetry> telemetries(ThingDataMessage message) {
			return Collections.emptyList();
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
