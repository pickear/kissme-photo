package com.kissme.photo.application;

import com.google.inject.AbstractModule;
import com.kissme.photo.application.app.AppService;
import com.kissme.photo.application.app.AppServiceImpl;
import com.kissme.photo.application.gallery.GalleryService;
import com.kissme.photo.application.gallery.GalleryServiceImpl;
import com.kissme.photo.application.photo.PhotoService;
import com.kissme.photo.application.photo.PhotoServiceImpl;
import com.kissme.photo.application.timestamp.TimestampService;
import com.kissme.photo.application.timestamp.TimestampServiceImpl;

/**
 * 
 * @author loudyn
 * 
 */
public class ApplicationModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(AppService.class).to(AppServiceImpl.class).asEagerSingleton();
		bind(GalleryService.class).to(GalleryServiceImpl.class).asEagerSingleton();
		bind(PhotoService.class).to(PhotoServiceImpl.class).asEagerSingleton();
		bind(TimestampService.class).to(TimestampServiceImpl.class).asEagerSingleton();
	}

}
