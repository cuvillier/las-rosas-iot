package com.lasrosas.iot.core.reactor.reactores;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lasrosas.iot.core.database.entities.dtw.TwinReactorReceiver;
import com.lasrosas.iot.core.database.entities.dtw.TwinReactorReceiverFromThing;
import com.lasrosas.iot.core.database.twins.MultiSwitch;
import com.lasrosas.iot.core.reactor.base.ReactContext;
import com.lasrosas.iot.core.shared.telemetry.ConnectionState;
import com.lasrosas.iot.core.shared.telemetry.MultiSwitchOrder;
import com.lasrosas.iot.core.shared.telemetry.MultiSwitchValue;
import com.lasrosas.iot.core.shared.telemetry.Order;
import com.lasrosas.iot.core.shared.telemetry.Switched;
import com.lasrosas.iot.core.shared.telemetry.Telemetry;

public class MultiSwitchReactorTest {
	public static final Logger log = LoggerFactory.getLogger(MultiSwitchReactorTest.class);

	private MultiSwitchReactor reactor;
	private Gson gson = new GsonBuilder().setPrettyPrinting().create();

	@Test
	public void test() {
		ReactContext.push();
		reactor = new MultiSwitchReactor();

		MultiSwitch multiSwitch = new MultiSwitch();
		multiSwitch.setStateWhenConnect(Optional.of(MultiSwitch.ON));
		multiSwitch.setExpectedState(MultiSwitch.ON);

		TwinReactorReceiver receiver = new TwinReactorReceiverFromThing();

		receiver.setTwin(multiSwitch);
		multiSwitch.getReceivers().add(receiver);


		// Set the initial state: not connected, expected ON
		var payloadNotConnected = ConnectionState.disconnected();

		testReact(receiver, payloadNotConnected);

		assertReactContextTelemetries(new MultiSwitchValue(0, 1, false));
		assertReactContextOrders();

		assertFalse(multiSwitch.isConnected());
		assertEquals(MultiSwitch.OFF, multiSwitch.getState());

		// Do connect
		var payloadConnected = new ConnectionState(true);
		testReact(receiver, payloadConnected);

		assertReactContextTelemetries( new MultiSwitchValue(MultiSwitch.ON, MultiSwitch.ON, true));
		assertReactContextOrders( new MultiSwitchOrder(1));

		Assertions.assertTrue(multiSwitch.isConnected());
		assertEquals(MultiSwitch.ON, multiSwitch.getState());

		// Do switch off
		var payloadSwitchOff = new Switched(Switched.State.OFF);
		testReact(receiver, payloadSwitchOff);

		assertReactContextTelemetries(new MultiSwitchValue(MultiSwitch.OFF, MultiSwitch.OFF, true));
		assertReactContextOrders();

		assertTrue(multiSwitch.isConnected());
		assertEquals(MultiSwitch.OFF, multiSwitch.getState());

		// Do switch on
		var payloadSwitchOn = new Switched(Switched.State.ON);
		testReact(receiver, payloadSwitchOn);

		assertReactContextTelemetries(new MultiSwitchValue(MultiSwitch.ON, MultiSwitch.ON, true));
		assertReactContextOrders();

		assertTrue(multiSwitch.isConnected());
		assertEquals(MultiSwitch.ON, multiSwitch.getState());
		
		throw new RuntimeException("Must be fixed");
	}

	private void testReact(TwinReactorReceiver receiver,  Telemetry inboundTelemetry) {
		ReactContext.clearAndPop();
		Message<Telemetry> telemetryConnected = MessageBuilder.withPayload(inboundTelemetry).build();

		reactor.react(receiver, telemetryConnected);
	}

	private void assertReactContextTelemetries(Object ... result) {
		var ctx = ReactContext.peek();

		assertEquals(result.length, ctx.getTelemetries().size());
		for(int i = 0; i < ctx.getTelemetries().size(); i++) {

			var telemetry1 = ctx.getTelemetries().get(i);
			var telemetry2 = result[i];

			String json1 = gson.toJson(telemetry1);
			String json2 = gson.toJson(telemetry2);

			assertEquals(json2, json1);
		}
	}

	private void assertReactContextOrders(Order... orders) {
		var ctx = ReactContext.peek();

		assertEquals(ctx.getOrders().size(), orders.length);
		for(int i = 0; i < ctx.getOrders().size(); i++) {

			var telemetry1 = ctx.getOrders().get(i);
			var telemetry2 = orders[i];

			String json1 = gson.toJson(telemetry1);
			String json2 = gson.toJson(telemetry2);

			assertEquals(json2, json1);
		}
	}
}
