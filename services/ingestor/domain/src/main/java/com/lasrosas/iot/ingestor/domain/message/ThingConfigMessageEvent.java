package com.lasrosas.iot.ingestor.domain.message;

import com.lasrosas.iot.ingestor.domain.model.thing.Thing;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class ThingConfigMessageEvent extends ApplicationEvent {
    private final ThingConfigMessage message;
    private Thing thing;
    private List<String> schemas;

    public ThingConfigMessageEvent(Thing thing, ThingConfigMessage message) {
        super(thing);
        this.message = message;
    }

    public Thing getThing() {
        return (Thing)getSource();
    }
}
