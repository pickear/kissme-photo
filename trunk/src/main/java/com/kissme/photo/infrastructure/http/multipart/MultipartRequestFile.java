package com.kissme.photo.infrastructure.http.multipart;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 
 * @author loudyn
 * 
 */
public interface MultipartRequestFile {

	/**
	 * 
	 * @return
	 */
	String getName();

	/**
	 * 
	 * @return
	 */
	String getOriginalFilename();

	/**
	 * 
	 * @return
	 */
	String getContentType();

	/**
	 * 
	 * @return
	 */
	boolean isEmpty();

	/**
	 * 
	 * @return
	 */
	long getSize();

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	byte[] getBytes() throws IOException;

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	InputStream getInputStream() throws IOException;

	/**
	 * 
	 * @param dest
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	void transferTo(File dest) throws IOException, IllegalStateException;
}
