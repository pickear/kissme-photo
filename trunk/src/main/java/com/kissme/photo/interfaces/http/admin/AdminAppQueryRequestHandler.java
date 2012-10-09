package com.kissme.photo.interfaces.http.admin;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.jboss.netty.handler.codec.http.HttpMethod;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.kissme.photo.application.app.AppService;
import com.kissme.photo.domain.Page;
import com.kissme.photo.domain.app.App;
import com.kissme.photo.infrastructure.http.Request;
import com.kissme.photo.infrastructure.http.Response;
import com.kissme.photo.infrastructure.util.JsonUtils;

/**
 * 
 * @author loudyn
 * 
 */
public class AdminAppQueryRequestHandler extends AbstractAdminRequestHandler {

	private AppService appService;

	@Inject
	public AdminAppQueryRequestHandler(AppService appService) {
		this.appService = appService;
	}

	@Override
	public String getMapping() {
		return "/admin/app/";
	}

	@Override
	public HttpMethod[] getMappingMethods() {
		return new HttpMethod[] { HttpMethod.GET };
	}

	@SuppressWarnings("unchecked")
	@Override
	protected String doHandleAdminRequest(Request request, Response response) {
		Page<App> page = JsonUtils.newfor(request.params(), Page.class);

		String id = request.param("id");
		if (StringUtils.isNotBlank(id)) {
			page.getParams().put("_id", new ObjectId(id));
		}

		String appkey = request.param("appkey");
		if (StringUtils.isNotBlank(appkey)) {
			page.getParams().put("keys.appKey", appkey);
		}

		String name = request.param("name");
		if (StringUtils.isNotBlank(name)) {
			page.getParams().put("name", ImmutableMap.of("$regex", ".*?" + name + ".*"));
		}
		
		page = appService.findPage(page);
		return JsonUtils.toJsonString(page);
	}
}
