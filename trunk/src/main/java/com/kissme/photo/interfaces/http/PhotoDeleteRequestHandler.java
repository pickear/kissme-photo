package com.kissme.photo.interfaces.http;

import org.jboss.netty.handler.codec.http.HttpMethod;

import com.google.inject.Inject;
import com.kissme.photo.application.photo.PhotoService;
import com.kissme.photo.infrastructure.http.Request;
import com.kissme.photo.infrastructure.http.Response;
import com.kissme.photo.interfaces.http.support.AbstractJsonpRequestHandler;

/**
 * 
 * @author loudyn
 * 
 */
public class PhotoDeleteRequestHandler extends AbstractJsonpRequestHandler {

	private PhotoService galleryPhotoService;

	@Inject
	public PhotoDeleteRequestHandler(PhotoService galleryPhotoService) {
		this.galleryPhotoService = galleryPhotoService;
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
	protected String doHandleRequest(Request request, Response response) {
		String id = request.getPathVariables().get("id");
		galleryPhotoService.delete(id);
		return "{\"status\":\"ok\"}";
	}

}
