package com.kissme.photo.interfaces.http.admin;

import org.apache.commons.lang.StringUtils;

import com.kissme.photo.domain.Admin;
import com.kissme.photo.infrastructure.http.Request;
import com.kissme.photo.infrastructure.http.Response;
import com.kissme.photo.infrastructure.util.SignatureUtils;
import com.kissme.photo.interfaces.http.exception.BadRequestException;
import com.kissme.photo.interfaces.http.support.AbstractJsonpRequestHandler;

/**
 * 
 * @author loudyn
 * 
 */
public abstract class AbstractAdminRequestHandler extends AbstractJsonpRequestHandler {

	@Override
	protected final String doHandleRequest(Request request, Response response) {
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		if (StringUtils.isBlank(signature) || StringUtils.isBlank(timestamp)) {
			throw new BadRequestException();
		}

		Admin admin = Admin.get();
		SignatureUtils.verify(signature, timestamp, admin.getUsername() + "-" + admin.getPassword());
		return doHandleAdminRequest(request, response);
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	protected abstract String doHandleAdminRequest(Request request, Response response);
}
