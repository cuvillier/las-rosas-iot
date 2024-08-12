package com.lasrosas.iot.ingestor.domain.message;

import com.lasrosas.iot.ingestor.domain.model.digitalTwin.DigitalTwin;
import com.lasrosas.iot.ingestor.domain.model.thing.Thing;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ThingEventMessage extends ApplicationEvent {
    private BaseMessage message;

    private record Source(Thing thing, DigitalTwin digitalTwin) {}

    public static ThingEventMessage of(Thing thing, BaseMessage payload) {
        return new ThingEventMessage(null, thing, payload);
    }

    public static ThingEventMessage of(DigitalTwin digitalTwin, Thing thing, BaseMessage payload) {
        return new ThingEventMessage(digitalTwin, thing, payload);
    }

    public static ThingEventMessage of(DigitalTwin digitalTwin, BaseMessage payload) {
        return new ThingEventMessage(digitalTwin, null, payload);
    }

    public static ThingEventMessage of(ThingEventMessage origin, BaseMessage payload) {
        payload.setOrigin(origin.getMessage());
        return ThingEventMessage.of(origin.getDigitalTwin(), payload);
    }

    public ThingEventMessage(DigitalTwin digitalTwin, Thing thing, BaseMessage message) {
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

    public String getMeasurement() {
        var twin = this.getDigitalTwin();
        var thing = this.getThing();

        var schema = this.message.getSchemaClass().getSimpleName();

        String naturalId = "THG_" + thing.getNaturalid();
        String sensor = this.message.getSensor();

        if (sensor == null)
            return (naturalId + "_" + schema).replaceAll("\\.", "_");
        else
            return (naturalId + "_" + sensor + "_" + schema).replaceAll("\\.", "_");
    }
/*
    public <X extends BaseMessage> X getAs(Class<X> c) {

        //noinspection unchecked
        return (X)getPayload();
    }
*/
}
