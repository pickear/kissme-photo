package com.kissme.photo.interfaces.interceptor;

import java.util.Arrays;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMethod;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.kissme.photo.application.AppService;
import com.kissme.photo.domain.app.App;
import com.kissme.photo.infrastructure.Exceptions;
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
public class SignatureVerifyRequestInterceptor implements RequestInterceptor {

	private final AppService appService;

	@Inject
	public SignatureVerifyRequestInterceptor(AppService appService) {
		Preconditions.checkNotNull(appService);
		this.appService = appService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kissme.photo.infrastructure.http.RequestInterceptor#getOrder()
	 */
	public int getOrder() {
		return 1;
	}

	/**
	 * 
	 * @author loudyn
	 * 
	 */
	@SuppressWarnings("serial")
	@ResponseStatus(status = 403, reason = "Invalid signature")
	public class InvalidSignatueException extends RuntimeException {

		public InvalidSignatueException(Exception e) {
			super(e);
		}

		public InvalidSignatueException() {
			super();
		}

	}

	/**
	 * 
	 * @author loudyn
	 * 
	 */
	@SuppressWarnings("serial")
	@ResponseStatus(status = 403, reason = "Invalid appKey")
	public class InvalidAppKeyException extends RuntimeException {

		public InvalidAppKeyException(Exception e) {
			super(e);
		}

		public InvalidAppKeyException() {
			super();
		}

	}

	private final static String MAC_NAME = "HmacSHA1";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kissme.photo.infrastructure.http.RequestInterceptor#intercept(com.kissme.photo.infrastructure.http.Request,
	 * com.kissme.photo.infrastructure.http.Response, com.kissme.photo.infrastructure.http.RequestHandlerChain)
	 */
	public void intercept(Request request, Response response, RequestHandlerChain handlerChain) {
		if (shouldIntercept(request)) {
			verifySignature(request);
		}

		handlerChain.handle(request, response);
	}

	private boolean shouldIntercept(Request request) {
		if (isCreateAppRequest(request)) {
			return false;
		}

		if (isGetPhotoRequest(request)) {
			return false;
		}
		return true;
	}

	private boolean isCreateAppRequest(Request request) {
		return request.getPath().startsWith("/app/") && request.getMethod() == HttpMethod.POST;
	}

	private boolean isGetPhotoRequest(Request request) {
		return request.getPath().startsWith("/photo/") && request.getMethod() == HttpMethod.GET;
	}

	private void verifySignature(Request request) {

		String authorizationHeader = getAuthorizationHeader(request);
		String appKey = getAppKey(authorizationHeader);
		String signature = getSignture(authorizationHeader);
		String signatureBaseString = getSignatureBaseString(request);

		new HMACSHA1Verifier(getSecretKey(appKey)).verify(signatureBaseString, signature);

	}

	private String getAuthorizationHeader(Request request) {
		return request.getHeader(HttpHeaders.Names.AUTHORIZATION);
	}

	private String getAppKey(String signature) {
		return StringUtils.substringBefore(signature, ";");
	}

	private String getSignture(String authorizationHeader) {
		return StringUtils.substringAfter(authorizationHeader, ";");
	}

	private String getSignatureBaseString(Request request) {

		return new StringBuilder().append(request.getMethod().getName())
								  .append("&")
								  .append(request.getUri())
								  .append("&").append(request.getParameter("timestamp"))
								  .toString();
	}

	private SecretKey getSecretKey(String appKey) {
		try {

			Preconditions.checkNotNull(appKey);
			App app = appService.getByAppKey(appKey);
			if (null == app) {
				throw new InvalidAppKeyException();
			}

			SecretKeySpec key = new SecretKeySpec(app.getAppSecret().getBytes("UTF-8"), MAC_NAME);
			return key;
		} catch (Exception e) {
			throw Exceptions.uncheck(e);
		}
	}

	final class HMACSHA1Verifier {
		private final SecretKey key;

		public HMACSHA1Verifier(SecretKey key) {
			Preconditions.checkNotNull(key);
			this.key = key;
		}

		public void verify(String signatureBaseString, String signature) {

			Preconditions.checkNotNull(signatureBaseString);
			Preconditions.checkNotNull(signature);
			try {

				// HmacSHA1 signature baseString , then encode as base64
				// so we must decode the signature as base64
				byte[] signatueBytes = Base64.decodeBase64(signature.getBytes("UTF-8"));
				Mac mac = Mac.getInstance(MAC_NAME);
				mac.init(key);
				byte[] calculateBytes = mac.doFinal(signatureBaseString.getBytes("UTF-8"));
				if (!Arrays.equals(signatueBytes, calculateBytes)) {
					throw new InvalidSignatueException();
				}
			} catch (Exception e) {
				if (e instanceof InvalidSignatueException) {
					throw (InvalidSignatueException) e;
				}

				throw new InvalidSignatueException(e);
			}
		}
	}

}
