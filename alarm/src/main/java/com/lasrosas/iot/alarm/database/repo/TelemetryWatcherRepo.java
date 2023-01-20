package com.lasrosas.iot.alarm.database.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lasrosas.iot.alarm.database.entity.TelemetryWatcher;

@Repository
public interface TelemetryWatcherRepo extends JpaRepository<TelemetryWatcher,Long>, TelemetryWatcherRepoCustom {
}
