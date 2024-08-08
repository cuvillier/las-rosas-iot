package com.lasrosas.iot.ingestor.domain.ports.stores;

import com.lasrosas.iot.ingestor.domain.message.EventMessage;
import com.lasrosas.iot.ingestor.domain.model.timeserie.TimeSeriePoint;

public interface TimeSerieStoreCommand {
    void insertPoint(EventMessage event);
}
