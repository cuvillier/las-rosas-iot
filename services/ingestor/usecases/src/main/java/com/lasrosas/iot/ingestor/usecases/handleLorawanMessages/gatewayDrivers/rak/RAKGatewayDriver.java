package com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.gatewayDrivers.rak;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lasrosas.iot.ingestor.domain.message.GatewayPayloadMessage;
import com.lasrosas.iot.ingestor.shared.JsonUtils;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.LorawanMessage;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.LorawanMessageAck;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.LorawanMessageJoin;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.LorawanMessageUplinkRx;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.gatewayDrivers.LorawanGatewayDriver;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class RAKGatewayDriver implements LorawanGatewayDriver {

    public String getName() {
        return "RAK";
    }

    @Override
    public LorawanMessage decodeJson(GatewayPayloadMessage message) {
        var jsonMapper = JsonUtils.mapper();

        LorawanMessage lorawanMessage;
        try {
            if (message.getTopic().endsWith("/join"))
                lorawanMessage = jsonMapper.readerFor(LorawanMessageJoin.class).readValue(message.getJson());

            else if (message.getTopic().endsWith("/rx"))
                lorawanMessage = jsonMapper.readerFor(LorawanMessageUplinkRx.class).readValue(message.getJson());

            else if (message.getTopic().endsWith("/ack"))
                lorawanMessage = jsonMapper.readerFor(LorawanMessageAck.class).readValue(message.getJson());

            else
                throw new RuntimeException("Invalid topic, message ignored");

            lorawanMessage.setCorrelationId(message.getCorrelationId());
            lorawanMessage.setTime(message.getTime());

            return lorawanMessage;

        } catch(JsonProcessingException e) {
            throw new RuntimeException("Cannot decode message json", e);
        }
    }
}
