package com.kissme.photo.interfaces.app;

import org.jboss.netty.handler.codec.http.HttpMethod;

import com.google.inject.Inject;
import com.kissme.photo.application.AppService;
import com.kissme.photo.infrastructure.http.Request;
import com.kissme.photo.infrastructure.http.Response;
import com.kissme.photo.interfaces.AbstractJsonpRequestHandler;

/**
 * 
 * @author loudyn
 * 
 */
public class DeleteAppRequestHandler extends AbstractJsonpRequestHandler {

	private AppService appService;

	@Inject
	public DeleteAppRequestHandler(AppService appService) {
		this.appService = appService;
	}

	public String getMapping() {
		return "/app/{id}/";
	}

	public HttpMethod[] getMappingMethods() {
		return new HttpMethod[] { HttpMethod.DELETE };
	}

	@Override
	protected String doHandleRequest(Request request, Response response) {
		String id = request.getPathVariables().get("id");
		appService.delete(id);
		return "{\"status\":\"ok\"}";
	}

}
