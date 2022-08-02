package com.lasrosas.iot.core.shared.utils;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

public class GsonUtils {

	public static Gson createGson() {
		return new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() { 
			@Override 
			public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException { 
				return ZonedDateTime.parse(json.getAsJsonPrimitive().getAsString()).toLocalDateTime();
			}}).create();
	}

	public static int mergeJsonObjects(JsonObject fromjson, JsonObject tojson, LocalDateTime time) {
		var fromEntrySetCopy = new ArrayList<Map.Entry<String, JsonElement>>(fromjson.entrySet());
		int changes = 0;
		
		String stime = time == null? null: time.toString();

		for(var fromEntry: fromEntrySetCopy) {
			var fromValue = fromEntry.getValue();
			var fromKey = fromEntry.getKey();

			if( fromValue.isJsonObject() ) {
				var subjson = new JsonObject();
				changes += mergeJsonObjects((JsonObject)fromValue, subjson, time);
				tojson.add(fromKey, subjson);
				if(time != null) {
					tojson.add(fromKey + "-time", new JsonPrimitive(stime));
					changes++;
				}

			} else if( fromValue.isJsonPrimitive() ) {

				var toValue = tojson.get(fromKey) == null? null: tojson.get(fromKey).getAsJsonPrimitive();

				if( toValue == null || !fromValue.equals(toValue) ) {
					changes++;
					tojson.add(fromKey, fromValue);
				}

				if(stime != null) {
					tojson.add(fromKey + "-time", new JsonPrimitive(stime));
					changes++;
				}
			} else
				throw new RuntimeException("JsonElement type not supported key="+ fromKey);
		}

		return changes;
	}

}
