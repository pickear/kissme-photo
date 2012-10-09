package com.kissme.photo.interfaces.http.admin;

import org.jboss.netty.handler.codec.http.HttpMethod;

import com.google.inject.Inject;
import com.kissme.photo.application.app.AppService;
import com.kissme.photo.domain.app.App;
import com.kissme.photo.infrastructure.http.Request;
import com.kissme.photo.infrastructure.http.Response;
import com.kissme.photo.infrastructure.util.JsonUtils;

/**
 * 
 * @author loudyn
 * 
 */
public class AdminAppCreateRequestHandler extends AbstractAdminRequestHandler {

	private AppService appService;

	/**
	 * 
	 * @param appService
	 */
	@Inject
	public AdminAppCreateRequestHandler(AppService appService) {
		this.appService = appService;
	}

	@Override
	public String getMapping() {
		return "/admin/app/";
	}

	@Override
	public HttpMethod[] getMappingMethods() {
		return new HttpMethod[] { HttpMethod.POST };
	}

	@Override
	protected String doHandleAdminRequest(Request request, Response response) {
		App entity = JsonUtils.newfor(request.params(), App.class);
		appService.save(entity.createKeys().expireAfterYears(1));
		return JsonUtils.toJsonString(entity);
	}
}
