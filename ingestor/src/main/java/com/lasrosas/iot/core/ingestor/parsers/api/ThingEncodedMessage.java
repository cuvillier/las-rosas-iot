package com.lasrosas.iot.core.ingestor.parsers.api;

import java.util.Base64;

public class ThingEncodedMessage {
	private String encodedData;
	private String dataEncoding;

	public ThingEncodedMessage() {
		super();
	}

	public ThingEncodedMessage(String encodedData) {
		this(encodedData, "base64");
	}

	public ThingEncodedMessage(String encodedData, String dataEncoding) {
		super();
		this.encodedData = encodedData;
		this.dataEncoding = dataEncoding;
	}

	public byte[] decodeData() {
		if(encodedData == null) return null;

		if (!dataEncoding.equalsIgnoreCase("base64"))
			throw new RuntimeException("Unknown data encoding encoding=" + dataEncoding);

		return Base64.getDecoder().decode(encodedData);
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
