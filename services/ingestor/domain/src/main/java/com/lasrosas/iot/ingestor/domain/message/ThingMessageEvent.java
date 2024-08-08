package com.lasrosas.iot.ingestor.domain.message;

import com.lasrosas.iot.ingestor.domain.model.thing.Thing;
import lombok.Getter;
import org.springframework.context.PayloadApplicationEvent;

@Getter
public class ThingMessageEvent extends PayloadApplicationEvent<BaseMessage> {

    public static ThingMessageEvent of(Thing thing, BaseMessage payload) {
        return new ThingMessageEvent(thing, payload);
    }

    public static ThingMessageEvent of(ThingMessageEvent origin, BaseMessage payload) {
        payload.setOrigin(origin.getPayload());
        return new ThingMessageEvent(origin.getThing(), payload);
    }

    public ThingMessageEvent(Thing thing, BaseMessage payload) {
        super(thing, payload);
    }

    public Thing getThing() {
        return (Thing)getSource();
    }
}
