package com.lasrosas.iot.ingestor.domain.ports.stores;

import com.lasrosas.iot.ingestor.domain.message.ThingEventMessage;

public interface TimeSerieStoreCommand {
    void insertPoint(ThingEventMessage event);
}
