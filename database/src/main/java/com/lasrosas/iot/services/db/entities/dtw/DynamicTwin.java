package com.lasrosas.iot.services.db.entities.dtw;

import java.util.List;

public class DynamicTwin extends DigitalTwin {
	private DynamicTwinType type;
	private List<ThingTwin> things;
}
