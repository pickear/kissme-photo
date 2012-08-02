package com.kissme.photo.application;

import com.kissme.photo.domain.gallery.Gallery;
import com.kissme.photo.infrastructure.Page;

/**
 * 
 * @author loudyn
 * 
 */
public interface GalleryService {

	/**
	 * 
	 * @param app
	 * @param page
	 * @return
	 */
	Page<Gallery> findPageByApp(String app, Page<Gallery> page);

	/**
	 * 
	 * @param id
	 * @return
	 */
	Gallery get(String id);

	/**
	 * 
	 * @param gallery
	 */
	void save(Gallery gallery);

	/**
	 * 
	 * @param id
	 */
	void delete(String id);
}
