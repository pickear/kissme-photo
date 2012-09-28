package com.kissme.photo.interfaces.http;

import org.jboss.netty.handler.codec.http.HttpMethod;

import com.google.inject.Inject;
import com.kissme.photo.application.app.AppService;
import com.kissme.photo.domain.app.App;
import com.kissme.photo.infrastructure.http.Request;
import com.kissme.photo.infrastructure.http.Response;
import com.kissme.photo.infrastructure.util.JsonUtils;
import com.kissme.photo.interfaces.http.exception.ResourceNotFoundException;
import com.kissme.photo.interfaces.http.support.AbstractJsonpRequestHandler;

/**
 * 
 * @author loudyn
 * 
 */
public class AppEditRequestHandler extends AbstractJsonpRequestHandler {
	private AppService appService;

	@Inject
	public AppEditRequestHandler(AppService appService) {
		this.appService = appService;
	}

	public String getMapping() {
		return "/app/{id}/";
	}

	public HttpMethod[] getMappingMethods() {
		return new HttpMethod[] { HttpMethod.PUT };
	}

	@Override
	protected String doHandleRequest(Request request, Response response) {
		String id = request.getPathVariables().get("id");
		App entity = appService.get(id);
		if (null == entity) {
			throw new ResourceNotFoundException();
		}

		entity.setName(request.getParameter("email")).setPhone(request.getParameter("phone"));
		appService.update(entity);
		return JsonUtils.toJsonString(entity);
	}
}
