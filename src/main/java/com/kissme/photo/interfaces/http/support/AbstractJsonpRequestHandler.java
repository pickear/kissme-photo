package com.kissme.photo.interfaces.http.support;

import java.nio.charset.Charset;

import org.apache.commons.lang.StringUtils;
import org.jboss.netty.buffer.ChannelBuffers;

import com.kissme.photo.infrastructure.http.Request;
import com.kissme.photo.infrastructure.http.RequestHandler;
import com.kissme.photo.infrastructure.http.Response;
import com.kissme.photo.interfaces.http.exception.BadRequestException;

/**
 * 
 * @author loudyn
 * 
 */
public abstract class AbstractJsonpRequestHandler implements RequestHandler {

	protected final static String CALLBACK_PARAM_NAME = "callback";

	@Override
	public final void handleRequest(Request request, Response response) {
		String callback = request.getParameter(CALLBACK_PARAM_NAME);
		if (StringUtils.isBlank(callback)) {
			throw new BadRequestException();
		}

		String contentString = doHandleRequest(request, response);
		String jsonpString = String.format("%s(%s)", callback, contentString);

		response.setContentType("application/json");
		response.setContent(ChannelBuffers.copiedBuffer(jsonpString, Charset.forName(request.getCharset())));
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	protected abstract String doHandleRequest(Request request, Response response);
}
