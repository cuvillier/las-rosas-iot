package com.lasrosas.iot.alarm.database.repo;

import java.util.List;

import com.lasrosas.iot.alarm.database.entity.TelemetryWatcher;
import com.lasrosas.iot.core.database.entities.dtw.DigitalTwin;
import com.lasrosas.iot.core.database.entities.thg.Thing;

public interface TelemetryWatcherRepoCustom {
	List<TelemetryWatcher> findMatchingWatchers(Thing thing, Object payload);
	List<TelemetryWatcher> findMatchingWatchers(DigitalTwin twin, Object payload);
}
