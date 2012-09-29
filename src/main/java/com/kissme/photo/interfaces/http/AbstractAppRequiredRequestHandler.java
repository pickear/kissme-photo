package com.kissme.photo.interfaces.http;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Preconditions;
import com.kissme.photo.application.app.AppService;
import com.kissme.photo.domain.app.App;
import com.kissme.photo.infrastructure.http.Request;
import com.kissme.photo.infrastructure.http.Response;
import com.kissme.photo.infrastructure.util.ExceptionUtils;
import com.kissme.photo.infrastructure.util.SignatureUtils;
import com.kissme.photo.interfaces.http.exception.BadRequestException;
import com.kissme.photo.interfaces.http.support.AbstractJsonpRequestHandler;

/**
 * 
 * @author loudyn
 * 
 */
public abstract class AbstractAppRequiredRequestHandler extends AbstractJsonpRequestHandler {
	private AppService appService;

	protected AbstractAppRequiredRequestHandler(AppService appService) {
		Preconditions.checkNotNull(appService);
		this.appService = appService;
	}

	/**
	 * 
	 * @return
	 */
	protected AppService getAppService() {
		return appService;
	}

	@Override
	protected final String doHandleRequest(Request request, Response response) {
		String appKey = request.getParameter("appkey");
		if (StringUtils.isBlank(appKey)) {
			throw new BadRequestException();
		}

		App app = appService.getByAppKey(appKey);
		if (null == app) {
			throw ExceptionUtils.oneThrow("Bad app key!");
		}

		if (app.isExpire()) {
			throw ExceptionUtils.oneThrow("app key is expire!");
		}

		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		SignatureUtils.verify(signature, timestamp, app.getAppSecret());
		return doHandleAppRequest(app, request, response);
	}

	/**
	 * 
	 * @param app
	 * @param request
	 * @param response
	 * @return
	 */
	protected abstract String doHandleAppRequest(App app, Request request, Response response);
}
