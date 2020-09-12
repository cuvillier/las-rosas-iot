package com.lasrosas.iot.services.db.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lasrosas.iot.services.db.entities.tsr.TimeSerie;

@Repository
public interface TimeSerieRepo extends JpaRepository<TimeSerie, Long>, TimeSerieRepoCustom {

}
