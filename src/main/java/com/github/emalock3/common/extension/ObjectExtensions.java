package com.github.emalock3.common.extension;

import java.util.Optional;

/**
 * provides utility methods for java.lang.Object.
 * 
 * @author Shinobu Aoki
 */
public final class ObjectExtensions {
	private ObjectExtensions() {
	}
	
	/**
     * @param <T>
	 * @param object
	 * @param ifNull
	 * @return object when object is not null, otherwise returns ifNull
	 */
	public static <T> T or(T object, T ifNull) {
		return object != null ? object : ifNull;
	}
    
    /**
     * 
     * @param <T>
     * @param object
     * @return Optional#empty() when object is null, otherwise returns Optional#of(object)
     */
    public static <T> Optional<T> opt(T object) {
        return object != null ? Optional.of(object) : Optional.empty();
    }
    
}
