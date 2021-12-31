package com.lasrosas.iot.core.flux;

import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttMessageConverter;

public class ConfigUtils {

	public static MqttPahoMessageDrivenChannelAdapter mqttChannelAdapter(String topic, MqttConfig config, MqttPahoClientFactory clientFactory, MqttMessageConverter mqttMessageConverter) {

		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
				config.getClientId(),
				clientFactory,
				topic);

		adapter.setConverter(mqttMessageConverter == null? new DefaultPahoMessageConverter(): mqttMessageConverter);

		// Set options from config
		if( config.getCompletionTimeout() != null) adapter.setCompletionTimeout(config.getCompletionTimeout());
		if( config.getQoss() != null) adapter.setQos(config.getQoss());
		if( config.getDisconnectCompletionTimeout() != null) adapter.setDisconnectCompletionTimeout(config.getDisconnectCompletionTimeout());
		if( config.getRecoveryInterval() != null) adapter.setRecoveryInterval(config.getRecoveryInterval());
		if( config.getSendTimeout() != null) adapter.setSendTimeout(config.getSendTimeout());

		return adapter;
	}
}
