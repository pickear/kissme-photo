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
	public String path() {
		return delegate.path();
	}

	@Override
	public String charset() {
		return delegate.charset();
	}

	@Override
	public void charset(String charset) {
		delegate.charset(charset);
	}

	public String param(String name) {
		return delegate.param(name);
	}

	public int paramAsInt(String name) {
		return delegate.paramAsInt(name);
	}

	public float paramAsFloat(String name) {
		return delegate.paramAsFloat(name);
	}

	public long paramAsLong(String name) {
		return delegate.paramAsLong(name);
	}

	public double paramAsDouble(String name) {
		return delegate.paramAsDouble(name);
	}

	public boolean paramAsBoolean(String name) {
		return delegate.paramAsBoolean(name);
	}

	public String pathVariable(String name) {
		return delegate.pathVariable(name);
	}

	public int pathVariableAsInt(String name) {
		return delegate.pathVariableAsInt(name);
	}

	public float pathVariableAsFloat(String name) {
		return delegate.pathVariableAsFloat(name);
	}

	public long pathVariableAsLong(String name) {
		return delegate.pathVariableAsLong(name);
	}

	public double pathVariableAsDouble(String name) {
		return delegate.pathVariableAsDouble(name);
	}

	public boolean pathVariableAsBoolean(String name) {
		return delegate.pathVariableAsBoolean(name);
	}

	public Map<String, String> pathVariables() {
		return delegate.pathVariables();
	}

	/**
	 * 
	 * @return
	 */
	public abstract Iterator<String> fileFields();

	/**
	 * 
	 * @param field
	 * @return
	 */
	public abstract MultipartRequestFile file(String field);

	/**
	 * 
	 * @return
	 */
	public abstract Map<String, MultipartRequestFile> files();
}
