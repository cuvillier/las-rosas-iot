package com.lasrosas.iot.ingestor.services.sensors.api;

import java.util.Base64;

public class ThingEncodedMessage {
	private long thingid;
	private String encodedData;
	private String dataEncoding;

	public byte[] decodeData() {
		if(encodedData == null) return null;

		if (!dataEncoding.equalsIgnoreCase("base64"))
			throw new RuntimeException("Unknown data encoding encoding=" + dataEncoding);

		return Base64.getDecoder().decode(encodedData);
	}

	public long getThingid() {
		return thingid;
	}
	public void setThingid(long thingid) {
		this.thingid = thingid;
	}
	public String getEncodedData() {
		return encodedData;
	}
	public void setEncodedData(String encodedData) {
		this.encodedData = encodedData;
	}
	public String getDataEncoding() {
		return dataEncoding;
	}
	public void setDataEncoding(String dataEncoding) {
		this.dataEncoding = dataEncoding;
	}
}
