package com.kissme.photo.interfaces.http.admin;

import org.jboss.netty.handler.codec.http.HttpMethod;

import com.google.inject.Inject;
import com.kissme.photo.application.app.AppService;
import com.kissme.photo.infrastructure.http.Request;
import com.kissme.photo.infrastructure.http.Response;

/**
 * 
 * @author loudyn
 * 
 */
public class AdminAppDeleteRequestHandler extends AbstractAdminRequestHandler {

	private AppService appService;

	@Inject
	public AdminAppDeleteRequestHandler(AppService appService) {
		this.appService = appService;
	}

	@Override
	public String getMapping() {
		return "/admin/app/{id}/";
	}

	@Override
	public HttpMethod[] getMappingMethods() {
		return new HttpMethod[] { HttpMethod.DELETE };
	}

	@Override
	protected String doHandleAdminRequest(Request request, Response response) {
		try {

			String id = request.pathVariable("id");
			appService.delete(id);
			return "{\"status\":\"ok\"}";
		} catch (Exception e) {
			return String.format("{\"status\":\"fail\",\"msg\":\"%s\"}", e.getMessage());
		}
	}
}
