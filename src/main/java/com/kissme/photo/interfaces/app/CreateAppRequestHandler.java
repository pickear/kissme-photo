package com.kissme.photo.interfaces.app;

import org.jboss.netty.handler.codec.http.HttpMethod;

import com.google.inject.Inject;
import com.kissme.photo.application.AppService;
import com.kissme.photo.domain.app.App;
import com.kissme.photo.infrastructure.Jsons;
import com.kissme.photo.infrastructure.http.Request;
import com.kissme.photo.infrastructure.http.Response;
import com.kissme.photo.interfaces.AbstractJsonpRequestHandler;

/**
 * 
 * @author loudyn
 * 
 */
public class CreateAppRequestHandler extends AbstractJsonpRequestHandler {
	private AppService appService;

	@Inject
	public CreateAppRequestHandler(AppService appService) {
		this.appService = appService;
	}

	public String getMapping() {
		return "/app/";
	}

	public HttpMethod[] getMappingMethods() {
		return new HttpMethod[] { HttpMethod.POST };
	}

	@Override
	protected String doHandleRequest(Request request, Response response) {

		App entity = Jsons.newfor(request.getParameterMap(), App.class);
		appService.save(entity.createKeys().expireAfterYears(1));
		return Jsons.toJsonString(entity);
	}
}
