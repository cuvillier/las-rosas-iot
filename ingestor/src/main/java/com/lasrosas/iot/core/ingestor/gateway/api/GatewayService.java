package com.lasrosas.iot.core.ingestor.gateway.api;

public interface GatewayService {
	String encodeDownlink(String gatewayNaturalId, byte[] data);
}
