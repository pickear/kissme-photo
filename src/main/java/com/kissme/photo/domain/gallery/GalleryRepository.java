package com.kissme.photo.domain.gallery;

import com.kissme.photo.infrastructure.Page;

/**
 * 
 * @author loudyn
 *
 */
public interface GalleryRepository {

	/**
	 * 
	 * @param appId
	 * @param page
	 * @return
	 */
	Page<Gallery> findPageByApp(String appId, Page<Gallery> page);

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
