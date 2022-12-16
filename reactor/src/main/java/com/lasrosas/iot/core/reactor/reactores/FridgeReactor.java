package com.lasrosas.iot.core.reactor.reactores;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;

import com.lasrosas.iot.core.database.entities.dtw.TwinReactorReceiver;
import com.lasrosas.iot.core.database.twins.Fridge;
import com.lasrosas.iot.core.reactor.base.TwinReactor;
import com.lasrosas.iot.core.shared.telemetry.AirEnvironment;
import com.lasrosas.iot.core.shared.utils.LasRosasHeaders;

public class FridgeReactor implements TwinReactor {
	public static final Logger log = LoggerFactory.getLogger(FridgeReactor.class);

	public FridgeReactor() {
	}

	@Override
	public void react(TwinReactorReceiver receiver, Message<?> imessage) {

		if (!receiver.getType().getRole().equals("level"))
			throw new RuntimeException("Unexpected receiver role " + receiver.getType().getRole());

		var fridge = receiver.<Fridge>getTwin();
		var payload = imessage.getPayload();

		if( !( payload instanceof AirEnvironment) ) return;
		var airEnvironment = (AirEnvironment)payload;

		var temp = airEnvironment.getTemperature();
		var humidity = airEnvironment.getTemperature();

		var sensor = LasRosasHeaders.sensor(imessage);
		if( sensor == null || sensor.equals("INT") ) {
			
			if(temp != null) {
				fridge.setInsideTemp(temp);
	
				// Temp code, should send an alarm.
				if( fridge.getInsideTempMax() != null && temp > fridge.getInsideTempMax())
					log.error("Fridge temp abovethe max: " + temp + " > " + fridge.getInsideTempMax());
	
				if( fridge.getInsideTempMin() != null && temp < fridge.getInsideTempMin())
					log.error("Fridge temp below the min: " + temp + " < " + fridge.getInsideTempMin());
			}

			if( humidity != null )
				fridge.setInsideHumidity(humidity);

		} else if( sensor.equals("EXT") ) {
			if(temp != null)
				fridge.setOutsideTemp(temp);
		} else
			log.warn("Fridge " + fridge.getTechid() + " Receive data with unknown sensor " + sensor);


		/*
		var wtf = new WaterTankFilling(volume, percentage, waterFlow);

		ReactContext.addTelemetry(wtf);
		*/
	}
}
