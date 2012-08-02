package com.kissme.photo.infrastructure.http.multipart;

import com.kissme.photo.infrastructure.http.Request;

/**
 * 
 * @author loudyn
 * 
 */
public interface MultipartRequestResolver {
	/**
	 * 
	 * @param request
	 * @return
	 */
	boolean isMultipart(Request request);

	/**
	 * 
	 * @param request
	 * @return
	 */
	MultipartRequest resolveMultipart(Request request);

	/**
	 * 
	 * @param request
	 */
	void cleanupMultipart(MultipartRequest request);
}
