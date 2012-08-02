package com.kissme.photo.infrastructure.http.transport;

import com.google.inject.AbstractModule;
import com.kissme.photo.infrastructure.http.DefaultRequestHandlerChainFactory;
import com.kissme.photo.infrastructure.http.RequestHandlerChainFactory;
import com.kissme.photo.infrastructure.http.exception.RequestExceptionTranslator;
import com.kissme.photo.infrastructure.http.exception.ResponseStatusExceptionTranslator;
import com.kissme.photo.infrastructure.http.mapping.DefaultRequestHandlerMapping;
import com.kissme.photo.infrastructure.http.mapping.RequestHandlerMapping;
import com.kissme.photo.infrastructure.http.multipart.MultipartRequestResolver;
import com.kissme.photo.infrastructure.http.multipart.commons.CommonsMultipartRequestResolver;

/**
 * 
 * @author loudyn
 * 
 */
public class NettyTransportModule extends AbstractModule {

	@Override
	protected void configure() {
		
		bind(RequestHandlerMapping.class).to(DefaultRequestHandlerMapping.class).asEagerSingleton();
		bind(RequestExceptionTranslator.class).to(ResponseStatusExceptionTranslator.class).asEagerSingleton();
		bind(MultipartRequestResolver.class).to(CommonsMultipartRequestResolver.class).asEagerSingleton();
		bind(RequestHandlerChainFactory.class).to(DefaultRequestHandlerChainFactory.class).asEagerSingleton();
	}
}
