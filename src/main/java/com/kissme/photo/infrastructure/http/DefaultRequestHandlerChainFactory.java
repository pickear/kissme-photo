package com.kissme.photo.infrastructure.http;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * 
 * @author loudyn
 * 
 */
public class DefaultRequestHandlerChainFactory implements RequestHandlerChainFactory {

	private final static Comparator<RequestInterceptor> ORDER_COMPARATOR = new Comparator<RequestInterceptor>() {

		public int compare(RequestInterceptor o1, RequestInterceptor o2) {
			return o1.getOrder() - o2.getOrder();
		}

	};
	private List<RequestInterceptor> registerInterceptors = Lists.newLinkedList();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kissme.photo.infrastructure.http.RequestHandlerChainFactory#registerRequestInterceptor(com.kissme.photo.infrastructure.http.RequestInterceptor)
	 */
	public void registerRequestInterceptor(RequestInterceptor requestInterceptor) {
		registerInterceptors.add(requestInterceptor);
		Collections.sort(registerInterceptors, ORDER_COMPARATOR);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kissme.photo.infrastructure.http.RequestHandlerChainFactory#newRequestHandlerChain(com.kissme.photo.infrastructure.http.RequestHandler)
	 */
	public RequestHandlerChain newRequestHandlerChain(RequestHandler requestHandler) {
		return new DefaultRequestHandlerChain(requestHandler, registerInterceptors);
	}

	/**
	 * 
	 * @author loudyn
	 * 
	 */
	class DefaultRequestHandlerChain implements RequestHandlerChain {

		private int position = -1;

		private RequestHandler handler;
		private List<RequestInterceptor> interceptors;

		/**
		 * 
		 * @param handler
		 */
		public DefaultRequestHandlerChain(@Nonnull RequestHandler handler, @Nonnull List<RequestInterceptor> interceptors) {
			Preconditions.checkNotNull(handler);
			Preconditions.checkNotNull(interceptors);
			this.handler = handler;
			this.interceptors = interceptors;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.kissme.photo.infrastructure.http.RequestHandlerChain#handle(com.kissme.photo.infrastructure.http.Request,
		 * com.kissme.photo.infrastructure.http.Response)
		 */
		public void handle(Request request, Response response) {
			if (++position < interceptors.size()) {
				interceptors.get(position).intercept(request, response, this);
				return;
			}

			handler.handleRequest(request, response);
		}

	}

}
