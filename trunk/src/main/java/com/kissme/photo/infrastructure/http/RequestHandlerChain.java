package com.kissme.photo.infrastructure.http;


/**
 * 
 * @author loudyn
 * 
 */
public interface RequestHandlerChain {

	/**
	 * 
	 * @param request
	 * @param response
	 */
	public void handle(Request request, Response response);
}
