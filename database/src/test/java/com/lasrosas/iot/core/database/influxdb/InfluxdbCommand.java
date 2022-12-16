package com.lasrosas.iot.database.influxdb;

public class InfluxdbCommand {
	public static void main(String ... argv) {
/*
		try {
			var inputStream = InfluxdbCommand.class
					  .getClassLoader()
					  .getResourceAsStream("influxdb.properties");
			var prop = new Properties();
			prop.load(inputStream);

			var influxDB = InfluxDBClientFactory.create(
					prop.getProperty("influxdb.server"),
					prop.getProperty("influxdb.token").toCharArray(),
					prop.getProperty("influxdb.org"),
					prop.getProperty("influxdb.bucket"));

			var tables = influxDB.getQueryApi().query("import \"influxdata/influxdb/schema\"\n"
					+ "\n"
					+ "schema.measurements(bucket: \"lasrosasiot\")");

			for(var table: tables) {
				for(var record: table.getRecords()) {
					var measurement = record.getValueByKey("_value").toString();
					System.out.println(measurement);
					normalize(influxDB, measurement);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private static void normalize(InfluxDBClient influxDB, String measurementIn) {
		var measurementOut = measurementIn.replaceAll("[\\.-]", "_");
		if(measurementOut.equals(measurementIn)) return;

		System.out.println("Normalization " + measurementIn + " into " + measurementOut);

		var q = "from(bucket: \"lasrosasiot\")\n"
				+ "  |> range(start: 0)\n"
				+ "  |> filter(fn: (r) => r._measurement == \"" + measurementIn + "\"\n"
				+ "  )";

		var queryAPI = influxDB.getQueryApi();
		var tables = queryAPI.query(q);

		var writeAPI = influxDB.getWriteApi();

		int n = 0;
		for(var table: tables) {
			for(var record: table.getRecords()) {
				n++;
				var point = new Point(measurementOut).time(record.getTime(), WritePrecision.MS);

				System.out.println(record.getField() + "=" + record.getValue());

				if(record.getValue() instanceof Boolean)
					point.addField(record.getField(), (Boolean)record.getValue());
				else if(record.getValue() instanceof Long)
					point.addField(record.getField(), (Long)record.getValue());
				else if(record.getValue() instanceof Double)
					point.addField(record.getField(), (Double)record.getValue());
				else if(record.getValue() instanceof Number)
					point.addField(record.getField(), (Number)record.getValue());
				else if(record.getValue() instanceof String)
					point.addField(record.getField(), (String)record.getValue());
				else
					throw new RuntimeException("Unsupported type: " + record.getValue().getClass().getName());

				// System.out.println("Write\n");
				writeAPI.writePoint(point);
			}
			break;
		}
		System.out.println("Wrote " + n + " points");
		*/
	}
}
