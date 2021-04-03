package com.lasrosas.iot.reactor.dchange;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lasrosas.iot.database.entities.dtw.DigitalTwin;
import com.lasrosas.iot.database.entities.tsr.TimeSerie;
import com.lasrosas.iot.database.entities.tsr.TimeSeriePoint;
import com.lasrosas.iot.database.entities.tsr.TimeSerieType;
import com.lasrosas.iot.database.repo.DigitalTwinRepo;
import com.lasrosas.iot.database.repo.TimeSeriePointRepo;
import com.lasrosas.iot.database.repo.TimeSerieRepo;
import com.lasrosas.iot.database.repo.TimeSerieTypeRepo;
import com.lasrosas.iot.influxdb.InfluxdbSession;
import com.lasrosas.iot.mqtt.rules.DataChange;
import com.lasrosas.iot.mqtt.rules.DataChangeEngine;
import com.lasrosas.iot.mqtt.rules.DataChangePublisher;
import com.lasrosas.iot.mqtt.rules.RuleResult;
import com.lasrosas.iot.mqtt.session.MqttSession;
import com.lasrosas.iot.shared.utils.NotFoundException;

public class DigitalTwinChangeEngine extends DataChangeEngine<DigitalTwin> {
	public static final Logger log = LoggerFactory.getLogger(DigitalTwinChangeEngine.class);

	@Autowired
	private Gson gson;	

	@Autowired
	private DigitalTwinRepo twinRepo;	

	@Autowired
	private TimeSerieTypeRepo tstRepo;

	@Autowired
	private TimeSerieRepo tsrRepo;

	@Autowired
	private TimeSeriePointRepo tspRepo;

	@Autowired
	private InfluxdbSession influxdb;

	@Autowired
	private MqttSession mqtt;

	@Override
	protected DigitalTwin loadEntity(Long dtwinId) {
		return twinRepo.getOne(dtwinId);
	}

	@Override
	protected String getEntityType(DigitalTwin dtwin) {
		return dtwin.getType().getName();
	}

	@Override
	protected void evaluateResult(DigitalTwin twin, RuleResult<DigitalTwin> result) {
		if(result instanceof ChangeDigitalTwin) {
			var changeDigitalTwin = (ChangeDigitalTwin)result;
			changeDigitalTwin(twin, changeDigitalTwin);
		}
	}

	private void changeDigitalTwin(DigitalTwin twin, ChangeDigitalTwin changeDigitalTwin) {
		var schema = changeDigitalTwin.getSchema();
		var value = changeDigitalTwin.getValues();
		var time = changeDigitalTwin.getTime();

		if(log.isDebugEnabled()) {
			log.debug("Add point to the twin " + twin.getName());
			log.debug(gson.toJson(value));
		}

		if( schema == null ) 
			throw new NotFoundException("schema in the sensor normalized message.");

		var tst = tstRepo.findBySchema(schema);
		if( tst == null ) {
			tst = new TimeSerieType(schema);
			log.debug("New time serie type for schema " + schema);
			tstRepo.save(tst);
		}

		var tsr = tsrRepo.findByTwinAndType(twin, tst);
		if( tsr == null ) {
			tsr = new TimeSerie(twin, tst);
			log.debug("New time serie for schema " + schema);
			tsrRepo.save(tsr);
		}

		String json = gson.toJson(value);

		var currentValue = tsr.getCurrentValue();
		var oldValue = gson.fromJson(currentValue.getValue(), JsonObject.class);
		var newValues = findNewValues(value, oldValue);

		var tsp = new TimeSeriePoint(tsr, time, json);
		tspRepo.save(tsp);
		tsr.setCurrentValue(tsp);

		// Write to InfluxDB
		influxdb.write(tsp);

		var publisher = new DataChangePublisher<DigitalTwin>(mqtt);

		var dataChange = new DataChange(DigitalTwin.class.getSimpleName(), twin.getTechid(), time, newValues);
		publisher.publish(dataChange);
	}

	private DataChange.NewValue [] findNewValues(JsonObject oldValue, JsonObject newValue) {
		return null;
	}
}
