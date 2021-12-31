package com.lasrosas.iot.core.reactor.reactores;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lasrosas.iot.core.database.entities.dtw.TwinReactorReceiver;
import com.lasrosas.iot.core.database.entities.dtw.TwinReactorReceiverFromThing;
import com.lasrosas.iot.core.database.finca.MultiSwitch;
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
		var payloadNotConnected = new ConnectionState(false);

		testReact(receiver, payloadNotConnected);

		assertReactContextTelemetries();
		assertReactContextOrders();

		Assert.assertFalse(multiSwitch.isConnected());
		Assert.assertEquals(MultiSwitch.OFF, multiSwitch.getState());

		// Do connect
		var payloadConnected = new ConnectionState(true);
		testReact(receiver, payloadConnected);

		assertReactContextTelemetries( new MultiSwitchValue(MultiSwitch.ON, MultiSwitch.ON, true));
		assertReactContextOrders( new MultiSwitchOrder(1));

		Assert.assertTrue(multiSwitch.isConnected());
		Assert.assertEquals(MultiSwitch.ON, multiSwitch.getState());

		// Do switch off
		var payloadSwitchOff = new Switched(MultiSwitch.OFF);
		testReact(receiver, payloadSwitchOff);

		assertReactContextTelemetries(new MultiSwitchValue(MultiSwitch.OFF, MultiSwitch.OFF, true));
		assertReactContextOrders();

		Assert.assertTrue(multiSwitch.isConnected());
		Assert.assertEquals(MultiSwitch.OFF, multiSwitch.getState());

		// Do switch on
		var payloadSwitchOn = new Switched(MultiSwitch.ON);
		testReact(receiver, payloadSwitchOn);

		assertReactContextTelemetries(new MultiSwitchValue(MultiSwitch.ON, MultiSwitch.ON, true));
		assertReactContextOrders();

		Assert.assertTrue(multiSwitch.isConnected());
		Assert.assertEquals(MultiSwitch.ON, multiSwitch.getState());
}

	private void testReact(TwinReactorReceiver receiver,  Telemetry inboundTelemetry) {
		ReactContext.clearAndPop();
		Message<Telemetry> telemetryConnected = MessageBuilder.withPayload(inboundTelemetry).build();

		reactor.react(receiver, telemetryConnected);
	}

	private void assertReactContextTelemetries(Telemetry ... telemetries) {
		var ctx = ReactContext.peek();

		assertEquals(telemetries.length, ctx.getTelemetries().size());
		for(int i = 0; i < ctx.getTelemetries().size(); i++) {

			var telemetry1 = ctx.getTelemetries().get(i);
			var telemetry2 = telemetries[i];

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
