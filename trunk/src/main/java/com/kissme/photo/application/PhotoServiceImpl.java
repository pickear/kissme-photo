package com.kissme.photo.application;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.kissme.photo.domain.app.App;
import com.kissme.photo.domain.app.AppExpireException;
import com.kissme.photo.domain.photo.Photo;
import com.kissme.photo.domain.photo.PhotoRepository;
import com.kissme.photo.domain.photo.PhotoThumbConf;
import com.kissme.photo.infrastructure.Page;

/**
 * 
 * @author loudyn
 * 
 */
public class PhotoServiceImpl implements PhotoService {

	private PhotoRepository photoRepository;

	@Inject
	public PhotoServiceImpl(PhotoRepository photoRepository) {
		this.photoRepository = photoRepository;
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
		App app = entity.getApp();
		Preconditions.checkNotNull(app);
		if (app.isExpire()) {
			throw new AppExpireException();
		}

		photoRepository.save(entity, conf);
	}

	@Override
	public Photo get(String id) {
		return photoRepository.get(id);
	}

}
