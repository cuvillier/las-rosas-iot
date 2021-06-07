package com.lasrosas.iot.database.influxdb;

import java.util.Properties;

import com.influxdb.client.InfluxDBClientFactory;

public class InfluxdbCommand {
	public static void main(String ... argv) {
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

			var measurement = "twin.LasRosas.WaterTank.WaterTank";

			var q = "from(bucket: \"lasrosasiot\")\n"
					+ "  |> range(start: -10d)\n"
					+ "  |> filter(fn: (r) =>\n"
					+ "      r._measurement == \"" + measurement + "\""
					+ "  )";

			var queryAPI = influxDB.getQueryApi();
			var tables = queryAPI.query(q);

			for(var table: tables) {
				for(var record: table.getRecords()) {
					System.out.println(
							record.getValueByKey("_field") + " " +
							record.getValueByKey("_value") + " " +
							record.getValueByKey("_start"));
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
