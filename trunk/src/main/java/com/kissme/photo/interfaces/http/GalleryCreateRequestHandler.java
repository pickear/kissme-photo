package com.kissme.photo.interfaces.http;

import org.jboss.netty.handler.codec.http.HttpMethod;

import com.google.inject.Inject;
import com.kissme.photo.application.app.AppService;
import com.kissme.photo.application.gallery.GalleryService;
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
public class GalleryCreateRequestHandler extends AbstractAppRequiredRequestHandler {

	private GalleryService galleryService;

	@Inject
	public GalleryCreateRequestHandler(AppService appService, GalleryService galleryService) {
		super(appService);
		this.galleryService = galleryService;
	}

	public String getMapping() {
		return "/gallery/";
	}

	public HttpMethod[] getMappingMethods() {
		return new HttpMethod[] { HttpMethod.POST };
	}

	@Override
	protected String doHandleAppRequest(App app, Request request, Response response) {
		Gallery entity = JsonUtils.newfor(request.params(), Gallery.class);
		galleryService.save(entity.setApp(app));
		return JsonUtils.toJsonString(entity);
	}
}
