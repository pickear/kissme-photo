package com.kissme.photo.application.gallery;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.kissme.photo.domain.Page;
import com.kissme.photo.domain.app.App;
import com.kissme.photo.domain.app.AppExpireException;
import com.kissme.photo.domain.gallery.Gallery;
import com.kissme.photo.domain.gallery.GalleryRepository;

/**
 * 
 * @author loudyn
 * 
 */
public class GalleryServiceImpl implements GalleryService {

	private GalleryRepository galleryRepository;

	@Inject
	public GalleryServiceImpl(GalleryRepository galleryRepository) {
		this.galleryRepository = galleryRepository;
	}

	@Override
	public Page<Gallery> findPageByApp(String app, Page<Gallery> page) {
		return galleryRepository.findPageByApp(app, page);
	}

	@Override
	public Gallery get(String id) {
		return galleryRepository.get(id);
	}

	@Override
	public void save(Gallery gallery) {
		App app = gallery.getApp();
		Preconditions.checkNotNull(app);

		if (app.isExpire()) {
			throw new AppExpireException();
		}
		galleryRepository.save(gallery);
	}

	@Override
	public void delete(String id) {
		galleryRepository.delete(id);
	}

}
