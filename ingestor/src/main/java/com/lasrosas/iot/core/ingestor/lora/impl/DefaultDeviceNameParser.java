package com.lasrosas.iot.core.ingestor.lora.impl;

import com.lasrosas.iot.core.ingestor.lora.api.DeviceNameParser;

public class DefaultDeviceNameParser implements DeviceNameParser {
	private final String separator = "/";

	@Override
	public DeviceInfo parse(String deviceName) {

		if(deviceName == null) return null;

		var splitedName = deviceName.split(separator);

		if (splitedName.length == 3) {
			return new DeviceInfo(splitedName[0], splitedName[1]);
		}

		return null;
	}

	public String getSeparator() {
		return separator;
	}	
}
