package com.lasrosas.iot.ingestor.usecases.useThings;

import com.lasrosas.iot.ingestor.domain.model.thing.Thing;
import com.lasrosas.iot.ingestor.domain.ports.stores.ThingStoreQuery;
import com.lasrosas.iot.ingestor.domain.ports.usecases.UseThingsUseCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ThingService implements UseThingsUseCase {

    private ThingStoreQuery thingStoreQuery;

    @Override
    public Optional<Thing> findThingByNaturalid(String deveui) {
        return  thingStoreQuery.getThingByNaturalid(deveui);
    }

    @Override
    public List<Thing> getThings() {
        return thingStoreQuery.getThings();
    }
}
