package com.lasrosas.iot.ingestor.domain.ports.stores;

import com.lasrosas.iot.ingestor.domain.message.EventMessage;

import java.util.Map;

public interface TimeSerieStoreCommand {
    void insertPoint(EventMessage event);
}
