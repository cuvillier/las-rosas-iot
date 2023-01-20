package com.lasrosas.iot.alarm.database.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lasrosas.iot.alarm.database.entity.AlarmType;

@Repository
public interface AlarmTypeRepo extends JpaRepository<AlarmType, Long> {
	Optional<AlarmType> findByDataTypeAndCause(String dataType, String cause);
}
