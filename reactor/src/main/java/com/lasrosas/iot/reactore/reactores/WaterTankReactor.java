package com.lasrosas.iot.reactore.reactores;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lasrosas.iot.database.HibernateUtils;
import com.lasrosas.iot.database.entities.dtw.DigitalTwin;
import com.lasrosas.iot.database.finca.WaterTank;
import com.lasrosas.iot.database.repo.TimeSeriePointRepo;
import com.lasrosas.iot.shared.ontology.DistanceMeasurement;
import com.lasrosas.iot.shared.utils.NotFoundException;

public class WaterTankReactor implements TwinReactor {

	@Autowired
	private TimeSeriePointRepo tspRepo;

	@Autowired
	private Gson gson;

	private Receiver distanceReceiver = new Receiver("from-sensor", DistanceMeasurement.class.getSimpleName());
	private Transmitter waterTankLevelTransmitter = new Transmitter("WaterTankLevel");

	public WaterTankReactor() {
	}

	public List<ReceiverValue> mapReceiverValues(JsonArray values) {
		var result = new ArrayList<ReceiverValue>();

		for(var value: values) {
			var jsonoMessage = value.getAsJsonObject();
			var action = jsonoMessage.get("action").getAsString();

			if(action == null) throw new NotFoundException("action field in message " + jsonoMessage);

			if(action.equals("newTimeSeriePoints")) {
				var pointTechid = jsonoMessage.getAsJsonPrimitive("tspTechid").getAsLong();
				var point = tspRepo.getOne(pointTechid);
				var tst = point.getTimeSerie().getType();

				var schema = tst.getSchema();
				if(schema.equals(DistanceMeasurement.SCHEMA)) {
					var receiverValue = new ReceiverValue(distanceReceiver, value.getAsJsonObject(), point.getTime());
					result.add(receiverValue);
				}
			} else 
				throw new RuntimeException("Unknown action " + action);
		}

		return result;
	}

	@Override
	public List<TransmitterValue> react(DigitalTwin twin, List<ReceiverValue> receiverValues) {
		var result = new ArrayList<TransmitterValue>();
		WaterTank waterTank = HibernateUtils.unProxyToClass(twin, WaterTank.class);

		for(var receiverValue: receiverValues) {
			var jsonoMessage = receiverValue.getValue();
			var action = jsonoMessage.get("action");

			if(action == null) throw new NotFoundException("action field in message " + jsonoMessage);

			if(action.getAsString().equals("newTimeSeriePoints")) {
				var pointTechid = jsonoMessage.getAsJsonPrimitive("tspTechid").getAsLong();
				var point = tspRepo.getOne(pointTechid);
				var tst = point.getTimeSerie().getType();

				if(tst.getSchema().equals(DistanceMeasurement.SCHEMA)) {
					var jsonoValue = gson.fromJson(point.getValue(), JsonObject.class);
					var level = jsonoValue.get("distance").getAsDouble();

					// Update twin
					waterTank.setLevel(level);

					// Notify change
					var transmitterValue = new JsonObject();
					transmitterValue.addProperty("volume",waterTank.getVolume());
					transmitterValue.addProperty("percentageFill",waterTank.getPercentageFill());

					result.add(new TransmitterValue(waterTankLevelTransmitter, receiverValue.getTime(), transmitterValue));
				}
			} else
				throw new RuntimeException("Unknown action " + action);
		}

		return result;
	}
}
