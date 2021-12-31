package com.lasrosas.iot.core.ingestor.gateway.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.lasrosas.iot.core.database.repo.GatewayRepo;
import com.lasrosas.iot.core.ingestor.gateway.api.GatewayDriver;
import com.lasrosas.iot.core.ingestor.gateway.api.GatewayService;

public class GatewayServiceImpl implements GatewayService {

	@Autowired
	private GatewayRepo gatewayRepo;

	public Map<String, GatewayDriver> drivers = new HashMap<>();

	public GatewayServiceImpl(GatewayDriver ... driverList ) {
		for(var driver: driverList) {
			drivers.put(driver.getClass().getSimpleName(), driver);
		}
	}

	private GatewayDriver getDriver(String name) {
		return Optional.ofNullable(drivers.get(name)).get();
	}

	@SuppressWarnings("unchecked")
	public <T> T getDriver(Class<T> driverClass) {
		return (T)getDriver(driverClass.getSimpleName());
	}

	@Override
	public String encodeDownlink(String gatewayNaturalId, byte[] data) {
		var gateway = gatewayRepo.findByNaturalId(gatewayNaturalId);
		var driverName = gateway.getTypeName();
		var driver = getDriver(driverName);
		return driver.encodeDownlink(data);
	}
}
