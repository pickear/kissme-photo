package com.kissme.photo.interfaces.gallery;

import org.apache.commons.lang.StringUtils;
import org.jboss.netty.handler.codec.http.HttpMethod;

import com.google.inject.Inject;
import com.kissme.photo.application.GalleryService;
import com.kissme.photo.domain.gallery.Gallery;
import com.kissme.photo.infrastructure.Jsons;
import com.kissme.photo.infrastructure.Page;
import com.kissme.photo.infrastructure.http.Request;
import com.kissme.photo.infrastructure.http.Response;
import com.kissme.photo.interfaces.AbstractJsonpRequestHandler;
import com.kissme.photo.interfaces.exception.BadRequestException;

/**
 * 
 * @author loudyn
 * 
 */
public class QueryGalleryRequestHandler extends AbstractJsonpRequestHandler {

	private GalleryService galleryService;

	@Inject
	public QueryGalleryRequestHandler(GalleryService galleryService) {
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
	protected String doHandleRequest(Request request, Response response) {
		String app = request.getParameter("app");
		if (StringUtils.isBlank(app)) {
			throw new BadRequestException();
		}

		Page<Gallery> page = Jsons.newfor(request.getParameterMap(), Page.class);
		page = galleryService.findPageByApp(app, page);
		return Jsons.toJsonString(page);
	}
}
