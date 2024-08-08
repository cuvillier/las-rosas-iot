package com.lasrosas.iot.ingestor.usecases.storeTimeSeries;

import com.lasrosas.iot.ingestor.domain.message.EventMessage;
import com.lasrosas.iot.ingestor.domain.ports.stores.TimeSerieStore;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Slf4j
@AllArgsConstructor
public class StoreTimeSerieListener implements ApplicationListener<EventMessage>, Ordered {
    private TimeSerieStore store;

    @Transactional
    @Override
    public void onApplicationEvent(EventMessage event) {
        log.info(String.format("Save message %s to the database", event.getMessage().getSchema()));
        store.insertPoint(event);
    }

    /**
     * Control the listener order. The persistence must be the first luistener executed
     * @return
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
