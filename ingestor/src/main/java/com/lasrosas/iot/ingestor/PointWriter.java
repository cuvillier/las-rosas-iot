package com.lasrosas.iot.ingestor;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lasrosas.iot.database.entities.thg.ThingLora;
import com.lasrosas.iot.database.entities.thg.ThingProxy;
import com.lasrosas.iot.database.entities.tsr.TimeSerie;
import com.lasrosas.iot.database.entities.tsr.TimeSeriePoint;
import com.lasrosas.iot.database.entities.tsr.TimeSerieType;
import com.lasrosas.iot.database.repo.TimeSeriePointRepo;
import com.lasrosas.iot.database.repo.TimeSerieRepo;
import com.lasrosas.iot.database.repo.TimeSerieTypeRepo;
import com.lasrosas.iot.shared.utils.GsonUtils;
import com.lasrosas.iot.shared.utils.NotFoundException;

public class PointWriter {
	public static Logger log = LoggerFactory.getLogger(PointWriter.class);

	@Autowired
	private Gson gson;
	
	@Autowired
	private TimeSerieRepo tsrRepo;

	@Autowired
	private TimeSerieTypeRepo tstRepo;

	@Autowired
	private TimeSeriePointRepo tspRepo;
	
	public TimeSeriePoint insertPoint(ThingLora thing, LocalDateTime time, ThingMessageHolder holder, boolean proxify) {
		var json = gson.toJsonTree(holder.getMessage()).getAsJsonObject();
		var point = insertPoint(thing, time, holder.getSchema(), holder.getSensor(), json);

		if( proxify) {
			var proxy = thing.getProxy();

			// Create the proxy if needed
			if( proxy == null ) {
				proxy = new ThingProxy();
				proxy.setThing(thing);
				thing.setProxy(proxy);
			}

			// Merge the proxy values
			proxy.setLastSeen(time);

			var proxyJsonValue = proxy.getValues();
			var proxyValue = gson.fromJson(proxyJsonValue, JsonObject.class);
			if( proxyValue == null) proxyValue = new JsonObject();

			String subjsonName;
			if( holder.getSensor() != null )
				subjsonName = holder.getSensor() + "-" + holder.getSchema();
			else
				subjsonName = holder.getSchema();

			JsonObject subjson = proxyValue.getAsJsonObject(subjsonName);
			if( subjson == null ) {
				subjson = new JsonObject();
				proxyValue.add(subjsonName, subjson);
			}

			var changes = GsonUtils.mergeJsonObjects(json, subjson, time);
			if(changes != 0) 
				proxy.setValues(gson.toJson(proxyValue));
		}

		return point;
	}

	private TimeSeriePoint insertPoint(ThingLora thing, LocalDateTime time, String schema, String sensor, JsonObject message) {

		if( schema == null ) 
			throw new NotFoundException("schema in the sensor normalized message.");

		var tst = tstRepo.findBySchema(schema);
		if( tst == null ) {
			tst = new TimeSerieType(schema);
			tstRepo.save(tst);
		}

		var tsr = tsrRepo.findByThingAndTypeAndSensor(thing, tst, sensor);
		if( tsr == null ) {
			tsr = new TimeSerie(thing, tst, sensor);
			tsrRepo.save(tsr);
		}

		String json = gson.toJson(message);

		var tsp = new TimeSeriePoint(tsr, time, json);
		tspRepo.save(tsp);
		return tsp;
	}
}
