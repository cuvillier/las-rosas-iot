package com.lasrosas.iot.ingeator.adapters.presenters.resources;

import com.lasrosas.iot.ingestor.domain.model.ThingSample;
import com.lasrosas.iot.ingestor.adapters.presenters.resources.ThingResourceMapper;
import org.junit.jupiter.api.Test;

public class ThingResourceMapperTest {
    @Test
    public void mapTwiceReturnOriginalObject() {

        // Given an entity to/from domain mapper
        var expected = ThingSample.sample();

        // When an entity is mapped back and forth as a domain object
        var mapper = ThingResourceMapper.MAPPER;
        var mapped = mapper.toThingResource(expected);
        var actual =  mapper.toThing(mapped);

        // Then the result must be identical to the initial object
        ThingSample.assertEquals(expected, actual);
    }
}
