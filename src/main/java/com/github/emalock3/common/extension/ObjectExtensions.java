package com.github.emalock3.common.extension;

/**
 * provides utility methods for java.lang.Object.
 * 
 * @author Shinobu Aoki
 */
public final class ObjectExtensions {
	private ObjectExtensions() {
	}
	
	/**
	 * @param object
	 * @param ifNull
	 * @return object when object is not null, otherwise returns ifNull
	 */
	public static <T> T or(T object, T ifNull) {
		return object != null ? object : ifNull;
	}
}
