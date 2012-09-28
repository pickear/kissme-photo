package com.kissme.photo.interfaces.http;

import org.apache.commons.lang.StringUtils;
import org.jboss.netty.handler.codec.http.HttpMethod;

import com.google.inject.Inject;
import com.kissme.photo.application.app.AppService;
import com.kissme.photo.application.gallery.GalleryService;
import com.kissme.photo.domain.app.App;
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
public class GalleryCreateRequestHandler extends AbstractJsonpRequestHandler {

	private AppService appService;
	private GalleryService galleryService;

	@Inject
	public GalleryCreateRequestHandler(AppService appService, GalleryService galleryService) {
		this.appService = appService;
		this.galleryService = galleryService;
	}

	public String getMapping() {
		return "/gallery/";
	}

	public HttpMethod[] getMappingMethods() {
		return new HttpMethod[] { HttpMethod.POST };
	}

	@Override
	protected String doHandleRequest(Request request, Response response) {

		String appId = request.getParameter("app");
		if (StringUtils.isBlank(appId)) {
			throw new BadRequestException();
		}
		
		App app = appService.get(appId);
		if (null == app) {
			throw new BadRequestException();
		}

		Gallery entity = JsonUtils.newfor(request.getParameterMap(), Gallery.class);
		galleryService.save(entity.setApp(app));
		return JsonUtils.toJsonString(entity);
	}
}
