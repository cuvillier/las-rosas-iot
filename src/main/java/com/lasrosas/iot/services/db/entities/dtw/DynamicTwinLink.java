package com.lasrosas.iot.services.db.entities.dtw;

import com.lasrosas.iot.services.db.entities.shared.BaseEntity;

public class DynamicTwinLink extends BaseEntity {
	private DynamicTwinType type;
	private DynamicTwin origin;
	private DynamicTwin destination;
}
