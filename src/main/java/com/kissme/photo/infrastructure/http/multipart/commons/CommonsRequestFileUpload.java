package com.kissme.photo.infrastructure.http.multipart.commons;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUpload;

/**
 * 
 * @author loudyn
 * 
 */
public class CommonsRequestFileUpload extends FileUpload {

	/**
	 * 
	 * @param factory
	 */
	public CommonsRequestFileUpload(FileItemFactory factory) {
		super(factory);
	}
}
