package com.lasrosas.iot.core.shared.utils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.LongSerializationPolicy;

import io.goodforgod.gson.configuration.GsonConfiguration;
import io.goodforgod.gson.configuration.GsonFactory;
import io.goodforgod.gson.configuration.deserializer.LocalDateTimeDeserializer;
import io.goodforgod.gson.configuration.serializer.LocalDateTimeSerializer;

@ContextConfiguration(classes=UtilsConfig.class)
public class GsonUtils {
	public static final Logger log = LoggerFactory.getLogger(GsonUtils.class);

	private static GsonBuilder gsonBuilder = null;

	public static Gson gson() {
		return gsonBuilder().create();
	}

	private static boolean debug = true;

	public synchronized static GsonBuilder gsonBuilder() {

		if(gsonBuilder == null) {

			var configuration = GsonConfiguration.ofJavaISO()
		        .setSerializeNulls(false)
		        .setLenient(true)
		        .setEscapeHtmlChars(false)
		        .setPrettyPrinting(true)
		        .setLongSerializationPolicy(LongSerializationPolicy.STRING)
		        .setComplexMapKeySerialization(true)
		        .setGenerateNonExecutableJson(true);

			if(debug)
				configuration.setPrettyPrinting(true);

			gsonBuilder = configuration.builder();
			gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
			gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
		}

		return gsonBuilder;
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
