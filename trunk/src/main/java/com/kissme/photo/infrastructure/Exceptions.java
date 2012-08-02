package com.kissme.photo.infrastructure;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * 
 * @author loudyn
 * 
 */
public abstract class Exceptions {
	/**
	 * 
	 * @return
	 */
	public static RuntimeException impossiable() {
		return new RuntimeException("r u kidding me!");
	}

	/**
	 * 
	 * @param message
	 * @return
	 */
	public static RuntimeException oneThrow(String message) {
		return new RuntimeException(message);
	}

	/**
	 * 
	 * @param e
	 * @return
	 */
	public static RuntimeException uncheck(Throwable e) {
		if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		}

		if (e instanceof InvocationTargetException) {
			Throwable cause = e.getCause();
			return new RuntimeException(cause);
		}

		return new RuntimeException(e);
	}

	/**
	 * 
	 * @param e
	 * @return
	 */
	public static Throwable unwrap(Throwable e) {
		Throwable unwrapp = e;

		while (true) {
			if (unwrapp instanceof InvocationTargetException) {
				unwrapp = ((InvocationTargetException) unwrapp).getTargetException();
			} else if (unwrapp instanceof UndeclaredThrowableException) {
				unwrapp = ((UndeclaredThrowableException) unwrapp).getUndeclaredThrowable();
			} else {
				return unwrapp;
			}
		}
	}

	private Exceptions() {}
}
