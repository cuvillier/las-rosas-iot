package com.lasrosas.iot.ingestor.adapters.presenters.resources;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@SuperBuilder
@Slf4j
@NoArgsConstructor
public class ThingResource extends LongResource {

	private String naturalid;
	private String readable;
	private Integer connectionTimeout;

	private ThingGatewayResource gateway;
	private ThingTypeResource type;
	private ThingProxyResource proxy;

	public enum AdminState {
		CONNECTED,
		DISCONNECTED,
		DISABLED,
		REMOVED
	}

	private AdminState adminState;
}
