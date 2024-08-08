package com.lasrosas.iot.ingestor.adapters.gateways.mqtt;

import com.lasrosas.iot.ingestor.domain.message.ThingMessageEvent;
import com.lasrosas.iot.ingestor.shared.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
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
public class MqttPublisherConfig {


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
    public ApplicationListener<ThingMessageEvent> publishThingMessages(MqttPublisher mqttPublisher) {

        return new ApplicationListener<ThingMessageEvent>() {

            @Override
            public void onApplicationEvent(ThingMessageEvent event) {
                var message = event.getPayload();
                var topic = "thing/naturalid/" + event.getThing().getNaturalid();
                var json = JsonUtils.toJson(message, true);

                log.info("MQTT Message published to " + topic + ":\n" + json);

                mqttPublisher.send(topic, json);
            }
        };
    }
}
