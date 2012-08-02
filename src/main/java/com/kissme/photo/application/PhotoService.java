package com.kissme.photo.application;

import com.kissme.photo.domain.photo.Photo;
import com.kissme.photo.infrastructure.Page;

/**
 * 
 * @author loudyn
 * 
 */
public interface PhotoService {

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
	 * @param id
	 * @return
	 */
	Photo get(String id);

	/**
	 * 
	 * @param entity
	 */
	void save(Photo entity);
}
