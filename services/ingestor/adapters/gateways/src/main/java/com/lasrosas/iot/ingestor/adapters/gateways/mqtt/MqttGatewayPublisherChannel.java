package com.lasrosas.iot.ingestor.adapters.gateways.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lasrosas.iot.ingestor.shared.exceptions.InvalidJsonFormatException;
import com.lasrosas.iot.ingestor.domain.model.message.ThingMessage;
import com.lasrosas.iot.ingestor.domain.ports.eventsources.IngestorMessagePublisher;
import com.lasrosas.iot.ingestor.shared.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MqttGatewayPublisherChannel {


    @Bean
    public MessageChannel outputChannel(MqttPahoClientFactory mqttClientFactory, MessageHandler outbound) {
        var channel = new DirectChannel();
        channel.subscribe(outbound);
        return channel;
    }

    @Bean
    @ServiceActivator(inputChannel = "outputChannel")
    public MessageHandler outbound(MqttPahoClientFactory mqttClientFactory) {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("ingestor", mqttClientFactory);
        messageHandler.setAsync(true);
        return messageHandler;
    }

    @MessagingGateway(defaultRequestChannel = "outputChannel")
    public interface MqttPublisher {
        void send(@Header(MqttHeaders.TOPIC) String topic, String message);
    }

    @Bean
    public IngestorMessagePublisher IngestorMessagePublisher(MqttPublisher publisher) {

        return new IngestorMessagePublisher() {

            @Override
            public void send(ThingMessage message) {

                var topic = "thing/naturalid/" + message.getThingNaturalid();
                var json = JsonUtils.toJson(message, true);

                log.info("Message published to " + topic + ":\n" + json);

                publisher.send(topic, json);
            }

            @Override
            public void send(String topic, String message) {
                publisher.send(topic, message);
            }
        };
    }

}
