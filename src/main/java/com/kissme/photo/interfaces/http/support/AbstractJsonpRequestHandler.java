package com.kissme.photo.interfaces.http.support;

import java.nio.charset.Charset;

import org.apache.commons.lang.StringUtils;
import org.jboss.netty.buffer.ChannelBuffers;

import com.kissme.photo.infrastructure.http.Request;
import com.kissme.photo.infrastructure.http.RequestHandler;
import com.kissme.photo.infrastructure.http.Response;

/**
 * 
 * @author loudyn
 * 
 */
public abstract class AbstractJsonpRequestHandler implements RequestHandler {

	protected final static String CALLBACK_PARAM_NAME = "callback";

	@Override
	public final void handleRequest(Request request, Response response) {

		String contentString = doHandleRequest(request, response);
		
		String callback = request.param(CALLBACK_PARAM_NAME);
		String jsonString = contentString;
		if (StringUtils.isNotBlank(callback)) {
			jsonString = String.format("%s(%s)", callback, jsonString);
		}

		response.setContentType("application/json");
		response.setContent(ChannelBuffers.copiedBuffer(jsonString, Charset.forName(request.charset())));
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	protected abstract String doHandleRequest(Request request, Response response);
}
