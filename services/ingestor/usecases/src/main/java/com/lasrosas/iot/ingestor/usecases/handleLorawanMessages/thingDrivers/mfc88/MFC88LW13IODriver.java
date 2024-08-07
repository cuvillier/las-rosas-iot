package com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.mfc88;

import com.lasrosas.iot.ingestor.shared.exceptions.ImpossibleException;
import com.lasrosas.iot.ingestor.domain.model.message.ConnectionStage;
import com.lasrosas.iot.ingestor.domain.model.message.Switched;
import com.lasrosas.iot.ingestor.domain.model.message.ThingMessage;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.LorawanMessageUplinkRx;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.ThingDriver;

import java.util.ArrayList;
import java.util.List;

import static com.lasrosas.iot.ingestor.domain.model.message.ConnectionStage.JOINED;

public class MFC88LW13IODriver implements ThingDriver {
		public static final String MANUFACTURER = "MFC88";
		public static final String MODEL = "LW13IO";

		private MFC88LW13IOFrameDecoder decoder = new MFC88LW13IOFrameDecoder();

		@Override
		public ThingMessage decodeUplink(LorawanMessageUplinkRx uplink) {
			return decoder.decodeUplink(uplink);
		}

		@Override
		public List<ThingMessage> normalize(ThingMessage message) {
			var result = new ArrayList<ThingMessage>();

			ThingMessage normalized =
				switch(message) {
					case MFC88LW13IOFrame.UplinkIO uplinkIO ->
						Switched.builder()
										.state(uplinkIO.getOutputs() != 0? Switched.State.ON: Switched.State.OFF)
										.build();

					case MFC88LW13IOFrame.UplinkTimeSyncRequest timeSyncRequest -> ConnectionStage.builder().stage(JOINED).build();
					default -> throw new ImpossibleException();
				};

			normalized.setOrigin(message);

			result = new ArrayList<ThingMessage>();
			result.add(normalized);

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
