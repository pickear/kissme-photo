package com.kissme.photo.infrastructure.http;

/**
 * 
 * @author loudyn
 * 
 */
public interface RequestHandlerChainFactory {

	/**
	 * 
	 * @param requestInterceptor
	 * 
	 * register the interceptor to the chainFactory,when create a new RequestHandlerChain,it will bound to 
	 */
	void registerRequestInterceptor(RequestInterceptor requestInterceptor);

	/**
	 * 
	 * @return
	 * 
	 * create a new RequestHandlerChain
	 */
	RequestHandlerChain newRequestHandlerChain(RequestHandler requestHandler);
}
