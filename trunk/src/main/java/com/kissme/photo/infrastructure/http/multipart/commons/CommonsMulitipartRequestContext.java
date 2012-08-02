package com.kissme.photo.infrastructure.http.multipart.commons;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.fileupload.RequestContext;
import org.jboss.netty.handler.codec.http.HttpHeaders;

import com.kissme.photo.infrastructure.http.Request;

/**
 * 
 * @author loudyn
 *
 */
public class CommonsMulitipartRequestContext implements RequestContext {

	private Request request;
	private InputStream inputStream;

	/**
	 * 
	 * @param request
	 */
	public CommonsMulitipartRequestContext(Request request) {
		this.request = request;
		inputStream = new CommonsRequestContentStreamAdpater(request);
	}

	public String getCharacterEncoding() {
		return request.getCharset();
	}

	public String getContentType() {
		return request.getHeader(HttpHeaders.Names.CONTENT_TYPE);
	}

	public int getContentLength() {
		return Integer.parseInt(request.getHeader(HttpHeaders.Names.CONTENT_LENGTH));
	}

	public InputStream getInputStream() throws IOException {
		return inputStream;
	}
}
