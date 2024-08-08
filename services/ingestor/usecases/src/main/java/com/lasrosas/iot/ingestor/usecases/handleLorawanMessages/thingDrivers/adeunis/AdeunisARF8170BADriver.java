package com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.adeunis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lasrosas.iot.ingestor.shared.exceptions.InvalidJsonFormatException;
import com.lasrosas.iot.ingestor.domain.model.message.AirEnvironment;
import com.lasrosas.iot.ingestor.domain.model.message.BatteryLevel;
import com.lasrosas.iot.ingestor.domain.model.message.BaseMessage;
import com.lasrosas.iot.ingestor.shared.JsonUtils;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.LorawanMessageUplinkRx;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.ThingDriver;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.adeunis.AdeunisARF8170BAFrame.*;

import java.util.ArrayList;
import java.util.List;

public class AdeunisARF8170BADriver implements ThingDriver {
	public static final String MANUFACTURER = "Adeunis";
	public static final String MODEL = "ARF8170BA";
	public static final String SENSOR_INT = "INT";
	public static final String SENSOR_EXT = "EXT";

	private AdeunisARF8170BAFrameDecoder decoder = new AdeunisARF8170BAFrameDecoder();

	public Adeunis8170BAConfiguration decodeConfig(String json) {
        try {
            return JsonUtils.readerFor(Adeunis8170BAConfiguration.class).readValue(json);
        } catch (JsonProcessingException e) {
            throw new InvalidJsonFormatException(json, e);
        }
    }

	public void updateConfig(Adeunis8170BAConfiguration config, UplinkFrame0x10 frame) {
		config.setChannel1Type(frame.getS320_Channel1Configuration().getType());
		config.setChannel2Type(frame.getS321_Channel2Configuration().getType());
		config.setChannel3Type(frame.getS322_Channel3Configuration().getType());
		config.setChannel4Type(frame.getS323_Channel4Configuration().getType());
	}

	@Override
	public BaseMessage decodeUplink(LorawanMessageUplinkRx uplink) {
		return decoder.decodeUplink(uplink);
	}

	@Override
	public List<BaseMessage> normalize(BaseMessage message) {
		var result = new ArrayList<BaseMessage>();

		if(message instanceof AdeunisARF8180BAFrame.UplinkFrame0x30x43 uplinkFrame0x30x43) {

			var temp10thdeg = uplinkFrame0x30x43.getValueInternalSensor10thDeg();
			if(temp10thdeg != null) {

				var normalized = AirEnvironment.builder()
						.temperature(temp10thdeg / 10.0)
						.sensor(SENSOR_INT)
						.build();
				result.add(normalized);
			}

			temp10thdeg = uplinkFrame0x30x43.getValueExternalSensor10thDeg();
			if(temp10thdeg != null) {

				var normalized = AirEnvironment.builder()
						.temperature(temp10thdeg / 10.0)
						.sensor(SENSOR_EXT)
						.build();
				result.add(normalized);
			}
		}

		if(message instanceof AdeunisARF8180BAFrame.UplinkFrame0x30x43 uplinkFrame0x30x43) {
			var normalized = BatteryLevel.builder()
								.alarm(uplinkFrame0x30x43.getStatus().isLowBat())
								.build();
			result.add(normalized);
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
