package com.kissme.photo.infrastructure.http.multipart.commons;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.kissme.photo.infrastructure.http.Request;

/**
 * 
 * @author loudyn
 * 
 */
public class CommonsRequestContentStreamAdpater extends InputStream {

	private final InputStream in;

	public CommonsRequestContentStreamAdpater(Request request) {
		in = new ByteArrayInputStream(request.getContent().array());
	}

	@Override
	public int read() throws IOException {
		return in.read();
	}

	@Override
	public int available() throws IOException {
		return in.available();
	}
}
