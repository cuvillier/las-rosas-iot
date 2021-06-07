package com.lasrosas.iot.mqtt.session;

import javax.annotation.PreDestroy;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.validation.annotation.Validated;

@Configuration
@Validated
public class MqttSession {
	public static final Logger log = LoggerFactory.getLogger(MqttSession.class);

	@NotNull
	private String clientId;

	@NotNull
	private String server;

	@NotNull
	private String persistFolder;

	@Min(0)
	@Max(32000)
	private int port = 1883;

	private boolean automaticReconnect = true;

	private boolean cleanSession = false;

	@Min(0)
	private int connectionTimeout = 10;

	@Autowired
	private PlatformTransactionManager txManager;
	
	private IMqttClient mqtt;

	public IMqttClient getClient() {
		if (mqtt == null)
			open();
		return mqtt;
	}

	private void open() {
		try {
			log.info("Connect to mqtt server=" + server + ", port=" + port);
			if (mqtt != null)
				return;

			var persist = new MqttDefaultFilePersistence(persistFolder);
			this.mqtt = new MqttClient("tcp://" + server + ":" + port, clientId, persist);

			MqttConnectOptions options = new MqttConnectOptions();
			options.setAutomaticReconnect(this.automaticReconnect);
			options.setCleanSession(this.cleanSession);
			options.setConnectionTimeout(this.connectionTimeout);
			this.mqtt.connect(options);

			log.info("Connected to mqtt server=" + server + ", port=" + port);
		} catch (MqttException e) {
			throw new RuntimeException(e);
		}
	}

	@PreDestroy
	private void close() {
		try {
			if (mqtt != null)
				this.mqtt.close();
			log.info("Disconnected from mqtt");
		} catch (MqttException e) {
			throw new RuntimeException(e);
		}
	}

	@FunctionalInterface
	public interface MqttListener {
		void handle(String topic, MqttMessage message);
	}

	/*
	 * Transactions need to be handled manually because the lambda
	 * call does not go through the hibernate proxy, and there is no tx.
	 */
	public void subscribe(String topicEx, MqttListener listener) {
		try {
			getClient().subscribe(topicEx, (topic, message) -> {
				DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
				definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
				definition.setName("MQTT handler");
				TransactionStatus status = txManager.getTransaction(definition);
				try {
					listener.handle(topic, message);
					txManager.commit(status);
				} catch (RuntimeException e) {
					txManager.rollback(status);
					log.error("Cannot process message.", e);
				}
			});
		} catch (MqttException e) {
			throw new RuntimeException(e);
		}
	}

	public void unsubscribe(String topicEx) {
		try {
			getClient().unsubscribe(topicEx);
		} catch (MqttException e) {
			throw new RuntimeException(e);
		}
	}

	public void publish(String topic, MqttMessage message) {
		try {
			getClient().publish(topic, message);
		} catch (MqttException e) {
			throw new RuntimeException(e);
		}

	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
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

	public boolean isAutomaticReconnect() {
		return automaticReconnect;
	}

	public void setAutomaticReconnect(boolean automaticReconnect) {
		this.automaticReconnect = automaticReconnect;
	}

	public boolean isCleanSession() {
		return cleanSession;
	}

	public void setCleanSession(boolean cleanSession) {
		this.cleanSession = cleanSession;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public String getPersistFolder() {
		return persistFolder;
	}

	public void setPersistFolder(String persistFolder) {
		this.persistFolder = persistFolder;
	}
}
