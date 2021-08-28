package com.lasrosas.iot.core.ingestor.timeSerieWriter.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.lasrosas.iot.core.database.entities.thg.ThingProxy;
import com.lasrosas.iot.core.database.entities.tsr.TimeSerie;
import com.lasrosas.iot.core.database.entities.tsr.TimeSeriePoint;
import com.lasrosas.iot.core.database.entities.tsr.TimeSerieType;
import com.lasrosas.iot.core.database.repo.ThingRepo;
import com.lasrosas.iot.core.database.repo.TimeSeriePointRepo;
import com.lasrosas.iot.core.database.repo.TimeSerieRepo;
import com.lasrosas.iot.core.database.repo.TimeSerieTypeRepo;
import com.lasrosas.iot.core.ingestor.timeSerieWriter.api.WriteSQL;
import com.lasrosas.iot.core.shared.utils.GsonUtils;
import com.lasrosas.iot.core.shared.utils.LasRosasHeaders;

public class WriteSQLImpl implements WriteSQL{

	private Gson gson = new GsonBuilder().create();

	@Autowired
	private ThingRepo thgRepo;

	@Autowired
	private TimeSerieRepo tsrRepo;

	@Autowired
	private TimeSerieTypeRepo tstRepo;

	@Autowired
	private TimeSeriePointRepo tspRepo;

	@Override
	@Transactional
	public void writePoint(Message<?> imessage) {
		var point = insertPoint(imessage);
		updateProxy(point);
	}

	/* test friendly */ void updateProxy(TimeSeriePoint point) {
		var thing = point.getTimeSerie().getThing();
		var proxy = thing.getProxy();

		// Create the proxy if needed
		if (proxy == null) {
			proxy = new ThingProxy();
			proxy.setThing(thing);
			thing.setProxy(proxy);
		}

		// Merge the proxy values
		proxy.setLastSeen(point.getTime());

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
		var changes = GsonUtils.mergeJsonObjects(newValue, subjson, point.getTime());
		if (changes != 0)
			proxy.setValues(gson.toJson(proxyValue));
	}

	private TimeSeriePoint insertPoint(Message<?> imessage) {
		var schema = imessage.getPayload().getClass().getSimpleName();

		var tst = tstRepo.findBySchema(schema);
		if (tst == null) {
			tst = new TimeSerieType(schema);
			tstRepo.save(tst);
		}

		var sensor = LasRosasHeaders.sensor(imessage);
		var thing = thgRepo.findById(LasRosasHeaders.thingid(imessage)).orElseThrow();
		var tsr = tsrRepo.findByThingAndTypeAndSensor(thing, tst, sensor);
		if (tsr == null) {
			tsr = new TimeSerie(thing, tst, sensor);
			tsrRepo.save(tsr);
		}

		String json = gson.toJson(imessage.getPayload());

		var time = LasRosasHeaders.time(imessage);
		var tsp = new TimeSeriePoint(tsr, time, json);

		tspRepo.save(tsp);
		return tsp;
	}
}
