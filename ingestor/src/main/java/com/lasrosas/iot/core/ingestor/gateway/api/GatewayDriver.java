package com.lasrosas.iot.core.ingestor.gateway.api;

public interface GatewayDriver {
	String encodeDownlink(byte[] data);
}
