package com.kissme.photo.infrastructure.http.mapping;

import java.util.Map;

import org.jboss.netty.handler.codec.http.HttpMethod;

import com.kissme.photo.infrastructure.http.RequestHandler;

/**
 * 
 * @author loudyn
 * 
 */
public interface RequestHandlerMapping {

	/**
	 * 
	 * @param method
	 * @param mapping
	 * @param value
	 */
	public void register(HttpMethod[] methods, String mapping, RequestHandler handler);

	/**
	 * 
	 * @param method
	 * @param mapping  
	 * @return
	 */
	public RequestHandler getHandler(HttpMethod method, String mapping, Map<String, String> pathVariables);
}
