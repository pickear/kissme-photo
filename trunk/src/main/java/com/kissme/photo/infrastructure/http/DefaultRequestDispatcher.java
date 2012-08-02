package com.kissme.photo.infrastructure.http;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.HttpMethod;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.kissme.photo.infrastructure.http.exception.RequestExceptionCode;
import com.kissme.photo.infrastructure.http.exception.RequestExceptionTranslator;
import com.kissme.photo.infrastructure.http.mapping.RequestHandlerMapping;
import com.kissme.photo.infrastructure.http.multipart.MultipartRequest;
import com.kissme.photo.infrastructure.http.multipart.MultipartRequestResolver;
import com.kissme.photo.infrastructure.ioc.Ioc;

/**
 * 
 * @author loudyn
 * 
 */
public class DefaultRequestDispatcher {

	private final Ioc ioc;

	/**
	 * mapping the request to concrete handler
	 */
	private RequestHandlerMapping requestHandlerMapping;

	/**
	 * create the requestHandlerChain
	 */
	private RequestHandlerChainFactory requestHandlerChainFactory;

	/**
	 * resolve the multipart request
	 */
	private MultipartRequestResolver multipartRequestResolver;

	/**
	 * translate the exception
	 */
	private RequestExceptionTranslator requestExceptionTranslator;

	private final Log log = LogFactory.getLog(DefaultRequestDispatcher.class);
	private final static List<String> ENHANCE_METHODS = ImmutableList.of("delete", "put");

	/**
	 * 
	 * @param ioc
	 */
	public DefaultRequestDispatcher(Ioc ioc) {
		this.ioc = ioc;

		initHandlerMapping();
		initHandlerChainFactory();
		initMultipartResolver();
		initExceptionTranslator();
	}

	public void dispatch(Request request, Response response) {

		try {

			request = checkMultipart(request);
			RequestHandlerChain handlerChain = getHandler(request);
			if (null == handlerChain) {
				notHandlerFound(request, response);
				return;
			}

			handlerChain.handle(request, response);
		} catch (Exception e) {
			handleException(request, response, e);
		} finally {
			cleanupMultipart(request);
		}
	}

	private void initHandlerMapping() {
		this.requestHandlerMapping = getIoc().getBean(RequestHandlerMapping.class);
		Preconditions.checkNotNull(requestHandlerMapping);

		Iterator<Object> handlers = Iterators.filter(getIoc().getAllBeans().iterator(), new Predicate<Object>() {

			public boolean apply(Object input) {
				return input instanceof RequestHandler;
			}

		});

		while (handlers.hasNext()) {
			RequestHandler handler = (RequestHandler) handlers.next();
			this.requestHandlerMapping.register(handler.getMappingMethods(), handler.getMapping(), handler);
		}
	}

	private void initHandlerChainFactory() {
		this.requestHandlerChainFactory = getIoc().getBean(RequestHandlerChainFactory.class);
		Preconditions.checkNotNull(requestHandlerChainFactory);

		Iterator<Object> interceptors = Iterators.filter(getIoc().getAllBeans().iterator(), new Predicate<Object>() {

			public boolean apply(Object input) {
				return input instanceof RequestInterceptor;
			}

		});

		while (interceptors.hasNext()) {
			RequestInterceptor interceptor = (RequestInterceptor) interceptors.next();
			this.requestHandlerChainFactory.registerRequestInterceptor(interceptor);
		}

	}

	private void initMultipartResolver() {
		this.multipartRequestResolver = getIoc().getBean(MultipartRequestResolver.class);
		Preconditions.checkNotNull(multipartRequestResolver);
	}

	private void initExceptionTranslator() {
		this.requestExceptionTranslator = getIoc().getBean(RequestExceptionTranslator.class);
		Preconditions.checkNotNull(requestExceptionTranslator);
	}

	private Request checkMultipart(Request request) {

		if (multipartRequestResolver.isMultipart(request)) {
			return multipartRequestResolver.resolveMultipart(request);
		}

		return request;
	}

	private RequestHandlerChain getHandler(Request request) {
		enhanceBrowserBehaviourIfPossiable(request);
		RequestHandler handler = requestHandlerMapping.getHandler(request.getMethod(), request.getPath(), request.getPathVariables());
		return prepareRequestHandlerChain(handler);
	}

	private void enhanceBrowserBehaviourIfPossiable(Request request) {
		String hiddenMethod = request.getParameter("_method");
		if (HttpMethod.POST == request.getMethod() && ENHANCE_METHODS.contains(StringUtils.lowerCase(hiddenMethod))) {
			request.setMethod(HttpMethod.valueOf(hiddenMethod));
		}
	}

	private RequestHandlerChain prepareRequestHandlerChain(RequestHandler handler) {
		if (null == handler) {
			return null;
		}

		return requestHandlerChainFactory.newRequestHandlerChain(handler);
	}

	private void notHandlerFound(Request request, Response response) {
		response.sendError(404);
		response.setContent(ChannelBuffers.copiedBuffer("No handler found!", Charset.forName(request.getCharset())));
	}

	private void handleException(Request request, Response response, Exception e) {
		log.error("handle request occur error.", e);
		RequestExceptionCode code = requestExceptionTranslator.translate(e);
		if (null != code) {
			response.sendError(code.getCode());
			response.setContent(ChannelBuffers.copiedBuffer(code.getMessage(), Charset.forName(request.getCharset())));
			return;
		}

		response.sendError(500);
		response.setContent(ChannelBuffers.copiedBuffer(e.getMessage(), Charset.forName(request.getCharset())));
	}

	private void cleanupMultipart(Request request) {
		if (null != request && request instanceof MultipartRequest) {
			multipartRequestResolver.cleanupMultipart((MultipartRequest) request);
		}
	}

	private Ioc getIoc() {
		return ioc;
	}
}
