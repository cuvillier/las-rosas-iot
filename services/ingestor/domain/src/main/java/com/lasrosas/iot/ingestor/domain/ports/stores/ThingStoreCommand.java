package com.lasrosas.iot.ingestor.domain.ports.stores;

import com.lasrosas.iot.ingestor.domain.model.thing.Thing;

public interface ThingStoreCommand {
    void saveThing(Thing thing);
}
