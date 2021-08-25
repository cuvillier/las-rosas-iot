package com.lasrosas.iot.shared.telemetry;

import java.io.IOException;
import java.time.LocalDateTime;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public abstract class Telemetry {

	public class RawJsonGsonAdapter extends TypeAdapter<String> {

		@Override
		public void write(final JsonWriter out, final String value) throws IOException {
			out.jsonValue(value);
		}

		@Override
		public String read(final JsonReader in) throws IOException {
			var sb = new StringBuilder();
			int n = 0;
			while (true) {

				switch (in.peek()) {
				case BEGIN_ARRAY:
					in.beginArray();
					sb.append("[");
					break;
				case BEGIN_OBJECT:
					in.beginObject();
					sb.append("{");
					n++;
					break;
				case BOOLEAN:
					sb.append(in.nextBoolean()).append(",");
					break;
				case END_ARRAY:
					dropLastComma(sb);
					in.endArray();
					sb.append("]");
					break;
				case END_DOCUMENT:
					throw new RuntimeException("END_DOCUMENT invalid here");
				case END_OBJECT:
					dropLastComma(sb);
					in.endObject();
					sb.append("}");
					if (--n == 0)
						return sb.toString();
					break;
				case NAME:
					sb.append("\"").append(in.nextName()).append("\":");
					break;
				case NULL:
					in.nextNull();
					sb.append("");
					break;
				case NUMBER:
					try {
						sb.append(in.nextInt()).append(",");
						break;
					} catch (Exception e1) {
						try {
							sb.append(in.nextLong()).append(",");
							break;
						} catch (Exception e2) {
							sb.append(in.nextDouble()).append(",");
							break;
						}
					}
				case STRING:
					sb.append("\"").append(in.nextString()).append("\",");
					break;
				}
			}
		}

		private void dropLastComma(StringBuilder sb) {
			if (sb.charAt(sb.length() - 1) == ',') {
				sb.setLength(sb.length() - 1);
			}
		}
	}

	public String toJson(Gson gson) {
		return gson.toJson(this);
	}
}
