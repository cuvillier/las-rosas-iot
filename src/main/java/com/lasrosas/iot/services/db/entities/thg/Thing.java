package com.lasrosas.iot.services.db.entities.thg;

import java.util.List;

import com.lasrosas.iot.services.db.entities.shared.BaseEntity;

public class Thing extends BaseEntity {
	private ThingType type;
	private List<Sensor> sensors;
}
