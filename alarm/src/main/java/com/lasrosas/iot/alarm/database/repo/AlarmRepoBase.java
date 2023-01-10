package com.lasrosas.iot.alarm.database.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lasrosas.iot.alarm.database.entity.Alarm;

public interface AlarmRepoBase <ALR extends Alarm> extends JpaRepository<ALR, Long> {

}
