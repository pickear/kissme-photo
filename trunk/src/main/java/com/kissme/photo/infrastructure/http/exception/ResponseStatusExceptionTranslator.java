package com.kissme.photo.infrastructure.http.exception;

import com.google.common.base.Preconditions;
import com.kissme.photo.infrastructure.http.annotation.ResponseStatus;

/**
 * 
 * @author loudyn
 * 
 */
public class ResponseStatusExceptionTranslator implements RequestExceptionTranslator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kissme.photo.infrastructure.http.exception.RequestExceptionTranslator#translate(java.lang.Exception)
	 */
	public RequestExceptionCode translate(Exception e) {

		Preconditions.checkNotNull(e);

		ResponseStatus status = e.getClass().getAnnotation(ResponseStatus.class);
		if (null == status) {
			return new RequestExceptionCode(500, e.getMessage());
		}

		return new RequestExceptionCode(status.status(), status.reason());
	}
}
