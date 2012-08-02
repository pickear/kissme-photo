package com.kissme.photo.infrastructure.http;

import org.jboss.netty.handler.codec.http.HttpMethod;


/**
 * 
 * @author loudyn
 * 
 */
public interface RequestHandler {

	/**
	 * 
	 * @return
	 */
	public String getMapping();

	/**
	 * 
	 * @return
	 */
	public HttpMethod[] getMappingMethods();

	/**
	 * 
	 * @param request
	 * @param response
	 */
	public void handleRequest(Request request, Response response);
}
