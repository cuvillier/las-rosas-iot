package com.lasrosas.iot.ingestor.domain.model.message;

import java.time.LocalDateTime;

public interface MessageOrigin {
    String getCorrelationId();
    void setCorrelationId(String correlationId);
    LocalDateTime getTime();
    void setTime(LocalDateTime time);
    default String getSensor() { return null; };
    default void setSensor(String sensor) { };
}
