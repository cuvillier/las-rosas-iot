package com.lasrosas.iot.ingestor.adapters.gateways.ha;

import com.lasrosas.iot.ingestor.domain.message.ThingConfigMessageEvent;
import com.lasrosas.iot.ingestor.domain.model.thing.Thing;
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
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class HaPublisherConfig {


    @Bean
    public MessageChannel haOutputChannel(MqttPahoClientFactory haClientFactory, MessageHandler haOutbound) {
        var channel = new DirectChannel();
        channel.subscribe(haOutbound);
        return channel;
    }

    @Bean
    @ServiceActivator(inputChannel = "haOutputChannel")
    public MessageHandler haOutbound(MqttPahoClientFactory mqttClientFactory) {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("ingestor-ha-publisher", mqttClientFactory);
        messageHandler.setAsync(true);
        return messageHandler;
    }

    @MessagingGateway(defaultRequestChannel = "outputChannel")
    public interface HaPublisher {
        @Transactional
        void send(@Header(MqttHeaders.TOPIC) String topic, String message);
    }

    @Bean
    public ApplicationListener<ThingConfigMessageEvent> publishHaThingMessages(HaPublisher haPublisher) {

        return new ApplicationListener<ThingConfigMessageEvent>() {

            @Override
            public void onApplicationEvent(ThingConfigMessageEvent event) {
/*
                var message = event.getMessage();
                var topic = createConfigurationTopic(event.getThing(), "");

                var haConfigMessage = createHaConfigMessage(event.getThing());
                var json = JsonUtils.toJson(haConfigMessage, true);

                log.info("MQTT Message published to " + topic + ":\n" + json);

                haPublisher.send(topic, json);
 */
            }
        };
    }

    private String createHaConfigMessage(Thing thing) {
        /*
        var type = thing.getType();
        if(type)

         */
        return "";
    }

    private String createConfigurationTopic(Thing thing, String schema) {
        var domain = thing.getType().getHomeAssistantDomain();
        var object_id = thing.getType().getHomeAssistantTypePrefix() + "-" + thing.getNaturalid() + "-" + schema;
        return  String.format("homeassistant/%s/%s/config", domain, object_id);
    }
}
