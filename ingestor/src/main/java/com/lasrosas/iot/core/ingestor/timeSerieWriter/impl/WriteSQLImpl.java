package com.lasrosas.iot.core.ingestor.timeSerieWriter.impl;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lasrosas.iot.core.database.entities.tsr.TimeSerie;
import com.lasrosas.iot.core.database.entities.tsr.TimeSeriePoint;
import com.lasrosas.iot.core.database.entities.tsr.TimeSerieType;
import com.lasrosas.iot.core.database.repo.DigitalTwinRepo;
import com.lasrosas.iot.core.database.repo.ThingRepo;
import com.lasrosas.iot.core.database.repo.TimeSeriePointRepo;
import com.lasrosas.iot.core.database.repo.TimeSerieRepo;
import com.lasrosas.iot.core.database.repo.TimeSerieTypeRepo;
import com.lasrosas.iot.core.ingestor.timeSerieWriter.api.WriteSQL;
import com.lasrosas.iot.core.shared.utils.GsonUtils;
import com.lasrosas.iot.core.shared.utils.LasRosasHeaders;

public class WriteSQLImpl implements WriteSQL {
	public static final Logger log = LoggerFactory.getLogger(WriteSQLImpl.class);

	@Autowired
	public Gson gson;

	@Autowired
	private ThingRepo thgRepo;

	@Autowired
	private DigitalTwinRepo twinRepo;

	@Autowired
	private TimeSerieRepo tsrRepo;

	@Autowired
	private TimeSerieTypeRepo tstRepo;

	@Autowired
	private TimeSeriePointRepo tspRepo;
	
	@PersistenceContext
	private EntityManager em;

	private boolean storeProxyTime = false;
	
	// public for testing, not in interface 
	public boolean isStoreProxyTime() {
		return storeProxyTime;
	}

	// public for testing, not in interface 
	public void setStoreProxyTime(boolean storeProxyTime) {
		this.storeProxyTime = storeProxyTime;
	}

	@Override
	@Transactional
	public Optional<TimeSeriePoint> writePoint(Message<?> imessage) {
		var point = insertPoint(imessage);

		if(point.isPresent())
			updateProxy(point.get());

		return point;
	}

	// public for testing, not in interface
	public void updateProxy(TimeSeriePoint point) {
		var thing = point.getTimeSerie().getThing();

		// Twin point
		if( thing == null ) return;
		var proxy = thing.getCreateProxy(em);

		// Merge the proxy values

		var proxyJsonValue = proxy.getValues();
		var proxyValue = gson.fromJson(proxyJsonValue, JsonObject.class);
		if (proxyValue == null)
			proxyValue = new JsonObject();

		var schema = point.getTimeSerie().getType().getSchema();
		var sensor = point.getTimeSerie().getSensor();

		var subjsonName = sensor == null? schema: sensor+"_"+schema;
		JsonObject subjson = proxyValue.getAsJsonObject(subjsonName);
		if (subjson == null) {
			subjson = new JsonObject();
			proxyValue.add(subjsonName, subjson);
		}

		var newValue = point.getValue(gson);
		var changes = GsonUtils.mergeJsonObjects(newValue, subjson, storeProxyTime?point.getTime(): null);
		if (changes != 0)
			proxy.setValues(gson.toJson(proxyValue));
	}

	private Optional<TimeSeriePoint> insertPoint(Message<?> imessage) {
		var schema = imessage.getPayload().getClass().getSimpleName();

		var otst = tstRepo.findBySchema(schema);
		TimeSerieType tst;

		if (otst.isEmpty()) {
			tst = new TimeSerieType(schema);
			tst.setPersistent(true);
			tstRepo.save(tst);
		} else
			tst = otst.get();

		if( !tst.isPersistent() ) return Optional.empty();

		// Get/create TimeSerie
		TimeSerie tsr;
		var twinId = LasRosasHeaders.twinId(imessage).orElse(null);
		if( twinId != null) {
			var twin = twinRepo.findById(twinId).get();
			tsr = tsrRepo.findByTwinAndType(twin, tst);
			if (tsr == null) {
				tsr = new TimeSerie(twin, tst);
				tsrRepo.save(tsr);
			}
		} else {
			var sensor = LasRosasHeaders.sensor(imessage);

			var thingId = LasRosasHeaders.thingId(imessage).get();
			var thing = thgRepo.findById(thingId).get();

			tsr = tsrRepo.findByThingAndTypeAndSensor(thing, tst, sensor);
			if (tsr == null) {
				tsr = new TimeSerie(thing, tst, sensor);
				tsrRepo.save(tsr);
			}
		}

		String json = gson.toJson(imessage.getPayload());

		var time = LasRosasHeaders.timeReceived(imessage);
		TimeSeriePoint tsp;

		var otsp = tspRepo.getByTimeAndTimeSerie(time, tsr);
		if(otsp.isPresent()) {
			tsp = otsp.get();
			tsp.setValue(json);
		} else {
			tsp = new TimeSeriePoint(tsr, time, json);
			em.persist(tsp);
			tsr.setCurrentValue(tsp);
		}

		return Optional.of(tsp);
	}
}
