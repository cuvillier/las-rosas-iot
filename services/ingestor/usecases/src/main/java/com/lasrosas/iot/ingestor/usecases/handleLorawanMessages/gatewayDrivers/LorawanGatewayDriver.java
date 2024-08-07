package com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.gatewayDrivers;

import com.lasrosas.iot.ingestor.domain.model.message.GatewayPayloadMessage;
import com.lasrosas.iot.ingestor.usecases.handleLorawanMessages.LorawanMessage;

public interface LorawanGatewayDriver {
    String getName();
    LorawanMessage decodeJson(GatewayPayloadMessage message);
}
