package com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.dragino;

import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.LorawanMessageUplinkRx;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DraginoLHT65FrameDecoder {

	public DraginoLHT65Frame.UplinkTempHumRequest decodeUplink(LorawanMessageUplinkRx message) {
		var bytes = message.decodeData();
		var b0 = bytes[0];
		var statusBits = b0 & 0b11;
		var batteryStatus = DraginoLHT65Frame.UplinkTempHumRequest.BatteryStatus.getByCode(statusBits);

		var b1 = bytes[1];
		
		int batteryMV = (((b0 & 0xff) << 8) | (b1 & 0xff)) & 0x3FFF;
		double batteryVoltage = (batteryMV) / 1000.0;

		var b2 = bytes[2];
		var b3 = bytes[3];
		double tempINT = (((b2 & 0xff) << 8) | (b3 & 0xff)) / 100.0;

		var b4 = bytes[4];
		var b5 = bytes[5];
		double humeINT = (((b4 & 0xff) << 8) | (b5 & 0xff)) / 10.0;

		int extSensor = bytes[6];

		Double tempEXT = null;
		if(extSensor != 0) {
			var b7 = bytes[7];
			var b8 = bytes[8];
			tempEXT = (((b7 & 0xff) << 8) | (b8 & 0xff)) / 100.0;
		}

		var result = DraginoLHT65Frame.UplinkTempHumRequest.builder()
				.batteryStatus(batteryStatus)
				.batteryVoltage(batteryVoltage)
				.temperatureEXT(tempEXT)
				.temperatureINT(tempINT)
				.humidityINT(humeINT)
				.sensorExt(extSensor)
				.build();
		result.setOrigin(message);
		return result;
	}

	void bin(byte b) {
		byte mask = 1;
		for(var i = 0; i < 8; i++) {
			var shift = b<<i;
			var bmask = mask <<i;
			var bit = shift & bmask;
		}
	}
	void bin(int b) {
		byte mask = 1;
		for(var i = 0; i < 16; i++) {
			var shift = b<<i;
			var bmask = mask <<i;
			var bit = shift & bmask;
			System.out.println(bit);
		}
	}

	private double toFixed(double value, double digits) {
		double scale = Math.pow(10, digits);
		return Math.round(value * scale) / scale;
	}
}
