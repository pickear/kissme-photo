package com.kissme.photo.interfaces.http.admin;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.jboss.netty.handler.codec.http.HttpMethod;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.kissme.photo.application.gallery.GalleryService;
import com.kissme.photo.domain.Page;
import com.kissme.photo.domain.gallery.Gallery;
import com.kissme.photo.infrastructure.http.Request;
import com.kissme.photo.infrastructure.http.Response;
import com.kissme.photo.infrastructure.util.JsonUtils;

/**
 * 
 * @author loudyn
 * 
 */
public class AdminGalleryQueryRequestHandler extends AbstractAdminRequestHandler {

	private GalleryService galleryService;

	@Inject
	public AdminGalleryQueryRequestHandler(GalleryService galleryService) {
		this.galleryService = galleryService;
	}

	@Override
	public String getMapping() {
		return "/admin/gallery/";
	}

	@Override
	public HttpMethod[] getMappingMethods() {
		return new HttpMethod[] { HttpMethod.GET };
	}

	@Override
	@SuppressWarnings("unchecked")
	protected String doHandleAdminRequest(Request request, Response response) {
		Page<Gallery> page = JsonUtils.newfor(request.params(), Page.class);

		String appId = request.param("appId");
		if (StringUtils.isNotBlank(appId)) {
			page.getParams().put("app.$id", new ObjectId(appId));
		}

		String name = request.param("name");
		if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(appId)) {
			page.getParams().put("name", ImmutableMap.of("$regex", ".*?" + name + ".*"));
		}

		page = galleryService.findPage(page);
		return JsonUtils.toJsonString(page);
	}

}
