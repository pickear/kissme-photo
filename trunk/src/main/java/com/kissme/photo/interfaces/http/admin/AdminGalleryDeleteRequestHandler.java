package com.kissme.photo.interfaces.http.admin;

import org.jboss.netty.handler.codec.http.HttpMethod;

import com.google.inject.Inject;
import com.kissme.photo.application.gallery.GalleryService;
import com.kissme.photo.infrastructure.http.Request;
import com.kissme.photo.infrastructure.http.Response;

/**
 * 
 * @author loudyn
 * 
 */
public class AdminGalleryDeleteRequestHandler extends AbstractAdminRequestHandler {

	private GalleryService galleryService;

	@Inject
	public AdminGalleryDeleteRequestHandler(GalleryService galleryService) {
		this.galleryService = galleryService;
	}

	@Override
	public String getMapping() {
		return "/admin/gallery/{id}/";
	}

	@Override
	public HttpMethod[] getMappingMethods() {
		return new HttpMethod[] { HttpMethod.DELETE };
	}

	@Override
	protected String doHandleAdminRequest(Request request, Response response) {
		try {

			String id = request.pathVariable("id");
			galleryService.delete(id);
			return "{\"status\":\"ok\"}";
		} catch (Exception e) {
			return String.format("{\"status\":\"fail\",\"msg\":\"%s\"}", e.getMessage());
		}
	}
}
