package com.kissme.photo.interfaces.http;

import org.jboss.netty.handler.codec.http.HttpMethod;

import com.google.inject.Inject;
import com.kissme.photo.application.app.AppService;
import com.kissme.photo.application.gallery.GalleryService;
import com.kissme.photo.domain.app.App;
import com.kissme.photo.infrastructure.http.Request;
import com.kissme.photo.infrastructure.http.Response;

/**
 * 
 * @author loudyn
 * 
 */
public class GalleryDeleteRequestHandler extends AbstractAppRequiredRequestHandler {

	private GalleryService galleryService;

	@Inject
	public GalleryDeleteRequestHandler(AppService appService, GalleryService galleryService) {
		super(appService);
		this.galleryService = galleryService;
	}

	@Override
	public String getMapping() {
		return "/gallery/{id}/";
	}

	@Override
	public HttpMethod[] getMappingMethods() {
		return new HttpMethod[] { HttpMethod.DELETE };
	}

	@Override
	protected String doHandleAppRequest(App app, Request request, Response response) {

		try {

			String id = request.pathVariable("id");
			galleryService.deleteByAppAndId(app.getId(), id);
			return "{\"status\":\"ok\"}";
		} catch (Exception e) {
			return String.format("{\"status\":\"fail\",\"msg\":\"%s\"}", e.getMessage());
		}
	}
}
