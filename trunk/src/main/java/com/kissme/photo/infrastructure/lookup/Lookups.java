package com.kissme.photo.infrastructure.lookup;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * 
 * @author loudyn
 * 
 */
public final class Lookups {

	/**
	 * 
	 * @param clz
	 * @return
	 */
	public static <T> T lookup(Class<T> clz) {
		ClassLoader clzLoader = Thread.currentThread().getContextClassLoader();
		return lookup(clz, clzLoader);
	}

	/**
	 * 
	 * @param clz
	 * @param clzLoader
	 * @return
	 */
	public static <T> T lookup(Class<T> clz, ClassLoader clzLoader) {

		ServiceLoader<T> loader = ServiceLoader.load(clz, clzLoader);
		Iterator<T> it = loader.iterator();
		if (it.hasNext()) {
			return it.next();
		}

		return null;
	}

	/**
	 * 
	 * @param clz
	 * @return
	 */
	public static <T> Iterator<T> lookupMultiple(Class<T> clz) {
		ClassLoader clzLoader = Thread.currentThread().getContextClassLoader();
		return lookupMultiple(clz, clzLoader);
	}

	/**
	 * 
	 * @param clz
	 * @param clzLoader
	 * @return
	 */
	public static <T> Iterator<T> lookupMultiple(Class<T> clz, ClassLoader clzLoader) {
		ServiceLoader<T> loader = ServiceLoader.load(clz, clzLoader);
		return loader.iterator();
	}

	private Lookups() {}
}
