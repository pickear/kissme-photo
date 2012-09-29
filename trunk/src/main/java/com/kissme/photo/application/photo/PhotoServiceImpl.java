package com.kissme.photo.application.photo;

import com.google.inject.Inject;
import com.kissme.photo.domain.Page;
import com.kissme.photo.domain.gallery.Gallery;
import com.kissme.photo.domain.gallery.GalleryRepository;
import com.kissme.photo.domain.photo.Photo;
import com.kissme.photo.domain.photo.PhotoRepository;
import com.kissme.photo.domain.photo.PhotoThumbConf;
import com.kissme.photo.infrastructure.util.ExceptionUtils;

/**
 * 
 * @author loudyn
 * 
 */
public class PhotoServiceImpl implements PhotoService {

	private GalleryRepository galleryRepository;
	private PhotoRepository photoRepository;

	@Inject
	public PhotoServiceImpl(GalleryRepository galleryRepository, PhotoRepository photoRepository) {
		this.galleryRepository = galleryRepository;
		this.photoRepository = photoRepository;
	}

	@Override
	public Page<Photo> findPage(Page<Photo> page) {
		return photoRepository.findPage(page);
	}

	@Override
	public Page<Photo> findPageByGallery(String galleryId, Page<Photo> page) {
		return photoRepository.findPageByGallery(galleryId, page);
	}

	@Override
	public void delete(String id) {
		photoRepository.delete(id);
	}

	@Override
	public void save(Photo entity, PhotoThumbConf conf) {
		photoRepository.save(entity, conf);
	}

	@Override
	public Photo get(String id) {
		return photoRepository.get(id);
	}

	@Override
	public void deleteByAppAndId(String appId, String id) {
		String galleryId = photoRepository.getGalleryId(id);
		Gallery gallery = galleryRepository.get(appId, galleryId);
		if (null == gallery) {
			throw ExceptionUtils.oneThrow("bad photoId with appId");
		}

		photoRepository.delete(id);
	}

	@Override
	public Page<Photo> findPageByAppAndGallery(String appId, String galleryId, Page<Photo> page) {
		Gallery gallery = galleryRepository.get(appId, galleryId);
		if (null == gallery) {
			throw ExceptionUtils.oneThrow("bad galleryId with appId");
		}
		return findPageByGallery(galleryId, page);
	}
}
