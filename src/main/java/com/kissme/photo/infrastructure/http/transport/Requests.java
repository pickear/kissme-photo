package com.kissme.photo.infrastructure.http.transport;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import org.apache.commons.codec.net.URLCodec;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kissme.photo.infrastructure.http.Request;

/**
 * 
 * @author loudyn
 * 
 */
abstract class Requests {
	private static final String ONLY_ACCEPT_CHARSET = "UTF-8";

	/**
	 * 
	 * @param request
	 * @return
	 */
	static Request newRequest(@Nonnull HttpRequest request) {
		Charset charset = determineCharset(request);
		Map<String, List<String>> queryParams = decodeQueryParams(request, charset);
		Map<String, List<String>> contentParams = decodeContentParams(request, charset, queryParams);

		Request nettyRequest = new DefaultNettyHttpRequest(request, Maps.transformValues(contentParams, new Function<List<String>, String>() {

			public String apply(List<String> input) {
				if (null == input || input.isEmpty()) {
					return null;
				}

				return input.get(0);
			}
		}));

		nettyRequest.charset(ONLY_ACCEPT_CHARSET);
		return nettyRequest;
	}

	private static Charset determineCharset(HttpRequest request) {
		return Charset.forName(ONLY_ACCEPT_CHARSET);
	}

	private static Map<String, List<String>> decodeQueryParams(HttpRequest request, Charset charset) {
		return new QueryStringDecoder(request.getUri(), charset).getParameters();
	}

	private static Map<String, List<String>> decodeContentParams(HttpRequest request, Charset charset, Map<String, List<String>> params) {
		if (HttpMethod.GET == request.getMethod()) {
			return params;
		}

		if (request.isChunked()) {
			return params;
		}

		String contentType = request.getHeader(HttpHeaders.Names.CONTENT_TYPE);
		if (null != contentType && contentType.toLowerCase().startsWith("multipart/")) {
			return params;
		}

		String contentString = decodeContent(request.getContent().array(), charset);
		return decodeContentParams(contentString, params);
	}

	private static String decodeContent(byte[] array, Charset charset) {
		try {

			byte[] bytes = new URLCodec().decode(array);
			return new String(bytes, charset);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static Map<String, List<String>> decodeContentParams(String contentString, Map<String, List<String>> params) {

		// when params is empty,it may be a Collections.emptyMap(),to prevent some exception,we reassign it
		if (params.isEmpty()) {
			params = Maps.newLinkedHashMap();
		}

		String name = null;

		int pos = 0; // Beginning of the unprocessed region
		int i; // End of the unprocessed region
		char c = 0; // Current character

		for (i = 0; i < contentString.length(); i++) {

			c = contentString.charAt(i);
			if (c == '=' && name == null) {
				if (pos != i) {
					name = contentString.substring(pos, i);
				}
				pos = i + 1;
			}
			else if (c == '&') {
				if (name == null && pos != i) {
					// We haven't seen an `=' so far but moved forward.
					// Must be a param of the form '&a&' so add it with
					// an empty value.
					addParam(params, contentString.substring(pos, i), "");
				}
				else if (name != null) {
					addParam(params, name, contentString.substring(pos, i));
					name = null;
				}
				pos = i + 1;
			}
		}

		if (pos != i) { // Are there characters we haven't dealt with?
			if (name == null) { // Yes and we haven't seen any `='.
				addParam(params, contentString.substring(pos, i), "");
			}
			else { // Yes and this must be the last value.
				addParam(params, name, contentString.substring(pos, i));
			}
		}

		else if (name != null) { // Have we seen a name without value?
			addParam(params, name, "");
		}

		return params;
	}

	private static void addParam(Map<String, List<String>> params, String name, String value) {
		List<String> values = params.get(name);
		if (values == null) {
			values = Lists.newLinkedList(); // Often there's only 1 value.
			params.put(name, values);
		}
		values.add(value);
	}

	/**
	 * 
	 * @author loudyn
	 * 
	 */
	static final class DefaultNettyHttpRequest extends Request {
		private final Map<String, String> params;
		private final Map<String, String> pathVariables = Maps.newConcurrentMap();

		public DefaultNettyHttpRequest(HttpRequest delegate, Map<String, String> params) {
			super(delegate);
			this.params = params;
		}

		@Override
		public String param(String name) {
			return params.get(name);
		}

		@Override
		public Map<String, String> params() {
			return Collections.unmodifiableMap(params);
		}

		@Override
		public Map<String, String> pathVariables() {
			return pathVariables;
		}
	}

	private Requests() {}
}
