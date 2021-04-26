package com.lasrosas.iot.ingestor.parser.mfc88;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lasrosas.iot.ingestor.ThingMessageHolder;
import com.lasrosas.iot.ingestor.parser.PayloadParser;
import com.lasrosas.iot.ingestor.parser.impl.adenuis.AdenuisARF8170BAFrameDecoder;
import com.lasrosas.iot.ingestor.parser.impl.adenuis.Adeunis8170BAConfiguration;
import com.lasrosas.iot.ingestor.parser.impl.adenuis.AdenuisARF8170BAFrame.UplinkFrame0x10;
import com.lasrosas.iot.ingestor.parser.impl.adenuis.AdenuisARF8180BAFrame.BaseFrame;
import com.lasrosas.iot.ingestor.parser.impl.adenuis.AdenuisARF8180BAFrame.Frame0x30x43;
import com.lasrosas.iot.shared.ontology.AirEnvironment;
import com.lasrosas.iot.shared.ontology.BatteryLevel;

public class MFC88LW13IOParser implements PayloadParser {
		public static final String MANUFACTURER = "MFC88";
		public static final String MODEL = "LW13IO";

		public static Gson gson = new GsonBuilder().create();

		private MFC88LW13IOFrameDecoder decoder = new MFC88LW13IOFrameDecoder();

		@Override
		public List<ThingMessageHolder> parse(byte[] payload) {

			var result = new ArrayList<ThingMessageHolder>();
			result.add(decoder.decode(payload));

			return result;
		}

		@Override
		public List<ThingMessageHolder> normalize(ThingMessageHolder messageHolder) {
			var result = new ArrayList<ThingMessageHolder>();
			Object message = messageHolder.getMessage();

			return result;
		}

		public String getManufacturer() {
			return MANUFACTURER;
		}

		public String getModel() {
			return MODEL;
		}
}
