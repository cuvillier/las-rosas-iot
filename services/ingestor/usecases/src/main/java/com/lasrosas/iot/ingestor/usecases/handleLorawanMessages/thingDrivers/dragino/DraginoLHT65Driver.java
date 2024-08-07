package com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.dragino;

import com.lasrosas.iot.ingestor.domain.model.message.AirEnvironment;
import com.lasrosas.iot.ingestor.domain.model.message.BatteryLevel;
import com.lasrosas.iot.ingestor.domain.model.message.ThingMessage;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.LorawanMessageUplinkRx;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.ThingDriver;
import org.springframework.messaging.support.MessageBuilder;

import java.util.ArrayList;
import java.util.List;

public class DraginoLHT65Driver implements ThingDriver {
		public static final String MANUFACTURER = "DRAGINO";
		public static final String MODEL = "LHT65";
		public static final String SENSOR_INT = "INT";
		public static final String SENSOR_EXT = "EXT";

		private DraginoLHT65FrameDecoder decoder = new DraginoLHT65FrameDecoder();

		@Override
		public ThingMessage decodeUplink(LorawanMessageUplinkRx uplink) {
			return  decoder.decodeUplink(uplink);
		}

		@Override
		public List<ThingMessage> normalize(ThingMessage message) {
			var result = new ArrayList<ThingMessage>();

			if( message instanceof DraginoLHT65Frame.UplinkTempHumRequest tempHum) {

				// Internal temp & hum sensor
				var airEnvInt = AirEnvironment.builder()
								.sensor(SENSOR_INT)
								.temperature(tempHum.getTemperatureINT())
								.humidity(tempHum.getHumidityINT())
						.build();
				airEnvInt.setOrigin(message);
				result.add(airEnvInt);

				// External temp & hum sensor
				var airEnvExt = AirEnvironment.builder()
						.sensor(SENSOR_EXT)
						.temperature(tempHum.getTemperatureEXT())
						.build();
				airEnvExt.setOrigin(message);
				result.add(airEnvExt);

				// Battery Level
				var battery = BatteryLevel.from(tempHum.getBatteryVoltage(), 2.55, 3.2);
				result.add(battery);
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
