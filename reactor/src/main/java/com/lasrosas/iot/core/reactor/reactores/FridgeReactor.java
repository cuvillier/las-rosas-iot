package com.lasrosas.iot.core.reactor.reactores;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;

import com.lasrosas.iot.core.database.entities.dtw.TwinReactorReceiver;
import com.lasrosas.iot.core.database.twins.Fridge;
import com.lasrosas.iot.core.reactor.base.ReactContext;
import com.lasrosas.iot.core.reactor.base.TwinReactor;
import com.lasrosas.iot.core.shared.telemetry.AirEnvironment;
import com.lasrosas.iot.core.shared.telemetry.FridgeTemperature;
import com.lasrosas.iot.core.shared.utils.LasRosasHeaders;

public class FridgeReactor implements TwinReactor {
	public static final Logger log = LoggerFactory.getLogger(FridgeReactor.class);

	public FridgeReactor() {
	}

	@Override
	public void react(TwinReactorReceiver receiver, Message<?> imessage) {

		if (!receiver.getType().getRole().equals("temperature"))
			throw new RuntimeException("Unexpected receiver role " + receiver.getType().getRole());

		var fridge = receiver.<Fridge>getTwin();
		var payload = imessage.getPayload();

		if( !( payload instanceof AirEnvironment) ) return;
		var airEnvironment = (AirEnvironment)payload;

		var temp = airEnvironment.getTemperature();
		var humidity = airEnvironment.getHumidity();

		var sensor = LasRosasHeaders.sensor(imessage);
		if( sensor == null || sensor.equals("INT") ) {

			if( temp != null) fridge.setInsideTemp(temp);
			if( humidity != null ) fridge.setInsideHumidity(humidity);

		} else if( sensor.equals("EXT") ) {
			if(temp != null) fridge.setOutsideTemp(temp);

		} else
			log.warn("Fridge " + fridge.getTechid() + " Receive data with unknown sensor " + sensor);

		var telemetry = new FridgeTemperature(fridge.getStatus(), fridge.getInsideTemp(), fridge.getInsideHumidity(), fridge.getOutsideTemp());
		ReactContext.addTelemetry(telemetry);
	}
}
