package com.kissme.photo.infrastructure.http.multipart;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpVersion;

import com.kissme.photo.infrastructure.http.Request;

/**
 * 
 * @author loudyn
 * 
 */
public abstract class MultipartRequest extends Request {

	private Request delegate;

	/**
	 * 
	 * @param delegate
	 */
	protected MultipartRequest(Request delegate) {
		super(delegate);
		this.delegate = delegate;
	}

	@Override
	public String getHeader(String name) {
		return delegate.getHeader(name);
	}

	@Override
	public List<String> getHeaders(String name) {
		return delegate.getHeaders(name);
	}

	@Override
	public List<Entry<String, String>> getHeaders() {
		return delegate.getHeaders();
	}

	@Override
	public boolean containsHeader(String name) {
		return delegate.containsHeader(name);
	}

	@Override
	public Set<String> getHeaderNames() {
		return delegate.getHeaderNames();
	}

	@Override
	public HttpVersion getProtocolVersion() {
		return delegate.getProtocolVersion();
	}

	@Override
	public void setProtocolVersion(HttpVersion version) {
		delegate.setProtocolVersion(version);
	}

	@Override
	public ChannelBuffer getContent() {
		return delegate.getContent();
	}

	@Override
	public void setContent(ChannelBuffer content) {
		delegate.setContent(content);
	}

	@Override
	public void addHeader(String name, Object value) {
		delegate.addHeader(name, value);
	}

	@Override
	public void setHeader(String name, Object value) {
		delegate.setHeader(name, value);
	}

	@Override
	public void setHeader(String name, Iterable<?> values) {
		delegate.setHeader(name, values);
	}

	@Override
	public void removeHeader(String name) {
		delegate.removeHeader(name);
	}

	@Override
	public void clearHeaders() {
		delegate.clearHeaders();
	}

	@Override
	@Deprecated
	public long getContentLength() {
		return delegate.getContentLength();
	}

	@Override
	@Deprecated
	public long getContentLength(long defaultValue) {
		return delegate.getContentLength(defaultValue);
	}

	@Override
	public boolean isChunked() {
		return delegate.isChunked();
	}

	@Override
	public void setChunked(boolean chunked) {
		delegate.setChunked(chunked);
	}

	@Override
	public boolean isKeepAlive() {
		return delegate.isKeepAlive();
	}

	@Override
	public HttpMethod getMethod() {
		return delegate.getMethod();
	}

	@Override
	public void setMethod(HttpMethod method) {
		delegate.setMethod(method);
	}

	@Override
	public String getUri() {
		return delegate.getUri();
	}

	@Override
	public void setUri(String uri) {
		delegate.setUri(uri);
	}

	@Override
	public String getPath() {
		return delegate.getPath();
	}

	@Override
	public String getCharset() {
		return delegate.getCharset();
	}

	@Override
	public void setCharset(String charset) {
		delegate.setCharset(charset);
	}

	/**
	 * 
	 * @return
	 */
	public abstract Iterator<String> getFileFieldNames();

	/**
	 * 
	 * @param fieldName
	 * @return
	 */
	public abstract MultipartRequestFile getFile(String fieldName);

	/**
	 * 
	 * @return
	 */
	public abstract Map<String, MultipartRequestFile> getFileMap();
}
