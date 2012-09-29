package com.kissme.photo.interfaces.http;

import org.apache.commons.lang.StringUtils;
import org.jboss.netty.handler.codec.http.HttpMethod;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.kissme.photo.application.app.AppService;
import com.kissme.photo.application.photo.PhotoService;
import com.kissme.photo.domain.Page;
import com.kissme.photo.domain.app.App;
import com.kissme.photo.domain.photo.Photo;
import com.kissme.photo.infrastructure.http.Request;
import com.kissme.photo.infrastructure.http.Response;
import com.kissme.photo.infrastructure.util.JsonUtils;
import com.kissme.photo.interfaces.http.exception.BadRequestException;

/**
 * 
 * @author loudyn
 * 
 */
public class PhotoQueryRequestHandler extends AbstractAppRequiredRequestHandler {

	private PhotoService galleryPhotoService;

	@Inject
	public PhotoQueryRequestHandler(AppService appService, PhotoService galleryPhotoService) {
		super(appService);
		this.galleryPhotoService = galleryPhotoService;
	}

	@Override
	public String getMapping() {
		return "/photo/";
	}

	@Override
	public HttpMethod[] getMappingMethods() {
		return new HttpMethod[] { HttpMethod.GET };
	}

	@SuppressWarnings("unchecked")
	@Override
	protected String doHandleAppRequest(App app, Request request, Response response) {
		String galleryId = request.getParameter("gallery");
		if (StringUtils.isBlank(galleryId)) {
			throw new BadRequestException();
		}

		Page<Photo> page = JsonUtils.newfor(request.getParameterMap(), Page.class);
		String query = request.getParameter("q");
		if (StringUtils.isNotBlank(query)) {
			page.getParams().put("filename", ImmutableMap.of("$regex", ".*?" + query + ".*"));
		}

		page = galleryPhotoService.findPageByAppAndGallery(app.getId(), galleryId, page);
		return JsonUtils.toJsonString(page);
	}
}
