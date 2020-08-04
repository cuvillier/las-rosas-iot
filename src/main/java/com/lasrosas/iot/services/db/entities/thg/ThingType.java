package com.lasrosas.iot.services.db.entities.thg;

import java.util.List;

import com.lasrosas.iot.services.db.entities.shared.BaseEntity;

public class ThingType extends BaseEntity {
	private List<Thing> things;
	private String manufacturer;
	private String model;
}
