package com.kissme.photo.interfaces.http.interceptor;

import org.apache.commons.lang.StringUtils;
import org.jboss.netty.handler.codec.http.HttpMethod;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.kissme.photo.application.timestamp.TimestampService;
import com.kissme.photo.infrastructure.http.Request;
import com.kissme.photo.infrastructure.http.RequestHandlerChain;
import com.kissme.photo.infrastructure.http.RequestInterceptor;
import com.kissme.photo.infrastructure.http.Response;
import com.kissme.photo.infrastructure.http.annotation.ResponseStatus;

/**
 * 
 * @author loudyn
 * 
 */
public class TimestampVerifyRequestInterceptor implements RequestInterceptor {

	private TimestampService timestampService;

	@Inject
	public TimestampVerifyRequestInterceptor(TimestampService timestampService) {
		Preconditions.checkNotNull(timestampService);
		this.timestampService = timestampService;
	}

	/**
	 * 
	 * @author loudyn
	 * 
	 */
	@SuppressWarnings("serial")
	@ResponseStatus(status = 403, reason = "Invalid timestamp")
	public class InvalidTimestampException extends RuntimeException {}

	private final static String TIMESTAMP_PARAM_NAME = "timestamp";
	private final static long TEN_MINUTE_AS_MILLSECONDS = 10 * 60 * 1000;

	public int getOrder() {
		return 0;
	}

	public void intercept(Request request, Response response, RequestHandlerChain handlerChain) {

 		if (shouldIntercept(request)) {
			String timestamp = getTimestampString(request);
			verifyTimestamp(timestamp);
			preventReplayAttack(timestamp);
		}

		handlerChain.handle(request, response);
	}

	private boolean shouldIntercept(Request request) {

		if (isGetPhotoRequest(request)) {
			return false;
		}
		return true;
	}

	private boolean isGetPhotoRequest(Request request) {
		return request.getPath().startsWith("/photo/") && request.getMethod() == HttpMethod.GET;
	}

	private void verifyTimestamp(String timestamp) {

		if (!isAcceptableTimestamp(timestamp)) {
			throw new InvalidTimestampException();
		}

		if (isExistsTimestamp(timestamp)) {
			throw new InvalidTimestampException();
		}
	}

	private String getTimestampString(Request request) {
		return request.getParameter(TIMESTAMP_PARAM_NAME);
	}

	private boolean isAcceptableTimestamp(String timestampAsString) {
		long timestamp = castAsLong(timestampAsString);
		return Math.abs(now() - timestamp) < TEN_MINUTE_AS_MILLSECONDS;
	}

	private long castAsLong(String timestampAsString) {

		if (StringUtils.isBlank(timestampAsString)) {
			return 0;
		}

		try {

			return Long.parseLong(timestampAsString);
		} catch (Exception e) {
			return 0;
		}
	}

	private long now() {
		return System.currentTimeMillis();
	}

	private boolean isExistsTimestamp(String timestamp) {
		return timestampService.existsTimestamp(timestamp);
	}

	private void preventReplayAttack(String timestamp) {
		timestampService.save(timestamp);
	}

}
