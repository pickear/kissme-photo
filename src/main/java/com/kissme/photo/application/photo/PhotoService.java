package com.kissme.photo.application.photo;

import com.kissme.photo.domain.Page;
import com.kissme.photo.domain.photo.Photo;
import com.kissme.photo.domain.photo.PhotoThumbConf;

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
	 * @param appId
	 * @param id
	 */
	void deleteByAppAndId(String appId, String id);

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
	void save(Photo entity, PhotoThumbConf conf);

	/**
	 * 
	 * @param page
	 * @return
	 */
	Page<Photo> findPage(Page<Photo> page);

	/**
	 * 
	 * @param appId
	 * @param galleryId
	 * @param page
	 * @return
	 */
	Page<Photo> findPageByAppAndGallery(String appId, String galleryId, Page<Photo> page);

}
