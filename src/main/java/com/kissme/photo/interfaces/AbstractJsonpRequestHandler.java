package com.kissme.photo.interfaces;

import java.nio.charset.Charset;

import org.apache.commons.lang.StringUtils;
import org.jboss.netty.buffer.ChannelBuffers;

import com.kissme.photo.infrastructure.http.Request;
import com.kissme.photo.infrastructure.http.RequestHandler;
import com.kissme.photo.infrastructure.http.Response;
import com.kissme.photo.interfaces.exception.BadRequestException;

/**
 * 
 * @author loudyn
 * 
 */
public abstract class AbstractJsonpRequestHandler implements RequestHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.youboy.photo.infrastructure.http.RequestHandler#handleRequest(com.youboy.photo.infrastructure.http.Request,
	 * com.youboy.photo.infrastructure.http.Response)
	 */
	@Override
	public final void handleRequest(Request request, Response response) {
		String callback = request.getParameter("callback");
		if (StringUtils.isBlank(callback)) {
			throw new BadRequestException();
		}

		String contentString = doHandleRequest(request, response);
		String callbackString = String.format("%s(%s)", callback, contentString);
		
		response.setContentType("application/json");
		response.setContent(ChannelBuffers.copiedBuffer(callbackString, Charset.forName(request.getCharset())));
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	protected abstract String doHandleRequest(Request request, Response response);
}
