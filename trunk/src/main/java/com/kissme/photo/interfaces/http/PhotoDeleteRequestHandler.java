package com.kissme.photo.interfaces.http;

import org.jboss.netty.handler.codec.http.HttpMethod;

import com.google.inject.Inject;
import com.kissme.photo.application.app.AppService;
import com.kissme.photo.application.photo.PhotoService;
import com.kissme.photo.domain.app.App;
import com.kissme.photo.infrastructure.http.Request;
import com.kissme.photo.infrastructure.http.Response;

/**
 * 
 * @author loudyn
 * 
 */
public class PhotoDeleteRequestHandler extends AbstractAppRequiredRequestHandler {

	private PhotoService photoService;

	@Inject
	public PhotoDeleteRequestHandler(AppService appService, PhotoService photoService) {
		super(appService);
		this.photoService = photoService;
	}

	@Override
	public String getMapping() {
		return "/photo/{id}/";
	}

	@Override
	public HttpMethod[] getMappingMethods() {
		return new HttpMethod[] { HttpMethod.DELETE };
	}

	@Override
	protected String doHandleAppRequest(App app, Request request, Response response) {
		String id = request.getPathVariables().get("id");
		photoService.deleteByAppAndId(app.getId(), id);
		return "{\"status\":\"ok\"}";
	}
}
