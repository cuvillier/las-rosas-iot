package com.lasrosas.iot.reactore.reactor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lasrosas.iot.database.HibernateUtils;
import com.lasrosas.iot.database.entities.dtw.DigitalTwin;
import com.lasrosas.iot.database.finca.WaterTank;
import com.lasrosas.iot.database.repo.TimeSeriePointRepo;
import com.lasrosas.iot.database.repo.TimeSerieRepo;
import com.lasrosas.iot.database.repo.TimeSerieTypeRepo;
import com.lasrosas.iot.database.repo.TwinAlarmRepo;
import com.lasrosas.iot.shared.telemetry.DistanceMeasurement;
import com.lasrosas.iot.shared.telemetry.WaterTankFilling;
import com.lasrosas.iot.shared.utils.Loggers;
import com.lasrosas.iot.shared.utils.NotFoundException;

public class WaterTankReactor extends TwinReactor {
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

	public WaterTankReactor() {
	}

	@Override
	public List<ReactorResult> react(DigitalTwin twin, List<ReactorReceiverValue> receiverValues) {

		// Only "level" is expected
		assertEquals(1, receiverValues.size());
		var receiverValue = receiverValues.get(0);

		if (!receiverValue.getReceiver().getType().getRole().equals("level"))
			throw new RuntimeException("Unexpected receiver role " + receiverValue.getReceiver().getType().getRole());

		WaterTank waterTank = HibernateUtils.unProxyToClass(twin, WaterTank.class);

		var level = receiverValue.getValue().get("distance").getAsDouble();

		// Get the current WaterTankFilling value
		var wfTst = tstRepo.findBySchema(WaterTankFilling.SCHEMA);
		var wfTsr = wfTst == null ? null : tsrRepo.findByTwinAndType(twin, wfTst);
		var wfCurrentPoint = wfTsr == null ? null : wfTsr.getCurrentValue();

		boolean volumeSet = false;
		if (wfCurrentPoint != null) {

			// Get the previous value to compute the water flow
			var wfCurrentValue = wfCurrentPoint.getValue(gson);
			var currentVolume = wfCurrentValue.get("volume").getAsDouble();
			waterTank.setLevel(wfCurrentPoint.getTime(), currentVolume, receiverValue.getTime(), level);

			/*
			 * The Elsys Ultrasonic sensor send sometime invalid data. Filter out data based
			 * on impossible waterFlow.
			 */
			if (waterTank.getMaxWaterFlow() != null && waterTank.getWaterFlow() != null
					&& Math.abs(waterTank.getWaterFlow()) >= waterTank.getMaxWaterFlow()) {

				// The value looks invalid. Skip this point.
				Loggers.FunctionalErrors.info("Invalid distance measurement " + "waterTank=" + waterTank.getName()
						+ ", " + "time=" + receiverValue.getTime() + ", " + "level=" + level + "m, " + "waterFlow="
						+ waterTank.getWaterFlow() + " m3/h, " + "maxWaterFlow=" + waterTank.getMaxWaterFlow()
						+ " m3/h");
			} else
				volumeSet = true;
		}

		if (!volumeSet) {
			log.info("No current value, cannot update waterflow");
			waterTank.setLevel(level); // Cannot compute WaterFlow
			log.debug("Cannot compute waterFlow, no previous data");
		} else {
			log.debug("WaterFlow=" + waterTank.getWaterFlow());
		}

		// Return result
		var volume = waterTank.getVolume();
		var percentage = waterTank.getPercentageFill();
		var waterFlow = waterTank.getWaterFlow();

		var wtf = new WaterTankFilling(volume, percentage, waterFlow);

		var result = new ArrayList<ReactorResult>();
		result.add(new ReactorResult(wtf.toJsonObject(), WaterTankFilling.SCHEMA));
		return result;
	}
}
