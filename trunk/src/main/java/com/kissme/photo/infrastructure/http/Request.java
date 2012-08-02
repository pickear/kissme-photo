package com.kissme.photo.infrastructure.http;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;

import com.google.common.base.Preconditions;

/**
 * 
 * @author loudyn
 * 
 */
public abstract class Request implements HttpRequest {

	private final HttpRequest httpRequest;

	private String charset;

	/**
	 * 
	 * @param delegate
	 */
	protected Request(HttpRequest delegate) {
		Preconditions.checkNotNull(delegate);
		this.httpRequest = delegate;
	}

	public String getHeader(String name) {
		return this.httpRequest.getHeader(name);
	}

	public List<String> getHeaders(String name) {
		return this.httpRequest.getHeaders(name);
	}

	public List<Entry<String, String>> getHeaders() {
		return this.httpRequest.getHeaders();
	}

	public boolean containsHeader(String name) {
		return this.httpRequest.containsHeader(name);
	}

	public Set<String> getHeaderNames() {
		return this.httpRequest.getHeaderNames();
	}

	public HttpVersion getProtocolVersion() {
		return this.httpRequest.getProtocolVersion();
	}

	public void setProtocolVersion(HttpVersion version) {
		this.httpRequest.setProtocolVersion(version);
	}

	public ChannelBuffer getContent() {
		return this.httpRequest.getContent();
	}

	public void setContent(ChannelBuffer content) {
		this.httpRequest.setContent(content);
	}

	public void addHeader(String name, Object value) {
		this.httpRequest.addHeader(name, value);
	}

	public void setHeader(String name, Object value) {
		this.httpRequest.setHeader(name, value);
	}

	public void setHeader(String name, Iterable<?> values) {
		this.httpRequest.setHeader(name, values);
	}

	public void removeHeader(String name) {
		this.httpRequest.removeHeader(name);
	}

	public void clearHeaders() {
		this.httpRequest.clearHeaders();
	}

	@Deprecated
	public long getContentLength() {
		return this.httpRequest.getContentLength();
	}

	@Deprecated
	public long getContentLength(long defaultValue) {
		return this.httpRequest.getContentLength(defaultValue);
	}

	public boolean isChunked() {
		return this.httpRequest.isChunked();
	}

	public void setChunked(boolean chunked) {
		this.httpRequest.setChunked(chunked);
	}

	public boolean isKeepAlive() {
		boolean http10 = this.httpRequest.getProtocolVersion().equals(HttpVersion.HTTP_1_0);
		String connection = this.httpRequest.getHeader(HttpHeaders.Names.CONNECTION);
		return HttpHeaders.Values.CLOSE.equalsIgnoreCase(connection) || (http10 && !HttpHeaders.Values.KEEP_ALIVE.equalsIgnoreCase(connection));
	}

	public HttpMethod getMethod() {
		return this.httpRequest.getMethod();
	}

	public void setMethod(HttpMethod method) {
		this.httpRequest.setMethod(method);
	}

	public String getUri() {
		return this.httpRequest.getUri();
	}

	public void setUri(String uri) {
		this.httpRequest.setUri(uri);
	}

	public String getPath() {
		return new QueryStringDecoder(getUri()).getPath();
	}

	/**
	 * 
	 * @return
	 */
	public String getCharset() {
		return this.charset;
	}

	/**
	 * 
	 * @param charset
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public abstract String getParameter(String name);

	/**
	 * 
	 * @return
	 */
	public abstract Map<String, String> getParameterMap();

	/**
	 * 
	 * @return
	 */
	public abstract Map<String, String> getPathVariables();
}
