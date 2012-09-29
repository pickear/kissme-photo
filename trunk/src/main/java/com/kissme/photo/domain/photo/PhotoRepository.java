package com.kissme.photo.domain.photo;

import com.kissme.photo.domain.Page;

/**
 * 
 * @author loudyn
 * 
 */
public interface PhotoRepository {

	/**
	 * 
	 * @param id
	 */
	void delete(String id);

	/**
	 * 
	 * @param id
	 * @return
	 */
	Photo get(String id);

	/**
	 * 
	 * @param id
	 * @return
	 */
	public String getGalleryId(String id);

	/**
	 * 
	 * @param entity
	 */
	void save(Photo entity, PhotoThumbConf conf);

	/**
	 * 
	 * @param page
	 * @return
	 */
	Page<Photo> findPage(Page<Photo> page);

	/**
	 * 
	 * @param galleryId
	 * @param page
	 * @return
	 */
	Page<Photo> findPageByGallery(String galleryId, Page<Photo> page);
}
