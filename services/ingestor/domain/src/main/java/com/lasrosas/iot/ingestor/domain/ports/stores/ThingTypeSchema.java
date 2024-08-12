package com.lasrosas.iot.ingestor.domain.ports.stores;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ThingTypeSchema {
    private String className;
    private List<ThingTypeField> fields;

    @Getter
    @Setter
    public static class ThingTypeField {
        private String name;
        private String unitOfMeasurement;
    }
}
