package com.lasrosas.iot.ingestor.shared;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lasrosas.iot.ingestor.shared.exceptions.InvalidJsonFormatException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

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

    public static ObjectNode objectNode() {
        return mapper().createObjectNode();
    }

    public static ObjectNode toObjectNode(String json) {
        try {
            return (ObjectNode)mapper().readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static ObjectNode toObjectNode(Object object) {
        return (ObjectNode)mapper().convertValue(object, ObjectNode.class);
    }

    public static int mergeJsonObjects(ObjectNode fromjson, ObjectNode tojson, LocalDateTime time) {
        int changes = 0;

        String stime = time == null? null: time.toString();

        var fromFields = fromjson.fields();
        while(fromFields.hasNext()) {
            var fromField = fromFields.next();
            var fromValue = fromField.getValue();
            var fromKey = fromField.getKey();

            if( fromValue.isObject() ) {
                var subjson = JsonUtils.objectNode();
                changes += mergeJsonObjects((ObjectNode)fromValue, subjson, time);
                tojson.putIfAbsent(fromKey, subjson);

                if(time != null) {
                    tojson.put(fromKey + "-time", stime);
                    changes++;
                }

            } else {

                var toValue = tojson.get(fromKey);

                if( !fromValue.equals(toValue) ) {
                    changes++;
                    tojson.putIfAbsent(fromKey, fromValue);
                }

                if(stime != null) {
                    tojson.put(fromKey + "-time", stime);
                    changes++;
                }
            }
        }

        return changes;
    }
}
