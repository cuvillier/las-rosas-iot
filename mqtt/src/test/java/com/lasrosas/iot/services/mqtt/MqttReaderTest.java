package com.lasrosas.iot.services.mqtt;

import static org.junit.Assert.assertEquals;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import com.hivemq.testcontainer.junit5.HiveMQTestContainerExtension;

public class MqttReaderTest {

	@RegisterExtension
	final public @NotNull HiveMQTestContainerExtension extension = new HiveMQTestContainerExtension();
	
	private int counter = 0;

	@Test
	void test_mqtt() throws Exception {

		MqttReader mqtt = new MqttReader("unit test");
		mqtt.setPort(extension.getMqttPort());
		mqtt.connect().get();
		mqtt.subscribe("test", m -> {counter++; System.out.println(new String(m.getPayloadAsBytes()));}).get();
		mqtt.publishAsync("test", "******************** JUnit test MQTT").get();

		// Wait for the event to be processed
		Thread.sleep(1000);

		mqtt.disconnect();

		assertEquals(1, counter);
	}
}
