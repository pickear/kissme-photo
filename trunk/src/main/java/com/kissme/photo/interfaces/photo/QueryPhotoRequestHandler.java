package com.kissme.photo.interfaces.photo;

import org.apache.commons.lang.StringUtils;
import org.jboss.netty.handler.codec.http.HttpMethod;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.kissme.photo.application.PhotoService;
import com.kissme.photo.domain.photo.Photo;
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
public class QueryPhotoRequestHandler extends AbstractJsonpRequestHandler {

	private PhotoService galleryPhotoService;

	@Inject
	public QueryPhotoRequestHandler(PhotoService galleryPhotoService) {
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
	protected String doHandleRequest(Request request, Response response) {
		String galleryId = request.getParameter("gallery");
		if (StringUtils.isBlank(galleryId)) {
			throw new BadRequestException();
		}

		Page<Photo> page = Jsons.newfor(request.getParameterMap(), Page.class);
		String query = request.getParameter("q");
		if (StringUtils.isNotBlank(query)) {
			page.getParams().put("filename", ImmutableMap.of("$regex", ".*?" + query + ".*"));
		}

		page = galleryPhotoService.findPageByGallery(galleryId, page);
		return Jsons.toJsonString(page);
	}
}
