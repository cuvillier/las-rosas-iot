package com.lasrosas.iot.services.db.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lasrosas.iot.services.db.entities.tsr.TimeSerie;

public interface TimeSerieRepo extends JpaRepository<TimeSerie, Long>, TimeSerieRepoCustom {

}
