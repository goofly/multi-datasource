package com.goofly.multidb.core;

public class DynamicDataSourceContextHolder {

	private static final ThreadLocal<String> currentDatesource = new ThreadLocal<>();

	public static void clear() {
		currentDatesource.remove();
	}

	public static String get() {
		return currentDatesource.get();
	}

	public static void set(String value) {
		currentDatesource.set(value);
	}

}
