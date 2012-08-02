package com.kissme.photo.interfaces.exception;

import com.kissme.photo.infrastructure.http.annotation.ResponseStatus;

/**
 * 
 * @author loudyn
 * 
 */
@ResponseStatus(status = 400, reason = "Bad request!")
public class BadRequestException extends RuntimeException {

	private static final long serialVersionUID = 1L;

}
