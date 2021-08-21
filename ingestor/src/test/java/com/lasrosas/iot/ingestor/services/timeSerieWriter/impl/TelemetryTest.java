package com.lasrosas.iot.ingestor.services.timeSerieWriter.impl;

import com.lasrosas.iot.shared.telemetry.NotPartOfTelemetry;
import com.lasrosas.iot.shared.telemetry.Telemetry;
import com.lasrosas.iot.shared.telemetry.TelemetryState;

public class TelemetryTest extends Telemetry {
	public static class ValuesHolder {
		private int glop = 123;

		public int getGlop() {
			return glop;
		}

		public void setGlop(int glop) {
			this.glop = glop;
		}
	}

	private int intValue = 123;
	private float floatValue = 123.456f;
	private double doubleValue = 123.456;
	private long longValue = 123l;
	private short shortValue = 123;
	private boolean booleanValue = true;

	private Integer intValueOrNull;
	private Float floatValueOrNull;
	private Double doubleVvalueOrNull;
	private Long longValueOrNull;
	private Short shortValueOrNull;
	private Boolean booleanValueOrNull;
	private Character charValueOrNull;

	@TelemetryState
	private ValuesHolder valuesHolder = new ValuesHolder();

	@NotPartOfTelemetry
	private int notParOfTelemetry;

	private String stringValue = "hello";

	public ValuesHolder getValuesHolder() {
		return valuesHolder;
	}

	public void setValuesHolder(ValuesHolder valuesHolder) {
		this.valuesHolder = valuesHolder;
	}

	public int getNotParOfTelemetry() {
		return notParOfTelemetry;
	}

	public void setNotParOfTelemetry(int notParOfTelemetry) {
		this.notParOfTelemetry = notParOfTelemetry;
	}

	public int getIntValue() {
		return intValue;
	}

	public void setIntValue(int intValue) {
		this.intValue = intValue;
	}

	public float getFloatValue() {
		return floatValue;
	}

	public void setFloatValue(float floatValue) {
		this.floatValue = floatValue;
	}

	public double getDoubleValue() {
		return doubleValue;
	}

	public void setDoubleValue(double doubleValue) {
		this.doubleValue = doubleValue;
	}

	public long getLongValue() {
		return longValue;
	}

	public void setLongValue(long longValue) {
		this.longValue = longValue;
	}

	public short getShortValue() {
		return shortValue;
	}

	public void setShortValue(short shortValue) {
		this.shortValue = shortValue;
	}

	public boolean isBooleanValue() {
		return booleanValue;
	}

	public void setBooleanValue(boolean booleanValue) {
		this.booleanValue = booleanValue;
	}

	public Integer getIntValueOrNull() {
		return intValueOrNull;
	}

	public void setIntValueOrNull(Integer intValueOrNull) {
		this.intValueOrNull = intValueOrNull;
	}

	public Float getFloatValueOrNull() {
		return floatValueOrNull;
	}

	public void setFloatValueOrNull(Float floatValueOrNull) {
		this.floatValueOrNull = floatValueOrNull;
	}

	public Double getDoubleVvalueOrNull() {
		return doubleVvalueOrNull;
	}

	public void setDoubleVvalueOrNull(Double doubleVvalueOrNull) {
		this.doubleVvalueOrNull = doubleVvalueOrNull;
	}

	public Long getLongValueOrNull() {
		return longValueOrNull;
	}

	public void setLongValueOrNull(Long longValueOrNull) {
		this.longValueOrNull = longValueOrNull;
	}

	public Short getShortValueOrNull() {
		return shortValueOrNull;
	}

	public void setShortValueOrNull(Short shortValueOrNull) {
		this.shortValueOrNull = shortValueOrNull;
	}

	public Boolean getBooleanValueOrNull() {
		return booleanValueOrNull;
	}

	public void setBooleanValueOrNull(Boolean booleanValueOrNull) {
		this.booleanValueOrNull = booleanValueOrNull;
	}

	public Character getCharValueOrNull() {
		return charValueOrNull;
	}

	public void setCharValueOrNull(Character charValueOrNull) {
		this.charValueOrNull = charValueOrNull;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

}
