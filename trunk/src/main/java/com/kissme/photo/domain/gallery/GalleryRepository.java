package com.kissme.photo.domain.gallery;

import com.kissme.photo.domain.Page;

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
	 * @param id2
	 * @return
	 */
	Gallery get(String appId, String id);

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
	 * @param page
	 * @return
	 */
	Page<Gallery> findPage(Page<Gallery> page);
}
