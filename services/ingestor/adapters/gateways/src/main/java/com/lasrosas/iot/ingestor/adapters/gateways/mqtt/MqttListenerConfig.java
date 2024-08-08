package com.lasrosas.iot.ingestor.adapters.gateways.mqtt;

import com.lasrosas.iot.ingestor.domain.model.message.GatewayPayloadMessage;
import com.lasrosas.iot.ingestor.domain.model.message.GatewayPayloadMessageEvent;
import com.lasrosas.iot.ingestor.shared.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.events.IntegrationEvent;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class MqttListenerConfig {

    @Bean
    public MessageChannel gatewayInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inbound(MessageChannel gatewayInputChannel, MqttPahoClientFactory mqttClientFactory) {

        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter("ingestor-gateway", mqttClientFactory, "gateway/#");

        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(gatewayInputChannel);
        return adapter;
    }

    /**
     * Handle messages from a thing gateway.
     * This code is not specific to any gateway.
     *
     * Example from a Lorawan RAK gateway:
     {
         "payload" : "{\"applicationID\":\"1\",\"applicationName\":\"las-rosas-iot\",\"devEUI\":\"a8404152318446ea\",\"deviceName\":\"DRAGINO/LHT65/a8404152318446ea\",\"timestamp\":1722712584,\"fCnt\":31014,\"fPort\":2,\"data\":\"zBYMhgFXAX//f/8=\",\"data_encode\":\"base64\",\"adr\":true,\"rxInfo\":[{\"gatewayID\":\"60c5a8fffe76f8b2\",\"loRaSNR\":0.0,\"rssi\":-88,\"location\":{\"latitude\":36.825600,\"longitude\":-5.579470,\"altitude\":311},\"time\":\"2024-08-03T19:16:31.723856Z\"}],\"txInfo\":{\"frequency\":868800000,\"dr\":7}}",
         "headers" : {
             "mqtt_receivedRetained" : true,
             "mqtt_id" : 7,
             "mqtt_duplicate" : false,
             "id" : "b5759ba3-df7c-8340-f649-81e2aed520e8",
             "mqtt_receivedTopic" : "gateway/RAK/application/1/device/a8404152318446ea/rx",
             "mqtt_receivedQos" : 1,
             "timestamp" : 1722713547375
         }
     }
     */

    @Bean
    @ServiceActivator(inputChannel = "gatewayInputChannel")
    public MessageHandler handler(ApplicationEventPublisher publisher) {
        return new MessageHandler() {

            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                String jsonMessage = JsonUtils.toJson(message, true);

                try {

                    var topic = message.getHeaders().get("mqtt_receivedTopic").toString();

                    MqttConnectionConfig.logMessage.info("Message received from " + topic + ":\n"+ jsonMessage);

                    var gatewayNaturalId = topic.split("/")[1];
                    var id = message.getHeaders().get("id").toString();

                    var gatewayMessage = GatewayPayloadMessage.builder()
                            .topic(topic)
                            .correlationId(id)
                            .gatewayNaturalId(gatewayNaturalId)
                            .time(LocalDateTime.now())
                            .json(message.getPayload().toString())     // Json payload
                            .build();

                    // handle the message with the injected listener from the UseCase ring
                    publisher.publishEvent( new GatewayPayloadMessageEvent(this, gatewayMessage));

                } catch (RuntimeException e) {
                    log.error("Error while processing the message: \n" + jsonMessage, e);
                }
            }
        };
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
