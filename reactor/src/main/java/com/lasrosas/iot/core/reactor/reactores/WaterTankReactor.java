package com.lasrosas.iot.core.reactor.reactores;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

import com.lasrosas.iot.alarm.database.entity.AlarmGravity;
import com.lasrosas.iot.alarm.service.api.AlarmService;
import com.lasrosas.iot.core.database.entities.dtw.TwinReactorReceiver;
import com.lasrosas.iot.core.database.twins.WaterTank;
import com.lasrosas.iot.core.reactor.base.ReactContext;
import com.lasrosas.iot.core.reactor.base.TwinReactor;
import com.lasrosas.iot.core.shared.telemetry.AirEnvironment;
import com.lasrosas.iot.core.shared.telemetry.DistanceMeasurement;
import com.lasrosas.iot.core.shared.telemetry.WaterTankFilling;
import com.lasrosas.iot.core.shared.telemetry.WaterTankStatus;
import com.lasrosas.iot.core.shared.utils.LasRosasHeaders;
import com.lasrosas.iot.core.shared.utils.Loggers;

public class WaterTankReactor implements TwinReactor {
	public static final Logger log = LoggerFactory.getLogger(WaterTankReactor.class);

	public static final double LEVEL_NORMAL_MAX = 95;
	public static final double LEVEL_WARNING_MAX = 50;
	public static final double LEVEL_ALARM_MAX = 25;
	public static final double LEVEL_EMPTY_MAX = 5;

	public static final String ALR_WATER_LEVEL = "Water level";
	
	@Autowired
	private AlarmService alarmService;

	public WaterTankReactor() {
	}

	@Override
	public void react(TwinReactorReceiver receiver, Message<?> imessage) {
		var waterTank = receiver.<WaterTank>getTwin();

		var payload = imessage.getPayload();

		if(payload instanceof DistanceMeasurement) {
			var distanceMeasurement = (DistanceMeasurement)payload;

	
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

		} else if(payload instanceof AirEnvironment) {
			var airEnvironment = (AirEnvironment)payload;

			waterTank.setTemperature(airEnvironment.getTemperature());
			waterTank.setHumidity(airEnvironment.getHumidity());
		}

		// Return result
		var wtf = new WaterTankFilling(
							waterTank.getStatus(),
							waterTank.getVolume(),
							waterTank.getPercentageFill(),
							waterTank.getWaterFlow(),
							waterTank.getTemperature(),
							waterTank.getHumidity());

		ReactContext.addTelemetry(wtf);
	}

	public void computeStatus(WaterTank waterTank) {

		var percentageFill = waterTank.getPercentageFill();
		if(percentageFill == null) {
			waterTank.setStatus(WaterTankStatus.UNKNOWN);
			return;
		}

		WaterTankStatus status;
		if(percentageFill > LEVEL_NORMAL_MAX)
			status = WaterTankStatus.FULL;
		else if(percentageFill > LEVEL_WARNING_MAX)
			status =   WaterTankStatus.NORMAL;
		else if(percentageFill > LEVEL_ALARM_MAX)
			status =  WaterTankStatus.WARNING;
		else if(percentageFill > LEVEL_EMPTY_MAX)
			status =  WaterTankStatus.ALARM;
		else
			status =  WaterTankStatus.EMPTY;

		if(status != waterTank.getStatus()) {
			switch(status) {
				case FULL:
				case NORMAL:
				case UNKNOWN:
					alarmService.clearAlarm(waterTank, WaterTank.class, ALR_WATER_LEVEL);
					break;

				case WARNING:
					alarmService.openAlarm(null, waterTank, getClass(), ALR_WATER_LEVEL, AlarmGravity.LOW);
					break;

				case ALARM:
					alarmService.openAlarm(null, waterTank, getClass(), ALR_WATER_LEVEL, AlarmGravity.MEDIUM);
					break;

				case EMPTY:
					alarmService.openAlarm(null, waterTank, getClass(), ALR_WATER_LEVEL, AlarmGravity.HIGH);
					break;

			}
		}

		waterTank.setStatus(status);
	}
}
