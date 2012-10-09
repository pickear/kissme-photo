package com.kissme.photo.interfaces.http.admin;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.jboss.netty.handler.codec.http.HttpMethod;

import com.google.inject.Inject;
import com.kissme.photo.application.photo.PhotoService;
import com.kissme.photo.domain.Page;
import com.kissme.photo.domain.photo.Photo;
import com.kissme.photo.infrastructure.http.Request;
import com.kissme.photo.infrastructure.http.Response;
import com.kissme.photo.infrastructure.util.JsonUtils;

/**
 * 
 * @author loudyn
 * 
 */
public class AdminPhotoQueryRequestHandler extends AbstractAdminRequestHandler {

	private PhotoService photoService;

	@Inject
	public AdminPhotoQueryRequestHandler(PhotoService photoService) {
		this.photoService = photoService;
	}

	@Override
	public String getMapping() {
		return "/admin/photo/";
	}

	@Override
	public HttpMethod[] getMappingMethods() {
		return new HttpMethod[] { HttpMethod.GET };
	}

	@SuppressWarnings("unchecked")
	@Override
	protected String doHandleAdminRequest(Request request, Response response) {
		Page<Photo> page = JsonUtils.newfor(request.params(), Page.class);
		String id = request.param("id");
		if (StringUtils.isNotBlank(id)) {
			page.getParams().put("_id", new ObjectId(id));
		}

		String galleryId = request.param("galleryId");
		if (StringUtils.isNotBlank(galleryId)) {
			page.getParams().put("metadata.gallery.$id", new ObjectId(galleryId));
		}

		page = photoService.findPage(page);
		return JsonUtils.toJsonString(page);
	}

}
