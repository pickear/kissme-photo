package com.kissme.photo.interfaces.http;

import org.jboss.netty.handler.codec.http.HttpMethod;

import com.google.inject.Inject;
import com.kissme.photo.application.app.AppService;
import com.kissme.photo.domain.app.App;
import com.kissme.photo.infrastructure.http.Request;
import com.kissme.photo.infrastructure.http.Response;
import com.kissme.photo.infrastructure.util.JsonUtils;
import com.kissme.photo.interfaces.http.support.AbstractJsonpRequestHandler;

/**
 * 
 * @author loudyn
 * 
 */
public class AppCreateRequestHandler extends AbstractJsonpRequestHandler {
	private AppService appService;

	@Inject
	public AppCreateRequestHandler(AppService appService) {
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

		App entity = JsonUtils.newfor(request.getParameterMap(), App.class);
		appService.save(entity.createKeys().expireAfterYears(1));
		return JsonUtils.toJsonString(entity);
	}
}
