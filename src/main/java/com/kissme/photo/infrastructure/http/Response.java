package com.kissme.photo.infrastructure.http;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;

/**
 * 
 * @author loudyn
 * 
 */
public abstract class Response implements HttpResponse {

	private final HttpResponse httpResponse;

	private String charset;
	private transient volatile boolean includeCharset = false;
	private String contentType = "text/html";

	protected Response(HttpResponse delegate) {
		this.httpResponse = delegate;
	}

	public String getHeader(String name) {
		return this.httpResponse.getHeader(name);
	}

	public List<String> getHeaders(String name) {
		return this.httpResponse.getHeaders(name);
	}

	public List<Entry<String, String>> getHeaders() {
		return this.httpResponse.getHeaders();
	}

	public boolean containsHeader(String name) {
		return this.httpResponse.containsHeader(name);
	}

	public Set<String> getHeaderNames() {
		return this.httpResponse.getHeaderNames();
	}

	public HttpVersion getProtocolVersion() {
		return this.httpResponse.getProtocolVersion();
	}

	public void setProtocolVersion(HttpVersion version) {
		this.httpResponse.setProtocolVersion(version);
	}

	public ChannelBuffer getContent() {
		return this.httpResponse.getContent();
	}

	public void setContent(ChannelBuffer content) {
		this.httpResponse.setContent(content);
	}

	public void addHeader(String name, Object value) {
		this.httpResponse.addHeader(name, value);
	}

	public void setHeader(String name, Object value) {
		this.httpResponse.setHeader(name, value);
	}

	public void setHeader(String name, Iterable<?> values) {
		this.httpResponse.setHeader(name, values);
	}

	public void removeHeader(String name) {
		this.httpResponse.removeHeader(name);
	}

	public void clearHeaders() {
		this.httpResponse.clearHeaders();
	}

	@Deprecated
	public long getContentLength() {
		return this.httpResponse.getContentLength();
	}

	@Deprecated
	public long getContentLength(long defaultValue) {
		return this.httpResponse.getContentLength(defaultValue);
	}

	public boolean isChunked() {
		return this.httpResponse.isChunked();
	}

	public void setChunked(boolean chunked) {
		this.httpResponse.setChunked(chunked);
	}

	public boolean isKeepAlive() {
		boolean http10 = this.httpResponse.getProtocolVersion().equals(HttpVersion.HTTP_1_0);
		String connection = this.httpResponse.getHeader(HttpHeaders.Names.CONNECTION);
		return HttpHeaders.Values.CLOSE.equalsIgnoreCase(connection) || (http10 && !HttpHeaders.Values.KEEP_ALIVE.equalsIgnoreCase(connection));
	}

	public HttpResponseStatus getStatus() {
		return this.httpResponse.getStatus();
	}

	public void setStatus(HttpResponseStatus status) {
		this.httpResponse.setStatus(status);
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
		this.includeCharset = true;
		this.charset = charset;
	}

	/**
	 * 
	 * @return
	 */
	public String getContentType() {

		if (StringUtils.isNotBlank(this.contentType) && StringUtils.isNotBlank(getCharset()) && includeCharset) {
			return new StringBuilder().append(this.contentType).append(";charset=").append(getCharset()).toString();
		}

		return this.contentType;
	}

	/**
	 * 
	 * @param contentType
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * 
	 * @param code
	 */
	public abstract void sendError(int code);

	/**
	 * 
	 * @param location
	 */
	public abstract void sendRedirect(String location);
}
