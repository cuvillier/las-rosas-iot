package com.lasrosas.iot.ingestor.usecases.integrateHomeAssistant;

import com.lasrosas.iot.ingestor.domain.message.ThingConfigMessage;
import com.lasrosas.iot.ingestor.domain.message.ThingConfigMessageEvent;
import com.lasrosas.iot.ingestor.domain.message.ThingEventMessage;
import com.lasrosas.iot.ingestor.domain.ports.stores.ThingStoreQuery;
import com.lasrosas.iot.ingestor.domain.ports.stores.TimeSerieStoreQuery;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;

public class IntegrateHomeAssistantImpl implements ApplicationListener<ThingEventMessage> {
    private ThingStoreQuery thingStore;
    private TimeSerieStoreQuery timeSerieStore;
    private ApplicationEventPublisher publisher;

    public void sendConfiguration() {
        /*
        var types = thingStore.getThingTypes();
        for(var type: types) {
            var payloadSchemas = timeSerieStore.getPayloadSchemasForThingType(type);

            var things = thingStore.getThingsByType(type);
            for(var thing: things) {
                if (thing.isDiscoverable()) {
                    for(var payloadSchema: payloadSchemas) {
                        ThingConfigMessage configurationMessage;
                        publisher.publishEvent(new ThingConfigMessageEvent(thing, configurationMessage));
                    }
                }
            }
        }

         */
    }

    @Override
    public void onApplicationEvent(ThingEventMessage event) {

    }
}
