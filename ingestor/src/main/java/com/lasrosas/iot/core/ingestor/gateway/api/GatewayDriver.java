package com.lasrosas.iot.core.ingestor.gateway.api;

public interface GatewayDriver {
	String typeName();
	String encodeDownlink(byte[] data);
}
