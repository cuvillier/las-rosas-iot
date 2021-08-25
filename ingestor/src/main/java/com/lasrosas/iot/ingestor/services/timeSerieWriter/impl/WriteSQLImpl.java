package com.lasrosas.iot.ingestor.services.timeSerieWriter.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lasrosas.iot.database.entities.thg.ThingProxy;
import com.lasrosas.iot.database.entities.tsr.TimeSerie;
import com.lasrosas.iot.database.entities.tsr.TimeSeriePoint;
import com.lasrosas.iot.database.entities.tsr.TimeSerieType;
import com.lasrosas.iot.database.repo.ThingRepo;
import com.lasrosas.iot.database.repo.TimeSeriePointRepo;
import com.lasrosas.iot.database.repo.TimeSerieRepo;
import com.lasrosas.iot.database.repo.TimeSerieTypeRepo;
import com.lasrosas.iot.ingestor.services.timeSerieWriter.api.WriteSQL;
import com.lasrosas.iot.ingestor.shared.LasRosasHeaders;

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
	public void writePoint(Message<?> imessage) {
		var point = insertPoint(imessage);
		updateProxy(point);
	}

	private void updateProxy(TimeSeriePoint point) {
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
/*
		var proxyJsonValue = proxy.getValues();
		var proxyValue = gson.fromJson(proxyJsonValue, JsonObject.class);
		if (proxyValue == null)
			proxyValue = new JsonObject();

			var schema = point.getTimeSerie().getType().getSchema();

			JsonObject subjson = proxyValue.getAsJsonObject(subjsonName);
			if (subjson == null) {
				subjson = new JsonObject();
				proxyValue.add(subjsonName, subjson);
			}

			var changes = GsonUtils.mergeJsonObjects(json, subjson, time);
			if (changes != 0)
				proxy.setValues(gson.toJson(proxyValue));
		}
*/
	}

	private TimeSeriePoint insertPoint(Message<?> imessage) {
		var schema = imessage.getPayload().getClass().getSimpleName();

		var tst = tstRepo.findBySchema(schema);
		if (tst == null) {
			tst = new TimeSerieType(schema);
			tstRepo.save(tst);
		}

		var sensor = LasRosasHeaders.sensor(imessage);
		var thing = thgRepo.getOne(LasRosasHeaders.thingid(imessage));
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
