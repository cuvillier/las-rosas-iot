package com.lasrosas.iot.services.db.entities.dtw;

import java.util.List;

import com.lasrosas.iot.services.db.entities.shared.BaseEntity;

public class DigitalTwinType extends BaseEntity {
	private DigitalSpace space;
	private List<DynamicTwinPropertyType> properties;
}
