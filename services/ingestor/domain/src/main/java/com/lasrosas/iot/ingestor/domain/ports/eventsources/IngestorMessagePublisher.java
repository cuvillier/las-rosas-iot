package com.lasrosas.iot.ingestor.domain.ports.eventsources;

import com.lasrosas.iot.ingestor.domain.model.message.ThingMessage;

public interface IngestorMessagePublisher {
    void send(ThingMessage message);
    void send(String topic, String message);
}
