package com.lasrosas.iot.services.mqtt;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.lifecycle.MqttClientConnectedContext;
import com.hivemq.client.mqtt.lifecycle.MqttClientConnectedListener;
import com.hivemq.client.mqtt.lifecycle.MqttClientDisconnectedContext;
import com.hivemq.client.mqtt.lifecycle.MqttClientDisconnectedListener;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.message.connect.connack.Mqtt5ConnAck;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5PublishResult;
import com.hivemq.client.mqtt.mqtt5.message.subscribe.Mqtt5Subscribe;
import com.hivemq.client.mqtt.mqtt5.message.subscribe.suback.Mqtt5SubAck;

public class MqttReader implements MqttClientConnectedListener, MqttClientDisconnectedListener  {
	public static Logger log = LoggerFactory.getLogger(MqttReader.class);
	private final String name;
	private String server = "localhost";
	private int port = 1883;
	private boolean connected = false;
	private Duration initialDelay = Duration.ofSeconds(1);
	private Duration maxDelay = Duration.ofSeconds(15);
	private Mqtt5AsyncClient mqtt = null;

	public static final void main(String ... args) {

		try {
			var reader = new MqttReader("MQTT Reader");
			reader.connect();
			reader.publishAsync("test", "poipoipoi");
			reader.subscribe("test",System.out::println);
		} catch(Exception e) {
			System.out.print("==========> Exception");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		System.out.print("==========> End main thread");
	}

	public MqttReader(String name) {
		this.name = name;
	}

	private synchronized Mqtt5AsyncClient mqtt() {
		if(this.mqtt == null) {
			this.mqtt = MqttClient.builder()
			        .identifier(this.name)
			        .serverHost(this.server)
			        .serverPort(this.port)
			        .useMqttVersion5()
			        .addConnectedListener(this)
			        .addDisconnectedListener(this)
			        .automaticReconnect()
			        .initialDelay(this.initialDelay.getSeconds(), TimeUnit.SECONDS)
		            .maxDelay(this.maxDelay.getSeconds(), TimeUnit.SECONDS)
		            .applyAutomaticReconnect()
			        .build().toAsync();
		}

		return this.mqtt;
	}

	public synchronized CompletableFuture<Mqtt5ConnAck> connect() {

		try {
			return mqtt().connect();
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	public CompletableFuture<Mqtt5SubAck> subscribe(String topic, Consumer<Mqtt5Publish> consumer) {
		var subscribeMessage = Mqtt5Subscribe.builder()
		        .topicFilter("test")
		        .qos(MqttQos.EXACTLY_ONCE)
		        .build();

		return this.mqtt().subscribe(subscribeMessage, consumer);		
	}

	public CompletableFuture<Mqtt5PublishResult> publishAsync(String topic, String payload) {
		return mqtt().publishWith().topic(topic).payload(payload.getBytes()).send();		
	}

	public void disconnect() {
		mqtt().disconnect();
	}

	public boolean isConnected() {
		return connected;
	}

	@Override
	public void onConnected(MqttClientConnectedContext ctx) {
		log.info(this.name + " is connected");
		connected = true;
	}

	@Override
	public void onDisconnected( MqttClientDisconnectedContext ctx) {
		log.info(this.name + " is disconnected");
		connected = false;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Duration getInitialDelay() {
		return initialDelay;
	}

	public void setInitialDelay(Duration initialDelay) {
		this.initialDelay = initialDelay;
	}

	public Duration getMaxDelay() {
		return maxDelay;
	}

	public void setMaxDelay(Duration maxDelay) {
		this.maxDelay = maxDelay;
	}

	public String getName() {
		return name;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}
}
