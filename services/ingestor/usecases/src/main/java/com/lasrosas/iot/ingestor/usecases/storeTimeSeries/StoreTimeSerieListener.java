package com.lasrosas.iot.ingestor.usecases.storeTimeSeries;

import com.lasrosas.iot.ingestor.domain.message.BaseMessage;
import com.lasrosas.iot.ingestor.domain.message.DigitalTwinMessageEvent;
import com.lasrosas.iot.ingestor.domain.message.ThingMessageEvent;
import com.lasrosas.iot.ingestor.domain.model.digitalTwin.DigitalTwin;
import com.lasrosas.iot.ingestor.domain.model.thing.Thing;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StoreTimeSerieListener {

    @EventListener
    public void onApplicationEvent(ThingMessageEvent event) {
        store(event.getThing(), null, event.getPayload());
    }

    @EventListener
    public void onApplicationEvent(DigitalTwinMessageEvent event) {
        store(event.getThing(), event.getDigitalTwin(), event.getPayload());
    }

    public void store(Thing thing, DigitalTwin digitalTwin, BaseMessage message) {
        log.info("PERSIST MESSAGE " + message.getSchema());
    }
}
