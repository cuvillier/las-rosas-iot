package com.lasrosas.iot.ingestor.adapters.persisters.jpa.twins;

import com.lasrosas.iot.ingestor.adapters.persisters.jpa.PersisterTest;
import com.lasrosas.iot.ingestor.adapters.persisters.jpa.digitalTwin.mappers.DigitalSpaceEntityMapper;
import com.lasrosas.iot.ingestor.domain.model.DigitalSpaceSample;
import com.lasrosas.iot.ingestor.domain.model.ThingSample;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DigitalSpaceEntityMapperTest extends PersisterTest {

    @Autowired
    private DigitalSpaceEntityMapper mapper;

    @Test
    public void map_to_entity_to_domain_should_return_the_same_data() {

        // Given an entity to/from domain mapper
        var expected = DigitalSpaceSample.sample();

        // When an entity is mapped back and forth as a domain object
        var entity = mapper.toDigitalSpaceEntity(expected);
        var actual = mapper.toDigitalSpace(entity);

        // Then the result must be identical to the initial object
        DigitalSpaceSample.assertEquals(expected, actual);
    }
}
