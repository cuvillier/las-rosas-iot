package com.lasrosas.iot.ingestor.usecases.storeTimeSeries;

import com.lasrosas.iot.ingestor.domain.message.EventMessage;
import com.lasrosas.iot.ingestor.domain.ports.stores.TimeSerieStore;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Slf4j
@AllArgsConstructor
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StoreTimeSerieToJpaListener implements ApplicationListener<EventMessage>, Ordered {

    // Use Autowired because of the Qualifier annotation with lombock
    @Autowired
    private TimeSerieStore jpaTimeSerieStore;

    @Transactional
    @Override
    public void onApplicationEvent(EventMessage event) {
        log.info(String.format("Save message %s to JPA", event.getMessage().getSchema()));
        jpaTimeSerieStore.insertPoint(event);
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
