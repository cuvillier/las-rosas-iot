package com.lasrosas.iot.ingestor.usecases.handleThingMessage;

import com.lasrosas.iot.ingestor.domain.model.message.GatewayPayloadMessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ThingMessageListener implements ApplicationListener<GatewayPayloadMessageEvent> {

    @Override
    public void onApplicationEvent(GatewayPayloadMessageEvent event) {
        log.info("Message for digital twin");
    }
}
