package com.lasrosas.iot.shared.utils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class GsonUtils {

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
