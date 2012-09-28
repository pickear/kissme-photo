package com.kissme.photo.interfaces.http.exception;

import com.kissme.photo.infrastructure.http.annotation.ResponseStatus;

/**
 * 
 * @author loudyn
 * 
 */
@ResponseStatus(status = 404, reason = "Resource not found!")
public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

}
