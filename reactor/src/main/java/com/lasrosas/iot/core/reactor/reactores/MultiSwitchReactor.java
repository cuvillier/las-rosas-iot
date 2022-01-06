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
	public void react(TwinReactorReceiver receiver, Message<?> imessage) {

		// Only Thing receivers are supported
		var switchTwin = receiver.<MultiSwitch>getTwin();
		var payload = imessage.getPayload();
		boolean boot;

		if( payload instanceof Switched ) {

			// wweired, but the sensor is connected
			if( !switchTwin.isConnected() ) {
				log.warn("Multiswitch state change, but is not connected. Force connected to true");
				switchTwin.setConnected(1);
			}

			var switched = (Switched)payload;
			switchTwin.setState(switched.getState());
			switchTwin.setExpectedState(switched.getState());

			boot = false;

		} else if( payload instanceof ConnectionState ) {

			var connectionState = (ConnectionState)payload;
			if( connectionState.getRemind() == 1 && switchTwin.isConnected() == connectionState.isConnected()) return;

			boot = connectionState.getCause() == ConnectionState.CAUSE_NTW_JOIN;
			switchTwin.setConnected(connectionState.getConnected());

			if( switchTwin.isConnected() && switchTwin.getStateWhenConnect().isPresent()) {

				// If isOffWhenDisconnected and connected, restore switch the switch to on to restore the excpected state
				var newState = switchTwin.getStateWhenConnect().get();
				ReactContext.addOrder(new MultiSwitchOrder(newState));
	
				switchTwin.setState(newState);
				switchTwin.setExpectedState(newState);
			}
		} else
			return;

		ReactContext.addTelemetry(new MultiSwitchValue(switchTwin.getState(), switchTwin.getExpectedState(), switchTwin.isConnected(), boot));
	}
}
