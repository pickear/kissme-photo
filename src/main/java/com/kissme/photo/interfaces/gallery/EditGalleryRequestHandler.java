package com.kissme.photo.interfaces.gallery;

import org.jboss.netty.handler.codec.http.HttpMethod;

import com.google.inject.Inject;
import com.kissme.photo.application.GalleryService;
import com.kissme.photo.domain.gallery.Gallery;
import com.kissme.photo.infrastructure.Jsons;
import com.kissme.photo.infrastructure.http.Request;
import com.kissme.photo.infrastructure.http.Response;
import com.kissme.photo.interfaces.AbstractJsonpRequestHandler;
import com.kissme.photo.interfaces.exception.ResourceNotFoundException;

/**
 * 
 * @author loudyn
 * 
 */
public class EditGalleryRequestHandler extends AbstractJsonpRequestHandler {

	private GalleryService galleryService;

	@Inject
	public EditGalleryRequestHandler(GalleryService galleryService) {
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
	protected String doHandleRequest(Request request, Response response) {
		String id = request.getPathVariables().get("id");
		Gallery entity = galleryService.get(id);

		if (null == entity) {
			throw new ResourceNotFoundException();
		}

		galleryService.save(entity.setName(request.getParameter("name")));
		return Jsons.toJsonString(entity);
	}

}
