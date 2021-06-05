package com.lasrosas.iot.shared.ontology;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class IotMessage {

	private final long txid;
	private final LocalDateTime time;
	private final List<Point> points = new ArrayList<>();

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

	public static class Point {
		private final long techid;
		private final String schema;

		@JsonAdapter(RawJsonGsonAdapter.class)
		private final String value;

		public Point(long techid, String schema, String value) {
			super();
			this.techid = techid;
			this.schema = schema;
			this.value = value;
		}

		public long getTechid() {
			return techid;
		}

		public String getSchema() {
			return schema;
		}

		public String getValue() {
			return value;
		}
	}

	public IotMessage(long txid, LocalDateTime time) {
		super();
		this.txid = txid;
		this.time = time;
	}

	public long getTxid() {
		return txid;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public List<Point> getPoints() {
		return points;
	}

	public String toJson(Gson gson) {
		return gson.toJson(this);
	}

	public static IotMessage fronJson(Gson gson, String json) {
		return gson.fromJson(json, IotMessage.class);
	}
}
