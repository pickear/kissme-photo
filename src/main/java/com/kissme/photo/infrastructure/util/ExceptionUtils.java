package com.kissme.photo.infrastructure.util;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author loudyn
 * 
 */
public final class ExceptionUtils {

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

	/**
	 * 
	 * @param throwables
	 * @return
	 */
	public static ComboException comboThrow(Throwable... throwables) {
		Collection<Throwable> throwablesAsCollection = Arrays.asList(throwables);
		return comboThrow(throwablesAsCollection);
	}

	/**
	 * 
	 * @param throwables
	 * @return
	 */
	public static ComboException comboThrow(Collection<Throwable> throwables) {
		return new ComboException(throwables);
	}

	/**
	 * 
	 * @author loudyn
	 * 
	 */
	@SuppressWarnings("serial")
	static final class ComboException extends RuntimeException {
		private Collection<Throwable> throwables;

		public ComboException() {
			throwables = new LinkedList<Throwable>();
		}

		public ComboException(Collection<Throwable> throwables) {
			this.throwables = throwables;
		}

		public ComboException push(Throwable throwable) {
			this.throwables.add(throwable);
			return this;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Throwable#getMessage()
		 */
		@Override
		public String getMessage() {
			StringBuilder sb = new StringBuilder();
			for (Throwable throwable : this.throwables) {
				sb.append(throwable.getMessage()).append("\n");
			}
			return sb.toString();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Throwable#getLocalizedMessage()
		 */
		@Override
		public String getLocalizedMessage() {
			StringBuilder sb = new StringBuilder();
			for (Throwable throwable : this.throwables) {
				sb.append(throwable.getLocalizedMessage()).append("\n");
			}
			return sb.toString();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Throwable#getCause()
		 */
		@Override
		public Throwable getCause() {
			if (this.throwables.isEmpty()) {
				return null;
			}

			Iterator<Throwable> it = this.throwables.iterator();
			return it.next();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Throwable#printStackTrace()
		 */
		@Override
		public void printStackTrace() {
			for (Throwable throwable : this.throwables) {
				throwable.printStackTrace();
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Throwable#printStackTrace(java.io.PrintStream)
		 */
		@Override
		public void printStackTrace(PrintStream s) {
			for (Throwable throwable : this.throwables) {
				throwable.printStackTrace(s);
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Throwable#printStackTrace(java.io.PrintWriter)
		 */
		@Override
		public void printStackTrace(PrintWriter s) {
			for (Throwable throwable : this.throwables) {
				throwable.printStackTrace(s);
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Throwable#getStackTrace()
		 */
		@Override
		public StackTraceElement[] getStackTrace() {
			List<StackTraceElement> result = new LinkedList<StackTraceElement>();
			for (Throwable throwable : this.throwables) {
				result.addAll(Arrays.asList(throwable.getStackTrace()));
			}

			return result.toArray(new StackTraceElement[] {});
		}
	}

	private ExceptionUtils() {}
}
