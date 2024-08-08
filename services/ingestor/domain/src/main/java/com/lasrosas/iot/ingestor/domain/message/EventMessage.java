package com.lasrosas.iot.ingestor.domain.message;

import com.lasrosas.iot.ingestor.domain.model.digitalTwin.DigitalTwin;
import com.lasrosas.iot.ingestor.domain.model.thing.Thing;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EventMessage extends ApplicationEvent {
    private BaseMessage message;

    private record Source(Thing thing, DigitalTwin digitalTwin) {}

    public static EventMessage of(Thing thing, BaseMessage payload) {
        return new EventMessage(null, thing, payload);
    }

    public static EventMessage of(DigitalTwin digitalTwin, Thing thing, BaseMessage payload) {
        return new EventMessage(digitalTwin, thing, payload);
    }

    public static EventMessage of(DigitalTwin digitalTwin, BaseMessage payload) {
        return new EventMessage(digitalTwin, null, payload);
    }

    public static EventMessage of(EventMessage origin, BaseMessage payload) {
        payload.setOrigin(origin.getMessage());
        return EventMessage.of(origin.getDigitalTwin(), payload);
    }

    public EventMessage(DigitalTwin digitalTwin, Thing thing, BaseMessage message) {
        super(new Source(thing, digitalTwin));
        this.message = message;
    }

    public Thing getThing() {
        return ((Source)getSource()).thing();
    }

    public DigitalTwin  getDigitalTwin() {
        return ((Source)getSource()).digitalTwin();
    }

    public Long getDigitalTwinId() {
        var dtwin = getDigitalTwin();
        return dtwin == null ? null: dtwin.getTechid();
    }

    public Long getThingId() {
        var thing = getThing();
        return thing == null ? null: thing.getTechid();
    }
/*
    public <X extends BaseMessage> X getAs(Class<X> c) {

        //noinspection unchecked
        return (X)getPayload();
    }
*/
}
