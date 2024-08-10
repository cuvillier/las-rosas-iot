package com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.adeunis;
import com.lasrosas.iot.ingestor.domain.message.AirEnvironment;
import com.lasrosas.iot.ingestor.domain.message.BatteryLevel;
import com.lasrosas.iot.ingestor.domain.message.BaseMessage;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.LorawanMessageUplinkRx;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.ThingDriver;

import java.util.ArrayList;
import java.util.List;

import static com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.adeunis.AdeunisARF8170BADriver.SENSOR_EXT;
import static com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.adeunis.AdeunisARF8170BADriver.SENSOR_INT;
import static com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.adeunis.AdeunisARF8180BAFrame.*;

public class AdeunisARF8180BADriver implements ThingDriver {
	private AdeunisARF8180BAFrameDecoder decoder = new AdeunisARF8180BAFrameDecoder();

	@Override
	public BaseMessage decodeUplink(LorawanMessageUplinkRx uplink) {
		return decoder.decodeUplink(uplink);
	}


	@Override
	public List<BaseMessage> normalize(BaseMessage message) {
		var result = new ArrayList<BaseMessage>();

		if(message instanceof UplinkFrame0x30x43 uplinkFrame0x30x43) {
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

		if(message instanceof UplinkFrame0x30x43 uplinkFrame0x30x43) {
			var normalized = BatteryLevel.builder().alarm(uplinkFrame0x30x43.getStatus().isLowBat()).build();
			result.add(normalized);
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
