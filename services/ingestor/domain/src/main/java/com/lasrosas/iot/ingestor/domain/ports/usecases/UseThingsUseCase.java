package com.lasrosas.iot.ingestor.domain.ports.usecases;

import com.lasrosas.iot.ingestor.domain.model.thing.Thing;
import java.util.List;
import java.util.Optional;

public interface UseThingsUseCase {
    Optional<Thing> findThingByNaturalid(String naturalid);
    List<Thing> getThings();
}
