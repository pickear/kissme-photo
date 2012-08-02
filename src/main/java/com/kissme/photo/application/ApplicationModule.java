package com.kissme.photo.application;

import com.google.inject.AbstractModule;

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
