package com.kissme.photo.interfaces.photo;

import org.jboss.netty.handler.codec.http.HttpMethod;

import com.google.inject.Inject;
import com.kissme.photo.application.PhotoService;
import com.kissme.photo.infrastructure.http.Request;
import com.kissme.photo.infrastructure.http.Response;
import com.kissme.photo.interfaces.AbstractJsonpRequestHandler;

/**
 * 
 * @author loudyn
 * 
 */
public class DeletePhotoRequestHandler extends AbstractJsonpRequestHandler {

	private PhotoService galleryPhotoService;

	@Inject
	public DeletePhotoRequestHandler(PhotoService galleryPhotoService) {
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
