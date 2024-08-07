package com.lasrosas.iot.ingestor.shared;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lasrosas.iot.ingestor.shared.exceptions.InvalidJsonFormatException;

public class JsonUtils {
    public static ObjectMapper mapper() {
        var mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }

    public static ObjectReader readerFor(Class<?> c) {
        return mapper().readerFor(c);
    }

    public static ObjectWriter writerFor(Class<?> c) {
        return mapper().writerFor(c);
    }

    public static String toJson(Object object) {
        return toJson(object, false);
    }

    public static String toJson(Object object, boolean pretty) {
        var writer = writerFor(object.getClass());
        if(pretty) writer = writer.withDefaultPrettyPrinter();
        try {
            return writer.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new InvalidJsonFormatException("Cannot write to JSON", e);
        }
    }

    public static <T> T fromJson(Class<T> c, String json) {
        try {
            return readerFor(c).readValue(json);
        } catch (JsonProcessingException e) {
            throw new InvalidJsonFormatException("Cannot write to JSON", e);
        }
    }
}
