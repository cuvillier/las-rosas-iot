package com.lasrosas.iot.core.ingestor.parsers.impl.dragino;

import java.util.ArrayList;
import java.util.List;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lasrosas.iot.core.ingestor.parsers.api.ThingDataMessage;
import com.lasrosas.iot.core.ingestor.parsers.api.ThingEncodedMessage;
import com.lasrosas.iot.core.ingestor.parsers.impl.PayloadParser;
import com.lasrosas.iot.core.ingestor.parsers.impl.dragino.DraginoLHT65Frame.UplinkTempHumRequest;
import com.lasrosas.iot.core.shared.telemetry.AirEnvironment;
import com.lasrosas.iot.core.shared.telemetry.BatteryLevel;
import com.lasrosas.iot.core.shared.telemetry.Order;
import com.lasrosas.iot.core.shared.telemetry.Telemetry;
import com.lasrosas.iot.core.shared.utils.LasRosasHeaders;

public class DraginoLHT65Parser implements PayloadParser {
		public static final String MANUFACTURER = "DRAGINO";
		public static final String MODEL = "LHT65";
		public static final String SENSOR_INT = "INT";
		public static final String SENSOR_EXT = "EXT";

		public static Gson gson = new GsonBuilder().create();

		private DraginoLHT65FrameDecoder decoder = new DraginoLHT65FrameDecoder();

		@Override
		public Message<ThingDataMessage> decodeUplink(Message<ThingEncodedMessage> imessage) {
			var bytes = imessage.getPayload().decodeData();
			var uplink = decoder.decodeUplink(bytes);
			return MessageBuilder.createMessage((ThingDataMessage)uplink, imessage.getHeaders());
		}

		@Override
		public byte[] encodeOrder(Order order) {
			throw new UnsupportedOperationException();
		}

		@Override
		public List<Message<Telemetry>> telemetries(Message<ThingDataMessage> imessage) {
			var result = new ArrayList<Message<Telemetry>>();

			var payload = imessage.getPayload();
			if( payload instanceof UplinkTempHumRequest) {
				var tempHum = (UplinkTempHumRequest)payload;

				// Internal temp & hum sensor
				var airEnv = new AirEnvironment();
				airEnv.setTemperature(tempHum.getTemperatureINT());
				airEnv.setHumidity(tempHum.getHumidityINT());
				var telemetry = MessageBuilder
						.withPayload((Telemetry)airEnv)
						.copyHeaders(imessage.getHeaders())
						.setHeader(LasRosasHeaders.SENSOR, SENSOR_INT)
						.build();
				result.add(telemetry);

				// External temp & hum sensor
				airEnv = new AirEnvironment();
				airEnv.setTemperature(tempHum.getTemperatureEXT());
				telemetry = MessageBuilder
						.withPayload((Telemetry)airEnv)
						.copyHeaders(imessage.getHeaders())
						.setHeader(LasRosasHeaders.SENSOR, SENSOR_EXT)
						.build();
				result.add(telemetry);

				// Battery Level
				var battery = new BatteryLevel(tempHum.getBatteryVoltage(), 2.55, 3.2);
				telemetry = MessageBuilder.withPayload((Telemetry)battery).copyHeaders(imessage.getHeaders()).build();
				result.add(telemetry);
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
