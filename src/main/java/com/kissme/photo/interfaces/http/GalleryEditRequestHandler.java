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
import com.kissme.photo.interfaces.http.exception.ResourceNotFoundException;

/**
 * 
 * @author loudyn
 * 
 */
public class GalleryEditRequestHandler extends AbstractAppRequiredRequestHandler {

	private GalleryService galleryService;

	@Inject
	public GalleryEditRequestHandler(AppService appService, GalleryService galleryService) {
		super(appService);
		this.galleryService = galleryService;
	}

	@Override
	public String getMapping() {
		return "/gallery/{id}/";
	}

	@Override
	public HttpMethod[] getMappingMethods() {
		return new HttpMethod[] { HttpMethod.PUT };
	}

	@Override
	protected String doHandleAppRequest(App app, Request request, Response response) {
		String id = request.pathVariable("id");
		Gallery entity = galleryService.getByAppAndId(app.getId(), id);

		if (null == entity) {
			throw new ResourceNotFoundException();
		}

		galleryService.save(entity.setName(request.param("name")));
		return JsonUtils.toJsonString(entity);
	}
}
