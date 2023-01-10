package com.lasrosas.iot.alarm.database.repo;

import org.springframework.stereotype.Repository;

import com.lasrosas.iot.alarm.database.entity.Alarm;

@Repository
public interface AlarmRepo extends AlarmRepoBase<Alarm> {
}
