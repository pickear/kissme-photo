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
	 * @param appId
	 * @param id
	 * @return
	 */
	Gallery getByAppAndId(String appId, String id);

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

	/**
	 * 
	 * @param appId
	 * @param id
	 */
	void deleteByAppAndId(String appId, String id);

	/**
	 * 
	 * @param page
	 * @return
	 */
	Page<Gallery> findPage(Page<Gallery> page);

	/**
	 * 
	 * @param app
	 * @param page
	 * @return
	 */
	Page<Gallery> findPageByApp(String app, Page<Gallery> page);

}
