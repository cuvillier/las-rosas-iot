package com.lasrosas.iot.ingestor.domain.ports.eventsources;

import com.lasrosas.iot.ingestor.domain.model.message.GatewayPayloadMessage;

public interface GatewayMessageListener {
    void onMessage(GatewayPayloadMessage message);
}
