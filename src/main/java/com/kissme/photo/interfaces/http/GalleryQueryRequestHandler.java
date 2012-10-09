package com.kissme.photo.interfaces.http;

import org.jboss.netty.handler.codec.http.HttpMethod;

import com.google.inject.Inject;
import com.kissme.photo.application.app.AppService;
import com.kissme.photo.application.gallery.GalleryService;
import com.kissme.photo.domain.Page;
import com.kissme.photo.domain.app.App;
import com.kissme.photo.domain.gallery.Gallery;
import com.kissme.photo.infrastructure.http.Request;
import com.kissme.photo.infrastructure.http.Response;
import com.kissme.photo.infrastructure.util.JsonUtils;

/**
 * 
 * @author loudyn
 * 
 */
public class GalleryQueryRequestHandler extends AbstractAppRequiredRequestHandler {

	private GalleryService galleryService;

	@Inject
	public GalleryQueryRequestHandler(AppService appService, GalleryService galleryService) {
		super(appService);
		this.galleryService = galleryService;
	}

	@Override
	public String getMapping() {
		return "/gallery/";
	}

	@Override
	public HttpMethod[] getMappingMethods() {
		return new HttpMethod[] { HttpMethod.GET };
	}

	@SuppressWarnings("unchecked")
	@Override
	protected String doHandleAppRequest(App app, Request request, Response response) {
		Page<Gallery> page = JsonUtils.newfor(request.params(), Page.class);
		page = galleryService.findPageByApp(app.getId(), page);
		return JsonUtils.toJsonString(page);
	}
}
