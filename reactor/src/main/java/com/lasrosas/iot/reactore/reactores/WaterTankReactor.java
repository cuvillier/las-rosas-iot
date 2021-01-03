package com.lasrosas.iot.reactore.reactores;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lasrosas.iot.database.HibernateUtils;
import com.lasrosas.iot.database.entities.alrm.TwinAlarm;
import com.lasrosas.iot.database.entities.dtw.DigitalTwin;
import com.lasrosas.iot.database.finca.WaterTank;
import com.lasrosas.iot.database.repo.TimeSeriePointRepo;
import com.lasrosas.iot.database.repo.TimeSerieRepo;
import com.lasrosas.iot.database.repo.TimeSerieTypeRepo;
import com.lasrosas.iot.database.repo.TwinAlarmRepo;
import com.lasrosas.iot.shared.ontology.DistanceMeasurement;
import com.lasrosas.iot.shared.ontology.WaterTankFilling;
import com.lasrosas.iot.shared.utils.Loggers;
import com.lasrosas.iot.shared.utils.NotFoundException;

public class WaterTankReactor implements TwinReactor {
	public static final Logger log = LoggerFactory.getLogger(WaterTankReactor.class);

	@Autowired
	private TimeSeriePointRepo tspRepo;

	@Autowired
	private TimeSerieTypeRepo tstRepo;

	@Autowired
	private TimeSerieRepo tsrRepo;

	@Autowired
	private TwinAlarmRepo twiAlrmRepo;

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
				var tsr = point.getTimeSerie();
				var tst = tsr.getType();

				if(tst.getSchema().equals(DistanceMeasurement.SCHEMA)) {
					var jsonoValue = gson.fromJson(point.getValue(), JsonObject.class);
					var level = jsonoValue.get("distance").getAsDouble();

					// Get the current WaterTankFilling value
					var wfTst = tstRepo.findBySchema(WaterTankFilling.SCHEMA);
					var wfTsr = wfTst == null? null: tsrRepo.findByTwinAndType(twin, wfTst);
					var wfCurrentPoint = wfTsr == null? null: wfTsr.getCurrentValue();
					
					boolean volumeSet = false;
					if(wfCurrentPoint != null) {

						// Get the previous value to compute the water flow
						var wfCurrentValue = wfCurrentPoint.getValue(gson);
						var currentVolume = wfCurrentValue.get("volume").getAsDouble();
						waterTank.setLevel(wfCurrentPoint.getTime(), currentVolume, receiverValue.getTime(), level);

						/*
						 * The Elsys Ultrasonic sensor send sometime invalid data.
						 * Filter out data based on impossible waterFlow.
						 */
						if(		waterTank.getMaxWaterFlow() != null &&
								waterTank.getWaterFlow() != null &&
								Math.abs(waterTank.getWaterFlow()) >= waterTank.getMaxWaterFlow()) {

							// The value looks invalid. Skip this point.
							Loggers.FunctionalErrors.info("Invalid distance measurement " +
															"waterTank=" + waterTank.getName() + ", " +
															"time=" + receiverValue.getTime() + ", " +
															"level=" + level + "m, " +
															"waterFlow=" + waterTank.getWaterFlow() + " m3/h, " +
															"maxWaterFlow=" + waterTank.getMaxWaterFlow() + " m3/h");
							continue;
						}

						volumeSet = true;
					}

					if(!volumeSet) {
						log.info("No current value, cannot update waterflow");
						waterTank.setLevel(level);	// Cannot compute WaterFlow
						log.debug("Cannot compute waterFlow, no previous data");
					} else {
						log.debug("WaterFlow=" + waterTank.getWaterFlow());
					}

					// Notify change
					var transmitterValue = new JsonObject();
					transmitterValue.addProperty("volume",waterTank.getVolume());
					transmitterValue.addProperty("percentageFill",waterTank.getPercentageFill());
					if( waterTank.getWaterFlow() != null )
						transmitterValue.addProperty("waterFlow",waterTank.getWaterFlow());

					result.add(new TransmitterValue(waterTankLevelTransmitter, receiverValue.getTime(), transmitterValue));
				}
			} else
				throw new RuntimeException("Unknown action " + action);
		}

		return result;
	}
}
