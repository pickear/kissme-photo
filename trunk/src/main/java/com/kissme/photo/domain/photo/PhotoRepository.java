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
	 * @param galleryId
	 * @param page
	 * @return
	 */
	Page<Photo> findPageByGallery(String galleryId, Page<Photo> page);

	/**
	 * 
	 * @param id
	 */
	void delete(String id);

	/**
	 * 
	 * @param md5
	 * @return
	 */
	Photo get(String md5);

	/**
	 * 
	 * @param entity
	 */
	void save(Photo entity, PhotoThumbConf conf);
}
