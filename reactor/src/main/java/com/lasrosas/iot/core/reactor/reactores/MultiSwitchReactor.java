package com.lasrosas.iot.core.reactor.reactores;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;

import com.lasrosas.iot.core.database.entities.dtw.TwinReactorReceiver;
import com.lasrosas.iot.core.database.twins.MultiSwitch;
import com.lasrosas.iot.core.reactor.base.ReactContext;
import com.lasrosas.iot.core.reactor.base.TwinReactor;
import com.lasrosas.iot.core.shared.telemetry.ConnectionStage;
import com.lasrosas.iot.core.shared.telemetry.ConnectionState;
import com.lasrosas.iot.core.shared.telemetry.MultiSwitchOrder;
import com.lasrosas.iot.core.shared.telemetry.MultiSwitchValue;
import com.lasrosas.iot.core.shared.telemetry.Switched;

public class MultiSwitchReactor implements TwinReactor {
	public static final String RECEIVER_SWITCH_STATE = "state";

	public static final Logger log = LoggerFactory.getLogger(WaterTankReactor.class);

	/**
	 * Sensor:
	 *  Disconnect
	 *  Connect
	 * 	SwitchOn
	 *	SwitchOff
	 */
	@Override
	public void react(TwinReactorReceiver receiver, Message<?> imessage) {

		// Only Thing receivers are supported
		var switchTwin = receiver.<MultiSwitch>getTwin();
		var payload = imessage.getPayload();

		log.info("MultiSwitch " + switchTwin.getName() + " handle message" + payload);

		boolean sendValue = false;

		if( payload instanceof Switched ) {

			// weired, but force the sensor to be connected
			if( !switchTwin.isConnected() ) {
				log.warn("Multiswitch state change, but is not connected. Force connected to true");
				switchTwin.setConnected(1);
			}

			var switched = (Switched)payload;

			switch(switched.getState()) {
			case ON:

				if( switchTwin.getState() != 1) {
					log.info("Multiswitch was Switched ON");
					switchTwin.setExpectedState(1);
					switchTwin.setState(1);
					sendValue = true;
				} else
					log.warn("Multiswitch was alredy Switched ON");
				break;

			case OFF:

				if( switchTwin.getState() != 0) {
					log.info("Multiswitch was Switched OFF");
					switchTwin.setExpectedState(0);
					switchTwin.setState(0);
					sendValue = true;
				} else
					log.warn("Multiswitch was alredy Switched OFF");


				break;
			}

			// If the state is not wath it should be, change the switch status
			// Happen after STAGE_JOINING when the sensor just reboot.
			if( switchTwin.isConnected() && switchTwin.getState() != switchTwin.getExpectedState() ) {
				log.info("Multiswitch state forced to " + switchTwin.getExpectedState());
				ReactContext.addOrder(new MultiSwitchOrder(switchTwin.getExpectedState()));
			}

		} else if( payload instanceof ConnectionState) {

			var ctxState = (ConnectionState)payload;
			switchTwin.setConnected(ctxState.getConnected().get());

		} else if( payload instanceof ConnectionStage) {
			var ctxStage = (ConnectionStage)payload;

			switch( ctxStage.getStage() ) {
			case ConnectionStage.JOINING:

				// Join start, reset the sensor state if needed
				if(switchTwin.getStateWhenConnect().isPresent()) {
					var newState = switchTwin.getStateWhenConnect().get();
					if( switchTwin.getState() != newState) {
						switchTwin.setState(switchTwin.getStateWhenConnect().get());
						sendValue = true;
					}
				}

				if(switchTwin.isConnected()) {
					switchTwin.setConnected(0);
					ReactContext.addTelemetry(ConnectionState.disconnected());
				}
				break;

			case ConnectionStage.JOINED:

				// These sensor lots the state when the power is on
				log.info("Multiswitch Joined now");
				if(switchTwin.getStateWhenConnect().isPresent()) {
					var newState = switchTwin.getStateWhenConnect().get();
					if( switchTwin.getState() != newState) {
						switchTwin.setState(switchTwin.getStateWhenConnect().get());
						sendValue = true;
					}
				}

				if( ! switchTwin.isConnected()) {
					switchTwin.setConnected(1);
					ReactContext.addTelemetry(ConnectionState.connected());
				}

				break;

			default:
				return;
			}
		}

		if(sendValue)
			ReactContext.addTelemetry(new MultiSwitchValue(switchTwin.getState(), switchTwin.getExpectedState(), switchTwin.isConnected()));
	}
}
