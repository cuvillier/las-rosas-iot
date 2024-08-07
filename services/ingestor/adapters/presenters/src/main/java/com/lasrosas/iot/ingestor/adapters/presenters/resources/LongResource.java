package com.lasrosas.iot.ingestor.adapters.presenters.resources;

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
public abstract class LongResource {
    private Long techid;
}
