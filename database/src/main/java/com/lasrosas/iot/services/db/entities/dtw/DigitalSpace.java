package com.lasrosas.iot.services.db.entities.dtw;

import java.util.List;

import com.lasrosas.iot.services.db.entities.shared.BaseEntity;

public class DigitalSpace extends BaseEntity {
	private String name;
	private List<DigitalTwinType> digitalTwinTypes;
}
