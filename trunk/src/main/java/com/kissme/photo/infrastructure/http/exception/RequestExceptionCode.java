package com.kissme.photo.infrastructure.http.exception;

/**
 * 
 * @author loudyn
 * 
 */
public class RequestExceptionCode {

	private final int code;
	private final String message;

	/**
	 * 
	 * @param code
	 * @param message
	 */
	public RequestExceptionCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

	/**
	 * 
	 * @return
	 */
	public int getCode() {
		return code;
	}

	/**
	 * 
	 * @return
	 */
	public String getMessage() {
		return message;
	}
}
