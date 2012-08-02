package com.kissme.photo.infrastructure.http;



/**
 * 
 * @author loudyn
 * 
 */
public interface RequestInterceptor {
	
	/**
	 * 
	 * @return
	 */
	public int getOrder();

	/**
	 * 
	 * @param request
	 * @param response
	 * @param handlerChain
	 */
	public void intercept(Request request, Response response, RequestHandlerChain handlerChain);
}
