package com.lasrosas.iot.core.ingestor.timeSerieWriter.api;

import javax.validation.constraints.NotNull;

import com.influxdb.LogLevel;

public class InfluxDBConfig {

	@NotNull
	private String url = "http://127.0.0.1:8086";

	@NotNull
	private String token;

	@NotNull
	private String org;

	@NotNull
	private String bucket = "lasrosasiot";

	private boolean gzip = false;
	
	private String logLevel = LogLevel.BASIC + "";

	public InfluxDBConfig() {}

	public InfluxDBConfig(@NotNull String url, @NotNull String token, @NotNull String org, @NotNull String bucket) {
		super();
		this.url = url;
		this.token = token;
		this.org = org;
		this.bucket = bucket;
	}

	public String getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	public boolean isGzip() {
		return gzip;
	}

	public void setGzip(boolean gzip) {
		this.gzip = gzip;
	}
}
