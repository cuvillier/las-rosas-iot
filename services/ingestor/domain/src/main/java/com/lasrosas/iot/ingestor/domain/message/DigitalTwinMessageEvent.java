package com.lasrosas.iot.ingestor.domain.message;

import com.lasrosas.iot.ingestor.domain.model.digitalTwin.DigitalTwin;
import com.lasrosas.iot.ingestor.domain.model.thing.Thing;
import lombok.Getter;
import org.springframework.context.PayloadApplicationEvent;

@Getter
public class DigitalTwinMessageEvent extends PayloadApplicationEvent<BaseMessage> {

    private record Source(Thing thing, DigitalTwin digitalTwin) {}

    public static DigitalTwinMessageEvent of(DigitalTwin digitalTwin, Thing thing, BaseMessage payload) {
        return new DigitalTwinMessageEvent(digitalTwin, thing, payload);
    }

    public static DigitalTwinMessageEvent of(DigitalTwin digitalTwin, BaseMessage payload) {
        return new DigitalTwinMessageEvent(digitalTwin, null, payload);
    }

    public static DigitalTwinMessageEvent of(DigitalTwinMessageEvent origin, BaseMessage payload) {
        payload.setOrigin(origin.getPayload());
        return DigitalTwinMessageEvent.of(origin.getDigitalTwin(), payload);
    }

    public DigitalTwinMessageEvent(DigitalTwin digitalTwin, Thing thing, BaseMessage payload) {
        super(new Source(thing, digitalTwin), payload);
    }

    public Thing getThing() {
        return ((Source)getSource()).thing();
    }

    public DigitalTwin  getDigitalTwin() {
        return ((Source)getSource()).digitalTwin();
    }
}
