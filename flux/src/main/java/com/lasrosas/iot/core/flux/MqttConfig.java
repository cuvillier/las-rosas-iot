package com.lasrosas.iot.core.flux;

import javax.validation.constraints.NotNull;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.validation.annotation.Validated;

@Validated
public class MqttConfig {

	@NotNull
	private String clientId;

	private Long completionTimeout;
	private Long disconnectCompletionTimeout;
	private int[] qoss;
	private Integer recoveryInterval;
	private Long sendTimeout;
	private boolean shouldTrack;

	@NotNull
	public MqttConnectOptions connectOptions = new MqttConnectOptions();
	private String persistFolder;

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getPersistFolder() {
		return persistFolder;
	}

	public void setPersistFolder(String persistFolder) {
		this.persistFolder = persistFolder;
	}

	public MqttConnectOptions getConnectOptions() {
		return connectOptions;
	}

	public void setConnectOptions(MqttConnectOptions connectOptions) {
		this.connectOptions = connectOptions;
	}

	public Long getCompletionTimeout() {
		return completionTimeout;
	}

	public void setCompletionTimeout(Long completionTimeout) {
		this.completionTimeout = completionTimeout;
	}

	public Long getDisconnectCompletionTimeout() {
		return disconnectCompletionTimeout;
	}

	public void setDisconnectCompletionTimeout(Long disconnectCompletionTimeout) {
		this.disconnectCompletionTimeout = disconnectCompletionTimeout;
	}

	public int[] getQoss() {
		return qoss;
	}

	public void setQoss(int[] qoss) {
		this.qoss = qoss;
	}

	public Integer getRecoveryInterval() {
		return recoveryInterval;
	}

	public void setRecoveryInterval(Integer recoveryInterval) {
		this.recoveryInterval = recoveryInterval;
	}

	public Long getSendTimeout() {
		return sendTimeout;
	}

	public void setSendTimeout(Long sendTimeout) {
		this.sendTimeout = sendTimeout;
	}

	public boolean isShouldTrack() {
		return shouldTrack;
	}

	public void setShouldTrack(boolean shouldTrack) {
		this.shouldTrack = shouldTrack;
	}

}
