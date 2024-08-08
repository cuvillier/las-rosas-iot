package com.lasrosas.iot.ingestor.shared;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JsonUtilsTest {

    @Test
    public void mergeEmpty() {
        var mapper = new ObjectMapper();
        var json1 = mapper.createObjectNode();
        var json2 = mapper.createObjectNode();

        var changes =JsonUtils.mergeJsonObjects(json1, json2, null);

        assertEquals(0, changes);
        assertEquals(0, json2.size());
    }

    @Test
    public void mergeNew() {
        var mapper = new ObjectMapper();
        var jsonFrom = mapper.createObjectNode();
        var jsonTo = mapper.createObjectNode();

        jsonFrom.put("int", 1);
        jsonFrom.put("string", "s");

        var changes = JsonUtils.mergeJsonObjects(jsonFrom, jsonTo, null);
        assertEquals(2, changes);

        assertEquals(2, jsonTo.size());
        assertEquals(1, jsonTo.get("int").asInt());
        assertEquals("s", jsonTo.get("string").asText());

        assertEquals(2, jsonTo.size());
    }

    @Test
    public void mergeUpdate() {
        var mapper = new ObjectMapper();
        var json1 = mapper.createObjectNode();
        var json2 = mapper.createObjectNode();

        json1.put("int", 1);
        json1.put("string", "s");

        json2.put("int", 1212);
        json2.put("string", "sasdsd");

        var changes = JsonUtils.mergeJsonObjects(json1, json2, null);

        assertEquals(2, changes);

        assertEquals(2, json2.size());
        assertEquals(1212, json2.get("int").asInt());
        assertEquals("sasdsd", json2.get("string").asText());

        assertEquals(2, json2.size());
    }

    @Test
    public void mergeUpdateWithTime() {
        var mapper = new ObjectMapper();
        var json1 = mapper.createObjectNode();
        var json2 = mapper.createObjectNode();

        json1.put("int", 1);
        json1.put("string", "s");

        json2.put("int", 1212);
        json2.put("string", "sasdsd");

        var time = LocalDateTime.now();
        var changes = JsonUtils.mergeJsonObjects(json1, json2, time);

        System.out.println(JsonUtils.toJson(json2));

        assertEquals(4, changes);

        assertEquals(4, json2.size());
        assertEquals(1212, json2.get("int").asInt());
        assertEquals("sasdsd", json2.get("string").asText());
        assertEquals(time.toString(), json2.get("int-time").asText());
        assertEquals(time.toString(), json2.get("string-time").asText());
    }

    @Test
    public void mergeSubjson() {
        var mapper = new ObjectMapper();
        var json1 = mapper.createObjectNode();
        var json2 = mapper.createObjectNode();

        json1.put("int", 1);
        json1.put("string", "s");

        var sub = mapper.createObjectNode();
        sub.put("name", "sub");

        json1.put("sub", sub);

        json2.put("int", 1212);
        json2.put("string", "sasdsd");

        var changes = JsonUtils.mergeJsonObjects(json1, json2, null);
        assertEquals(3, changes);

        assertEquals(3, json2.size());
        assertEquals(1212, json2.get("int").asInt());
        assertEquals("sasdsd", json2.get("string").asText());
        assertNotNull(json2.get("sub"));
        assertEquals("sub", json2.get("sub").get("name").asText());
    }

    public static class WithLocalDateTime {
        private LocalDateTime time = LocalDateTime.now();
        private LocalDate date = LocalDate.now();

        public LocalDateTime getTime() {
            return time;
        }

        public void setTime(LocalDateTime time) {
            this.time = time;
        }
    }

    @Test
    public void convertDateTimeThrowException() {
        var o = new WithLocalDateTime();
        var json = JsonUtils.toJson(o);
        JsonUtils.fromJson(WithLocalDateTime.class, json);
    }
}
