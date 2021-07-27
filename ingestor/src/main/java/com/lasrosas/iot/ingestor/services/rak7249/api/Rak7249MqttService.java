package com.lasrosas.iot.ingestor.services.rak7249.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import com.lasrosas.iot.ingestor.services.rak7249.impl.Rak7249MessageParser;
import com.lasrosas.iot.ingestor.shared.MqttAdapterOptions;

/*
 * Spring integration event handler to process incoming mqtt messages.
 */
public class Rak7249MqttService {

	@Autowired
	private Rak7249MessageParser parser;

	public MqttPahoMessageDrivenChannelAdapter mqttRAK7942ChannelAdapter(MqttPahoClientFactory factory, MqttAdapterOptions options, MessageChannel outputChannel) {
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(options.getClientId(), factory, options.getTopics());

		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setOutputChannel(outputChannel);

		if( options.getQos() != null) adapter.setQos(options.getQos());
		if( options.getCompletionTimeout() != null) adapter.setCompletionTimeout(options.getCompletionTimeout());
		if( options.getDisconnectCompletionTimeout() != null) adapter.setDisconnectCompletionTimeout(options.getDisconnectCompletionTimeout());

		return adapter;
	}

	public Rak7249Message handle(String topic, Message<?> imessage) {
		return parser.parse(topic, imessage);
	}
}
