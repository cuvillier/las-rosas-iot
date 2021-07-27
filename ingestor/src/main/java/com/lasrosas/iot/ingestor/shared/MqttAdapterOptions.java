package com.lasrosas.iot.ingestor.shared;

public class MqttAdapterOptions {
	private String clientId;
	private Integer qos;
	private Integer recoveryInterval;
	private Long completionTimeout;
	private Long disconnectCompletionTimeout;
	private String[] topics = {"application/+/device/+/+"};

	public String[] getTopics() {
		return topics;
	}
	public void setTopics(String[] topics) {
		this.topics = topics;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public Integer getQos() {
		return qos;
	}
	public void setQos(Integer qos) {
		this.qos = qos;
	}
	public Integer getRecoveryInterval() {
		return recoveryInterval;
	}
	public void setRecoveryInterval(Integer recoveryInterval) {
		this.recoveryInterval = recoveryInterval;
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

}
