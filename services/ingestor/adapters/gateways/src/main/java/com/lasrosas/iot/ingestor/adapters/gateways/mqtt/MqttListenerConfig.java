package com.lasrosas.iot.ingestor.adapters.gateways.mqtt;

import com.lasrosas.iot.ingestor.domain.message.GatewayPayloadMessage;
import com.lasrosas.iot.ingestor.domain.message.GatewayPayloadMessageEvent;
import com.lasrosas.iot.ingestor.shared.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@Slf4j
public class MqttListenerConfig {

    @Bean
    public MessageChannel gatewayInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducerSupport gatewayInbound(MessageChannel gatewayInputChannel, MqttPahoClientFactory mqttClientFactory) {

        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter("ingestor-gateway", mqttClientFactory, "gateway/#");

        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(gatewayInputChannel);
        return adapter;
    }

    @Bean
    public IntegrationFlow forwardMqttFLowToTheTransationalGateway(MessageProducerSupport gatewayInbound, TransactionalGateway gateway) {
        return IntegrationFlow.from(gatewayInbound)
                .handle(gateway::sendIntransaction)
                .get();
    }

    @MessagingGateway
    public interface TransactionalGateway {

        @Gateway(requestChannel = "gatewayTransactionInputChannel")
        @Transactional
        void sendIntransaction(Message<?> msg);
    }

    @Bean
    public MessageChannel gatewayTransactionInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow handleFLowInTransactionalScope(ApplicationEventPublisher publisher, MessageChannel gatewayTransactionInputChannel) {
        return IntegrationFlow.from(gatewayTransactionInputChannel).handle( m-> handle(publisher, m)).get();
    }

    public void handle(ApplicationEventPublisher publisher, Message<?> message) {
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


    @Bean
    @ServiceActivator(inputChannel = "gatewayInputChannel")
    public MessageHandler handler(ApplicationEventPublisher publisher) {
        return new MessageHandler() {

            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
            }
        };
    }

}
