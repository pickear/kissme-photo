package com.kissme.photo.application.gallery;

import com.kissme.photo.domain.Page;
import com.kissme.photo.domain.gallery.Gallery;

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
