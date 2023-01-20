package com.lasrosas.iot.core.reactor.reactores;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

import com.lasrosas.iot.alarm.database.entity.AlarmGravity;
import com.lasrosas.iot.alarm.service.api.AlarmService;
import com.lasrosas.iot.core.database.entities.dtw.TwinReactorReceiver;
import com.lasrosas.iot.core.database.twins.Fridge;
import com.lasrosas.iot.core.reactor.base.ReactContext;
import com.lasrosas.iot.core.reactor.base.TwinReactor;
import com.lasrosas.iot.core.shared.telemetry.AirEnvironment;
import com.lasrosas.iot.core.shared.telemetry.FridgeStatus;
import com.lasrosas.iot.core.shared.telemetry.FridgeTemperature;
import com.lasrosas.iot.core.shared.utils.LasRosasHeaders;

public class FridgeReactor implements TwinReactor {
	public static final Logger log = LoggerFactory.getLogger(FridgeReactor.class);

	@Autowired
	private AlarmService alarmService;

	public FridgeReactor() {
	}

	@Override
	public void react(TwinReactorReceiver receiver, Message<?> imessage) {

		enum ReactChannel {
			inside_temp,
			outside_temp
		};
		var channel = ReactChannel.valueOf(receiver.getType().getRole());

		var fridge = receiver.<Fridge>getTwin();
		var status = fridge.getStatus();

		var payload = imessage.getPayload();

		if( !( payload instanceof AirEnvironment) ) return;
		var airEnvironment = (AirEnvironment)payload;

		var temp = airEnvironment.getTemperature();
		var humidity = airEnvironment.getHumidity();

		var sensor = LasRosasHeaders.sensor(imessage);
		if( channel == ReactChannel.inside_temp) {

			if( temp != null) fridge.setInsideTemp(temp);

			if( humidity != null ) fridge.setInsideHumidity(humidity);

		} else if( channel == ReactChannel.outside_temp) {
			if(temp != null) fridge.setOutsideTemp(temp);

		} else {
			log.error("Fridge " + fridge.getTechid() + " Receive data with unknown sensor " + sensor);
			return;
		}

		if(status != fridge.getStatus()) {
			if(fridge.getStatus() == FridgeStatus.NORMAL )
				alarmService.closeAlarm(null, fridge, getClass(), FridgeStatus.NORMAL.toString());
			else
				alarmService.openAlarm(null, fridge, getClass(), fridge.getStatus().toString(), AlarmGravity.HIGH);
		}

		var telemetry = new FridgeTemperature(fridge.getStatus(), fridge.getInsideTemp(), fridge.getInsideHumidity(), fridge.getOutsideTemp());
		ReactContext.addTelemetry(telemetry);
	}
}
