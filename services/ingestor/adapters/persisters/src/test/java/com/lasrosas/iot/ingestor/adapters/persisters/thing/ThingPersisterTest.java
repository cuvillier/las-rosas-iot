package com.lasrosas.iot.ingestor.adapters.persisters.thing;

import com.lasrosas.iot.ingestor.adapters.persisters.PersisterTest;
import com.lasrosas.iot.ingestor.domain.model.ThingGatewaySample;
import com.lasrosas.iot.ingestor.domain.model.ThingSample;
import com.lasrosas.iot.ingestor.domain.model.ThingTypeSample;
import com.lasrosas.iot.ingestor.domain.ports.stores.ThingStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ThingPersisterTest extends PersisterTest {

    @Autowired
    private ThingStore thingStore;

    @Test
    @Transactional
    public void findThingByNaturalid() {

        // Given
        var sample = ThingSample.Test_Elsys_MB7389;

        // When
        var actual = thingStore.getThingByNaturalid(sample.getNaturalid()).orElseThrow();

        // Then
        ThingSample.assertEquals(ThingSample.Test_Elsys_MB7389, actual);
    }

    @Test
    @Transactional
    public void getThings() {

        // Given
        var numberOfThingsInTheTestDatabase = 6;

        // When
        var things = thingStore.getThings();

        // Then
        assertEquals(numberOfThingsInTheTestDatabase, things.size());
    }

    @Test
    @Transactional
    public void updateThing() {

        // Given
        var gateway = thingStore.getGatewayByNaturalId(ThingGatewaySample.TEST_GATEWAY.getNaturalid());
        var type = thingStore.getThingTypeByManufacturerAndModel(ThingTypeSample.Adeunis_ARF8180BA.getManufacturer(), ThingTypeSample.Adeunis_ARF8180BA.getModel());
        var expected = ThingSample.sample(gateway.orElseThrow(), type.orElseThrow());

        // When
        thingStore.saveThing(expected);

        // Then
        var actual = thingStore.getThingByNaturalid(expected.getNaturalid()).orElseThrow();
        ThingSample.assertEquals(expected, actual);
    }
}
