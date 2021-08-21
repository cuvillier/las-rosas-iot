package com.lasrosas.iot.ingestor.services.sensors.impl.elsys;

import java.util.List;

import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.lasrosas.iot.ingestor.services.sensors.api.ThingDataMessage;
import com.lasrosas.iot.ingestor.services.sensors.impl.PayloadParser;
import com.lasrosas.iot.shared.telemetry.Telemetry;

public class ElsysErsParser implements PayloadParser {

	@Autowired
	private ElsysGenericParser parser;

	@Override
	public ThingDataMessage decodeUplink(byte[] data) {
		return parser.decodeUplink(data);
	}

	@Override
	public byte[] encodeDownlink(Object frame) {
		throw new NotYetImplementedException();
	}

	@Override
	public List<Telemetry> telemetries(ThingDataMessage message) {
		return parser.telemetries((ElsysGenericUplinkFrame)message);
	}

	public String getManufacturer() {
		return "Elsys";
	}

	public String getModel() {
		return "ERS";
	}

}
