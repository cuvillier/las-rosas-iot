package com.lasrosas.iot.ingestor.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@SuperBuilder
@Slf4j
@NoArgsConstructor
public abstract class LongDomain {
    private Long techid;
}
