package com.lasrosas.iot.ingestor.adapters.persisters.thing;

import com.lasrosas.iot.ingestor.adapters.persisters.PersisterTest;
import com.lasrosas.iot.ingestor.adapters.persisters.thing.mappers.ThingEntityMapper;
import com.lasrosas.iot.ingestor.domain.model.ThingSample;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ThingEntityMapperTest extends PersisterTest {

    @Autowired
    private ThingEntityMapper mapper;

    @Test
    public void map_to_entity_to_domain_should_return_the_same_data() {

        // Given an entity to/from domain mapper
        var expected = ThingSample.sample();

        // When an entity is mapped back and forth as a domain object
        var entity = mapper.toThingEntity(expected);
        var actual = mapper.toThing(entity);

        // Then the result must be identical to the initial object
        ThingSample.assertEquals(expected, actual);
    }
}
