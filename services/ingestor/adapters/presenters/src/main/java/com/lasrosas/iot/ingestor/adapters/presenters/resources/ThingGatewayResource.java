package com.lasrosas.iot.ingestor.adapters.presenters.resources;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Slf4j
public class ThingGatewayResource extends LongResource {
	private String naturalid;
	private String type;
	private String driver;
}
