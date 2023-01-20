package com.lasrosas.iot.core.shared.utils;

import java.lang.reflect.ParameterizedType;
import java.util.Optional;

public class Junion {
	private final Optional<?> value;
	private final Class<?> valueClass;
	private final String name;

	public <T> Junion(T value) {
		this(value, value.getClass().getSimpleName());
	}

	public <T> Junion(T value, Class<? extends T> valueClass) {
		this(value, valueClass.getSimpleName());
	}

	public <T> Junion(T value, String name) {
		this.value = Optional.of(value);
		this.valueClass = value.getClass();
		this.name = name;
		System.out.println(this.valueClass.getSimpleName());
	}

	@SuppressWarnings("unchecked")
	public <T> Junion(Optional<T> mayBeValue, String name) {
		this.value = mayBeValue;

		this.valueClass = (Class<T>) ((ParameterizedType)mayBeValue.getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
		this.name = name;
		System.out.println(this.valueClass.getSimpleName());
	}

	public <T> Optional<T> getValueAs(Class<T> thisClass) {
		return getValueAs(thisClass.getSimpleName(), thisClass);
	}

	@SuppressWarnings("unchecked")
	public <T> Optional<T> getValueAs(String name, Class<T> thisClass) {
		if( !name.equals(this.name) || !thisClass.isAssignableFrom(this.valueClass) )
			return Optional.empty();

		return (Optional<T>)value;
	}

	public Object getValue() {
		return value;
	}

	public Class<?> getValueClass() {
		return valueClass;
	}

	public boolean isA(Class<?> thisClass) {
		return !this.valueClass.isAssignableFrom(thisClass);
	}
}
