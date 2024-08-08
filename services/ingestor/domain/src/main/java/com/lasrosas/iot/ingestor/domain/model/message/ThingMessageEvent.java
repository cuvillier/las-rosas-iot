package com.lasrosas.iot.ingestor.domain.model.message;

import com.lasrosas.iot.ingestor.domain.model.thing.Thing;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ThingMessageEvent extends ApplicationEvent {
    private final BaseMessage message;

    public static ThingMessageEvent of(Thing thing, BaseMessage message) {
        return new ThingMessageEvent(thing, message);
    }

    public static ThingMessageEvent of(ThingMessageEvent origin, BaseMessage message) {
        message.setOrigin(origin.getMessage());
        return new ThingMessageEvent(origin.getThing(), message);
    }

    public ThingMessageEvent(Thing thing, BaseMessage message) {
        super(thing);
        this.message = message;
    }

    public Thing getThing() {
        return (Thing)getSource();
    }
}
