package com.kissme.photo.infrastructure.http.transport;

import javax.annotation.Nonnull;

import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;

import com.kissme.photo.infrastructure.http.Response;


/**
 * 
 * @author loudyn
 * 
 */
abstract class Responses {

	/**
	 * 
	 * @param response
	 * @return
	 */
	static Response newResponse(@Nonnull HttpResponse response) {
		return new DefaultNettyHttpResponse(response);
	}

	/**
	 * 
	 * @author loudyn
	 * 
	 */
	static final class DefaultNettyHttpResponse extends Response {

		private transient volatile boolean includeError = false;
		private transient volatile boolean includeRedirect = false;

		/**
		 * 
		 * @param delegate
		 */
		public DefaultNettyHttpResponse(HttpResponse delegate) {
			super(delegate);
		}

		@Override
		public void sendError(int code) {
			if (includeError) {
				return;
			}

			includeError = true;
			setStatus(HttpResponseStatus.valueOf(code));
		}

		@Override
		public void sendRedirect(String location) {
			if (includeRedirect) {
				return;
			}

			includeRedirect = true;
			setHeader(HttpHeaders.Names.LOCATION, location);
		}
	}

	private Responses() {}
}
