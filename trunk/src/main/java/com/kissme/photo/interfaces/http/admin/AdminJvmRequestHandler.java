package com.kissme.photo.interfaces.http.admin;

import org.jboss.netty.handler.codec.http.HttpMethod;

import com.kissme.photo.infrastructure.http.Request;
import com.kissme.photo.infrastructure.http.Response;
import com.kissme.photo.infrastructure.jvm.Jvm;
import com.kissme.photo.infrastructure.util.JsonUtils;

/**
 * 
 * @author loudyn
 * 
 */
public class AdminJvmRequestHandler extends AbstractAdminRequestHandler {

	@Override
	public String getMapping() {
		return "/admin/vm/";
	}

	@Override
	public HttpMethod[] getMappingMethods() {
		return new HttpMethod[] { HttpMethod.GET };
	}

	@Override
	protected String doHandleAdminRequest(Request request, Response response) {
		return JsonUtils.toJsonString(Jvm.me());
	}

}
