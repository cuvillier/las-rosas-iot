package com.lasrosas.iot.ingestor.domain.message;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class GatewayPayloadMessageEvent extends ApplicationEvent {
    private final GatewayPayloadMessage message;

    public GatewayPayloadMessageEvent(Object source, GatewayPayloadMessage message) {
        super(source);
        this.message = message;
    }
}
