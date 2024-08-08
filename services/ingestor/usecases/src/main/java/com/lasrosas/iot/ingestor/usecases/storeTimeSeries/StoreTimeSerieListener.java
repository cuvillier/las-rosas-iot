package com.lasrosas.iot.ingestor.usecases.storeTimeSeries;

import com.lasrosas.iot.ingestor.domain.message.EventMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StoreTimeSerieListener {

    @EventListener
    public void onApplicationEvent(EventMessage event) {
        log.info("PERSIST MESSAGE " + event.getMessage().getSchema());
    }
}
