package com.lasrosas.iot.ingestor.usecases.handleLorawanMessages;

import com.lasrosas.iot.ingestor.domain.message.GatewayPayloadMessage;
import com.lasrosas.iot.ingestor.domain.message.LorawanRadioMessage;
import com.lasrosas.iot.ingestor.domain.message.GatewayPayloadMessageEvent;
import com.lasrosas.iot.ingestor.domain.message.EventMessage;
import com.lasrosas.iot.ingestor.domain.message.BaseMessage;
import com.lasrosas.iot.ingestor.domain.model.thing.Thing;
import com.lasrosas.iot.ingestor.domain.model.thing.ThingGateway;
import com.lasrosas.iot.ingestor.domain.ports.stores.ThingStoreQuery;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.gatewayDrivers.LorawanGatewayDriverManager;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.thingDrivers.ThingDriverManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class LorawanMessageListener implements ApplicationListener<GatewayPayloadMessageEvent> {

    private LorawanGatewayDriverManager gatewayDriverManager;
    private ThingDriverManager thingDriverManager;
    private ThingStoreQuery thingStore;
    ApplicationEventPublisher publisher;

    @Override
    public void onApplicationEvent(GatewayPayloadMessageEvent event) {
        var message = event.getMessage();

        // Decode the LoraGateway message
        var gateway = thingStore.getGatewayByNaturalId(message.getGatewayNaturalId()).orElseThrow();
        var lorawanMessage = decodeLorawanMessage(gateway, message);

        // Check if the thing is registered in the database
        var thing = thingStore.getThingByNaturalid(lorawanMessage.getDevEUI()).orElseThrow();
        if( !thing.getGateway().getType().equals("LORAWAN"))
            throw new RuntimeException(String.format("Gateway %s type is not LORAWAN", thing.getGateway().getNaturalid()));

        lorawanMessage.setThingNaturalid(thing.getNaturalid());

        if( lorawanMessage instanceof LorawanMessageUplinkRx uplink) {

            // Publish the LORAWAN radio indicators like RSSI or SNR
            var lorawanRadio = decodeLorawanRadio(uplink);
            publisher.publishEvent(EventMessage.of(thing, lorawanRadio));

            // Publish the sensor data
            var thingMessages = decodeThingMessage(thing, uplink);
            for (var thingMessage : thingMessages)
                publisher.publishEvent(EventMessage.of(thing, thingMessage));

        } else
            log.info("Lorawan message of type " + lorawanMessage.getClass().getSimpleName() + " ignored.");

    }


    private LorawanMessage decodeLorawanMessage(ThingGateway gateway, GatewayPayloadMessage message) {
        var driver = gatewayDriverManager.get(gateway.getDriver());
        return driver.decodeJson(message);
    }

    private LorawanRadioMessage decodeLorawanRadio(LorawanMessageUplinkRx message) {
        var result = LorawanRadioMessage.builder()
                .fCnt(message.getFCnt())
                .fPort(message.getFPort())
                .time(message.getTime())
                .correlationId((message.getCorrelationId()))
                .build();

        if(!message.getRxInfo().isEmpty()) {
            var rxInfo = message.getRxInfo().getFirst();
            result.setRssi(rxInfo.getRssi());
            result.setLoRaSNR(rxInfo.getLoRaSNR());
        }

        if(message.getTxInfo() != null) {
            result.setDr(message.getTxInfo().getDr());
            result.setFrequency(message.getTxInfo().getFrequency());
        }

        result.setOrigin(message);

        return result;
    }

    private List<BaseMessage> decodeThingMessage(Thing thing, LorawanMessageUplinkRx lorawanMessage) {
        var driver = thingDriverManager.get(thing.getType().getManufacturer(), thing.getType().getModel());

        var decoded = driver.decodeUplink(lorawanMessage);
        decoded.setOrigin(lorawanMessage);

        var normalized = driver.normalize(decoded);
        normalized.forEach(m -> m.setOrigin(lorawanMessage));

        return normalized;
    }
}
