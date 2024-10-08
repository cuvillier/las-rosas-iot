package com.lasrosas.iot.ingestor.domain.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.beans.Transient;
import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public abstract class BaseMessage implements MessageOrigin {

    private String schema;

    private LocalDateTime time;

    private String correlationId;

    private String sensor;

    public void setOrigin(MessageOrigin origin) {
        this.time = origin.getTime();
        this.correlationId = origin.getCorrelationId();
    }

    /**
     * Default schema name in the timeserie database.
     * May be redefined in a sub class.
     * @return the schema
     */
    public String getSchema() {
        return getClass().getName();
    }

    // Do not persist in Json
    @Transient
    public Class<?> getSchemaClass() {
        return getClass();
    }
}
