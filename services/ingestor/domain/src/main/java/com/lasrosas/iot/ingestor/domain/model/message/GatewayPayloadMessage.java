package com.lasrosas.iot.ingestor.domain.model.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lasrosas.iot.ingestor.shared.JsonUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Setter
@Getter
@SuperBuilder
public class GatewayPayloadMessage {
    private String correlationId;
    private LocalDateTime time;
    private String gatewayNaturalId;
    private String topic;
    private String json;

    public <T> T getContentAsJson(Class<T> jsonClass) throws JsonProcessingException {
        var jsonReader = JsonUtils.readerFor(jsonClass);
        return jsonReader.readValue(this.json);
    }

    public void setContentFromJson(Object payloadObject) throws JsonProcessingException {
        var jsonWriter = JsonUtils.writerFor(payloadObject.getClass());
        this.json = jsonWriter.writeValueAsString(payloadObject);
    }
}
