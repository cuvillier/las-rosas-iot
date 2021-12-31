package com.lasrosas.iot.core.reactor.reactores;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;

import com.lasrosas.iot.core.database.entities.dtw.TwinReactorReceiver;
import com.lasrosas.iot.core.database.finca.MultiSwitch;
import com.lasrosas.iot.core.reactor.base.ReactContext;
import com.lasrosas.iot.core.reactor.base.TwinReactor;
import com.lasrosas.iot.core.shared.telemetry.ConnectionState;
import com.lasrosas.iot.core.shared.telemetry.MultiSwitchOrder;
import com.lasrosas.iot.core.shared.telemetry.MultiSwitchValue;
import com.lasrosas.iot.core.shared.telemetry.Switched;
import com.lasrosas.iot.core.shared.telemetry.Telemetry;

public class MultiSwitchReactor implements TwinReactor {
	public static final String RECEIVER_SWITCH_STATE = "state";

	public static final Logger log = LoggerFactory.getLogger(WaterTankReactor.class);

	public void switchOnOff(MultiSwitch twin, int expectedState) {
		twin.setExpectedState(expectedState);
	}

	/**
	 * Sensor:
	 *  Disconnect
	 *  Connect
	 * 	SwitchOn
	 *	SwitchOff
	 */
	@Override
	public void react(TwinReactorReceiver receiver, Message<? extends Telemetry> imessage) {

		// Only Thing receivers are supported
		var switchTwin = receiver.<MultiSwitch>getTwin();
		var payload = imessage.getPayload();
		var previousState = switchTwin.getState();
		var previousConnected = switchTwin.isConnected();

		if( payload instanceof Switched ) {

			var switched = (Switched)payload;
			switchTwin.setState(switched.getState());
			switchTwin.setExpectedState(switched.getState());

		} else if( payload instanceof ConnectionState ) {

			var connectionState = (ConnectionState)payload;
			if( switchTwin.isConnected() == connectionState.isConnected()) return;

			switchTwin.setConnected(connectionState.isConnected());

			if( switchTwin.isConnected() && switchTwin.getStateWhenConnect().isPresent()) {

				// If isOffWhenDisconnected and connected, restore switch the switch to on to restore the excpected state
				var newState = switchTwin.getStateWhenConnect().get();
				ReactContext.addOrder(new MultiSwitchOrder(newState));

				switchTwin.setState(newState);
				switchTwin.setExpectedState(newState);
			}
		}

		if( previousState != switchTwin.getState() || previousConnected != switchTwin.isConnected())
			ReactContext.addTelemetry(new MultiSwitchValue(switchTwin.getState(), switchTwin.getExpectedState(), switchTwin.isConnected()));
	}
}
