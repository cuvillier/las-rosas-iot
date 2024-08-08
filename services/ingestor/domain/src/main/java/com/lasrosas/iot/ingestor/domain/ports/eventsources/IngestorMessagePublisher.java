package com.lasrosas.iot.ingestor.domain.ports.eventsources;

import com.lasrosas.iot.ingestor.domain.model.message.BaseMessage;

public interface IngestorMessagePublisher {
    void send(BaseMessage message);
    void send(String topic, String message);
}
