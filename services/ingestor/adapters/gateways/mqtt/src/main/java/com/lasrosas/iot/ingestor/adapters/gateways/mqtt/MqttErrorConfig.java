package com.lasrosas.iot.ingestor.adapters.gateways.mqtt;

import com.lasrosas.iot.ingestor.shared.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.events.IntegrationEvent;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MqttErrorConfig {

    @Bean
    public MessageChannel errorChannel() {
        var queue = new QueueChannel(500);

        queue.addInterceptor(new ChannelInterceptor() {

            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {

                log.error("");
                log.error(message.getPayload().getClass().getName());
                log.error(JsonUtils.toJson(message.getPayload()));

                return message;
            }
        });

        return queue;
    }


    /**
     * Display errors.
     * @return
     */
    @Bean
    public ApplicationListener<?> eventListener() {
        return new ApplicationListener<IntegrationEvent>() {

            @Override
            public void onApplicationEvent(IntegrationEvent event) {
                log.error("IntegrationEvent", event.getCause());
            }

        };
    }
}
