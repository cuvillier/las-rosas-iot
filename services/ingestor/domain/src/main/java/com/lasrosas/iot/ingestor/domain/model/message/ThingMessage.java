package com.lasrosas.iot.ingestor.domain.model.message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public abstract class ThingMessage implements MessageOrigin {
    private String thingNaturalid;
    private LocalDateTime time;
    private String correlationId;
    private String sensor;

    public void setOrigin(MessageOrigin origin) {
        this.thingNaturalid = origin.getThingNaturalid();
        this.time = origin.getTime();
        this.correlationId = origin.getCorrelationId();
    }

    /**
     * Default schema name in the timeserie database.
     * May be redefined in a sub class.
     * @return the schema
     */
    public String schema() {
        return getClass().getSimpleName();
    }
}
