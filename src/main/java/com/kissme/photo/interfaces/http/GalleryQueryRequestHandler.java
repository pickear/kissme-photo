package com.kissme.photo.interfaces.http;

import org.apache.commons.lang.StringUtils;
import org.jboss.netty.handler.codec.http.HttpMethod;

import com.google.inject.Inject;
import com.kissme.photo.application.gallery.GalleryService;
import com.kissme.photo.domain.Page;
import com.kissme.photo.domain.gallery.Gallery;
import com.kissme.photo.infrastructure.http.Request;
import com.kissme.photo.infrastructure.http.Response;
import com.kissme.photo.infrastructure.util.JsonUtils;
import com.kissme.photo.interfaces.http.exception.BadRequestException;
import com.kissme.photo.interfaces.http.support.AbstractJsonpRequestHandler;

/**
 * 
 * @author loudyn
 * 
 */
public class GalleryQueryRequestHandler extends AbstractJsonpRequestHandler {

	private GalleryService galleryService;

	@Inject
	public GalleryQueryRequestHandler(GalleryService galleryService) {
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

		Page<Gallery> page = JsonUtils.newfor(request.getParameterMap(), Page.class);
		page = galleryService.findPageByApp(app, page);
		return JsonUtils.toJsonString(page);
	}
}
