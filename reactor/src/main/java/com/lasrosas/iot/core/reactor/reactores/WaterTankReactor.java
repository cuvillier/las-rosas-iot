package com.lasrosas.iot.core.reactor.reactores;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;

import com.lasrosas.iot.core.database.entities.dtw.TwinReactorReceiver;
import com.lasrosas.iot.core.database.twins.WaterTank;
import com.lasrosas.iot.core.reactor.base.ReactContext;
import com.lasrosas.iot.core.reactor.base.TwinReactor;
import com.lasrosas.iot.core.shared.telemetry.DistanceMeasurement;
import com.lasrosas.iot.core.shared.telemetry.WaterTankFilling;
import com.lasrosas.iot.core.shared.utils.LasRosasHeaders;
import com.lasrosas.iot.core.shared.utils.Loggers;

public class WaterTankReactor implements TwinReactor {
	public static final Logger log = LoggerFactory.getLogger(WaterTankReactor.class);

	public WaterTankReactor() {
	}

	@Override
	public void react(TwinReactorReceiver receiver, Message<?> imessage) {

		if (!receiver.getType().getRole().equals("level"))
			throw new RuntimeException("Unexpected receiver role " + receiver.getType().getRole());

		// WaterTank waterTank = HibernateUtils.unProxyToClass(twin, WaterTank.class);
		var waterTank = receiver.<WaterTank>getTwin();
		var distanceMeasurement = (DistanceMeasurement)imessage.getPayload();

		var level = distanceMeasurement.getDistance();
		var time = LasRosasHeaders.timeReceived(imessage);

		// Get the previous value to compute the water flow
		waterTank.updateLevel(time, level);

		var waterFlow = waterTank.getWaterFlow();

		if(waterFlow != null) {

			/*
			 * The Elsys Ultrasonic sensor send sometime invalid data. Filter out data based
			 * on impossible waterFlow.
			 */
			if (waterTank.getMaxWaterFlow() != null && waterTank.getWaterFlow() != null
					&& Math.abs(waterFlow) >= waterTank.getMaxWaterFlow()) {

				// The value looks invalid. Skip this point.
				Loggers.FunctionalErrors.info("Invalid distance measurement " + "waterTank=" + waterTank.getName()
						+ ", " + "time=" + time + ", " + "level=" + level + "m, " + "waterFlow="
						+ waterTank.getWaterFlow() + " m3/h, " + "maxWaterFlow=" + waterTank.getMaxWaterFlow()
						+ " m3/h");
				return;
			}
		}

		// Return result
		var wtf = new WaterTankFilling(
							waterTank.getStatus(), 
							waterTank.getVolume(), 
							waterTank.getPercentageFill(), 
							waterTank.getWaterFlow());

		ReactContext.addTelemetry(wtf);
	}
}
