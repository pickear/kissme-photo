package com.kissme.photo.infrastructure.http.exception;



/**
 * 
 * @author loudyn
 * 
 */
public interface RequestExceptionTranslator {

	/**
	 * 
	 * @param e
	 * @return
	 */
	RequestExceptionCode translate(Exception e);
}
