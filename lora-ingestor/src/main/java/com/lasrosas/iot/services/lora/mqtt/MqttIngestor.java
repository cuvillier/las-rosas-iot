package com.lasrosas.iot.services.mqtt;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import com.lasrosas.iot.services.mqtt.MqttReader;

public class MqttIngestor implements Consumer<Mqtt5Publish> {
	public static Logger log = LoggerFactory.getLogger(MqttIngestor.class);
	private MqttReader mqtt;
	private String topic;

	public MqttIngestor(MqttReader mqtt, String topic) {

		this.mqtt = mqtt;
		this.topic = topic;

		mqtt.connect();
		this.mqtt.subscribe(this.topic, this);
	}

	@Override
	public void accept(Mqtt5Publish t) {
		String payload = new String(t.getPayloadAsBytes());
		log.info("Receive payload " + payload);
		t.acknowledge();
	}
}
