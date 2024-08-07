package com.lasrosas.iot.ingestor.adapters.persisters.thing;

import com.lasrosas.iot.ingestor.adapters.persisters.thing.entities.ThingEntity;
import com.lasrosas.iot.ingestor.adapters.persisters.thing.mappers.ThingEntityMapper;
import com.lasrosas.iot.ingestor.adapters.persisters.thing.mappers.ThingGatewayEntityMapper;
import com.lasrosas.iot.ingestor.adapters.persisters.thing.mappers.ThingTypeEntityMapper;
import com.lasrosas.iot.ingestor.adapters.persisters.thing.repositories.ThingEntityRepository;
import com.lasrosas.iot.ingestor.adapters.persisters.thing.repositories.ThingGatewayEntityRepository;
import com.lasrosas.iot.ingestor.adapters.persisters.thing.repositories.ThingProxyEntityRepository;
import com.lasrosas.iot.ingestor.adapters.persisters.thing.repositories.ThingTypeEntityRepository;
import com.lasrosas.iot.ingestor.domain.model.thing.Thing;
import com.lasrosas.iot.ingestor.domain.model.thing.ThingGateway;
import com.lasrosas.iot.ingestor.domain.model.thing.ThingType;
import com.lasrosas.iot.ingestor.domain.ports.stores.ThingStore;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
@Transactional
public class ThingPersister implements ThingStore {

    private ThingEntityRepository thingRepo;
    private ThingGatewayEntityRepository thingGatewayRepo;
    private ThingTypeEntityRepository thingTypeRepo;
    private ThingProxyEntityRepository thingProxyRepo;
    private ThingEntityMapper thingMapper;
    private ThingTypeEntityMapper typeMapper;
    private ThingGatewayEntityMapper gatewayMapper;

    @Override
    public Optional<Thing> getThingByNaturalid(String naturalid) {
        var optionalEntity = thingRepo.findByNaturalid(naturalid);
        return optionalEntity.map(ThingEntity -> thingMapper.toThing(ThingEntity));
    }

    @Override
    public Optional<ThingType> getThingTypeByManufacturerAndModel(String manufacturer, String model) {
        var optionalEntity = thingTypeRepo.findByManufacturerAndModel(manufacturer, model);
        return optionalEntity.map(entity -> typeMapper.toThingType(entity));
    }

    @Override
    public Optional<ThingGateway> getGatewayByNaturalId(String naturalId) {
        var optionalEntity = thingGatewayRepo.findByNaturalid(naturalId);
        return optionalEntity.map(entity -> gatewayMapper.toThingGateway(entity));
    }

    @Override
    public List<Thing> getThings() {
        return thingRepo.findAll().stream().map(entity -> thingMapper.toThing(entity)).toList();
    }

    @Override
    public void saveThing(Thing domain) {

        // Update existing entity
        var entityOpt = thingRepo.findByNaturalid(domain.getNaturalid());

        ThingEntity entity;
        if(entityOpt.isPresent()) {

            // Update existing entity, not the linked entities
            entity = entityOpt.get();
            thingMapper.update(domain, entity);

        } else {

            // New entity
            entity = thingMapper.toShallowThingEntity(domain);

            // Link
            var gateway = thingGatewayRepo.findByNaturalid(domain.getGateway().getNaturalid());
            var type  = thingTypeRepo.findByManufacturerAndModel(domain.getType().getManufacturer(), domain.getType().getModel());

            entity.setGateway(gateway.orElseThrow());
            entity.setType(type.orElseThrow());
        }

        thingRepo.save(entity);
    }
}
