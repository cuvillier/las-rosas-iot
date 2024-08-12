package com.lasrosas.iot.ingestor.domain.ports.stores;

import com.lasrosas.iot.ingestor.domain.model.thing.Thing;
import com.lasrosas.iot.ingestor.domain.model.thing.ThingGateway;
import com.lasrosas.iot.ingestor.domain.model.thing.ThingType;

import java.util.List;
import java.util.Optional;

public interface ThingStoreQuery {
    Optional<Thing> getThingByNaturalid(String naturalid);
    Optional<ThingType> getThingTypeByManufacturerAndModel(String manufacturer, String model);
    Optional<ThingGateway> getGatewayByNaturalId(String naturalId);
    List<Thing> getThings();

    List<ThingType> getThingTypes();

    List<Thing> getThingsByType(ThingType type);
    List<ThingTypeSchema> getPayloadSchemasForThingType(ThingType type);
}
