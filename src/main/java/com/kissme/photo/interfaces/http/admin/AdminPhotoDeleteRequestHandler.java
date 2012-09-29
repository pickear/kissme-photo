package com.kissme.photo.interfaces.http.admin;

import org.jboss.netty.handler.codec.http.HttpMethod;

import com.google.inject.Inject;
import com.kissme.photo.application.photo.PhotoService;
import com.kissme.photo.infrastructure.http.Request;
import com.kissme.photo.infrastructure.http.Response;

/**
 * 
 * @author loudyn
 * 
 */
public class AdminPhotoDeleteRequestHandler extends AbstractAdminRequestHandler {

	private PhotoService photoService;

	@Inject
	public AdminPhotoDeleteRequestHandler(PhotoService photoService) {
		this.photoService = photoService;
	}

	@Override
	public String getMapping() {
		return "/admin/photo/{id}/";
	}

	@Override
	public HttpMethod[] getMappingMethods() {
		return new HttpMethod[] { HttpMethod.DELETE };
	}

	@Override
	protected String doHandleAdminRequest(Request request, Response response) {
		try {

			String id = request.getPathVariables().get("id");
			photoService.delete(id);
			return "{\"status\":\"ok\"}";
		} catch (Exception e) {
			return String.format("{\"status\":\"fail\",\"msg\":\"%s\"}", e.getMessage());
		}
	}

}
