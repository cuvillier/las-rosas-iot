package com.lasrosas.iot.core.database.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lasrosas.iot.core.database.entities.dtw.DigitalTwin;
import com.lasrosas.iot.core.database.entities.thg.Thing;
import com.lasrosas.iot.core.database.entities.tsr.TimeSerie;
import com.lasrosas.iot.core.database.entities.tsr.TimeSerieType;

@Repository
public interface TimeSerieRepo extends JpaRepository<TimeSerie, Long>, TimeSerieRepoCustom {

	TimeSerie findByThingAndTypeAndSensor(Thing thg, TimeSerieType tst, String sensor);

	TimeSerie findByTwinAndType(DigitalTwin twin, TimeSerieType tst);

}
