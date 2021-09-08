package com.lasrosas.iot.core.reactor.reactores;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

import com.google.gson.Gson;
import com.lasrosas.iot.core.database.HibernateUtils;
import com.lasrosas.iot.core.database.entities.dtw.DigitalTwin;
import com.lasrosas.iot.core.database.entities.dtw.TwinReactorReceiver;
import com.lasrosas.iot.core.database.finca.WaterTank;
import com.lasrosas.iot.core.database.repo.TimeSerieRepo;
import com.lasrosas.iot.core.database.repo.TimeSerieTypeRepo;
import com.lasrosas.iot.core.reactor.base.TwinReactor;
import com.lasrosas.iot.core.shared.telemetry.DistanceMeasurement;
import com.lasrosas.iot.core.shared.telemetry.Telemetry;
import com.lasrosas.iot.core.shared.telemetry.WaterTankFilling;
import com.lasrosas.iot.core.shared.utils.LasRosasHeaders;
import com.lasrosas.iot.core.shared.utils.Loggers;

public class WaterTankReactor extends TwinReactor {
	public static final Logger log = LoggerFactory.getLogger(WaterTankReactor.class);

	@Autowired
	private TimeSerieTypeRepo tstRepo;

	@Autowired
	private TimeSerieRepo tsrRepo;

	@Autowired
	private Gson gson;

	public WaterTankReactor() {
	}

	@Override
	public List<? extends Telemetry> react(DigitalTwin twin, TwinReactorReceiver receiver, Message<? extends Telemetry> imessage) {

		if (!receiver.getType().getRole().equals("level"))
			throw new RuntimeException("Unexpected receiver role " + receiver.getType().getRole());

		WaterTank waterTank = HibernateUtils.unProxyToClass(twin, WaterTank.class);
		var distanceMeasurement = (DistanceMeasurement)imessage.getPayload();

		var level = distanceMeasurement.getDistance();
		var time = LasRosasHeaders.time(imessage);

		// Get the current WaterTankFilling value
		var wfTst = tstRepo.findBySchema(WaterTankFilling.SCHEMA);
		var wfTsr = wfTst == null ? null : tsrRepo.findByTwinAndType(twin, wfTst);
		var wfCurrentPoint = wfTsr == null ? null : wfTsr.getCurrentValue();

		boolean volumeSet = false;
		if (wfCurrentPoint != null) {

			// Get the previous value to compute the water flow
			var wfCurrentValue = wfCurrentPoint.getValue(gson);
			var currentVolume = wfCurrentValue.get("volume").getAsDouble();
			waterTank.setLevel(wfCurrentPoint.getTime(), currentVolume, time, level);

			/*
			 * The Elsys Ultrasonic sensor send sometime invalid data. Filter out data based
			 * on impossible waterFlow.
			 */
			if (waterTank.getMaxWaterFlow() != null && waterTank.getWaterFlow() != null
					&& Math.abs(waterTank.getWaterFlow()) >= waterTank.getMaxWaterFlow()) {

				// The value looks invalid. Skip this point.
				Loggers.FunctionalErrors.info("Invalid distance measurement " + "waterTank=" + waterTank.getName()
						+ ", " + "time=" + time + ", " + "level=" + level + "m, " + "waterFlow="
						+ waterTank.getWaterFlow() + " m3/h, " + "maxWaterFlow=" + waterTank.getMaxWaterFlow()
						+ " m3/h");
				return Collections.emptyList();
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

		var result = new ArrayList<Telemetry>();
		result.add(wtf);

		return result;
	}
}
