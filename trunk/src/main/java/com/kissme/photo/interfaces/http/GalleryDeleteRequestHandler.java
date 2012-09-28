package com.kissme.photo.interfaces.http;

import org.jboss.netty.handler.codec.http.HttpMethod;

import com.google.inject.Inject;
import com.kissme.photo.application.gallery.GalleryService;
import com.kissme.photo.infrastructure.http.Request;
import com.kissme.photo.infrastructure.http.Response;
import com.kissme.photo.interfaces.http.support.AbstractJsonpRequestHandler;

/**
 * 
 * @author loudyn
 * 
 */
public class GalleryDeleteRequestHandler extends AbstractJsonpRequestHandler {

	private GalleryService galleryService;

	@Inject
	public GalleryDeleteRequestHandler(GalleryService galleryService) {
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
	protected String doHandleRequest(Request request, Response response) {
		String id = request.getPathVariables().get("id");
		galleryService.delete(id);
		return "{\"status\":\"ok\"}";
	}

}
