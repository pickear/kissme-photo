package com.kissme.photo.interfaces;

import com.google.inject.AbstractModule;
import com.kissme.photo.interfaces.http.GalleryCreateRequestHandler;
import com.kissme.photo.interfaces.http.GalleryDeleteRequestHandler;
import com.kissme.photo.interfaces.http.GalleryEditRequestHandler;
import com.kissme.photo.interfaces.http.GalleryQueryRequestHandler;
import com.kissme.photo.interfaces.http.PhotoCreateRequestHandler;
import com.kissme.photo.interfaces.http.PhotoDeleteRequestHandler;
import com.kissme.photo.interfaces.http.PhotoGetRequestHandler;
import com.kissme.photo.interfaces.http.PhotoQueryRequestHandler;
import com.kissme.photo.interfaces.http.admin.AdminAppCreateRequestHandler;
import com.kissme.photo.interfaces.http.admin.AdminAppDeleteRequestHandler;
import com.kissme.photo.interfaces.http.admin.AdminAppQueryRequestHandler;
import com.kissme.photo.interfaces.http.admin.AdminGalleryDeleteRequestHandler;
import com.kissme.photo.interfaces.http.admin.AdminGalleryQueryRequestHandler;
import com.kissme.photo.interfaces.http.admin.AdminPhotoDeleteRequestHandler;
import com.kissme.photo.interfaces.http.admin.AdminPhotoQueryRequestHandler;
import com.kissme.photo.interfaces.http.interceptor.TimestampVerifyRequestInterceptor;

/**
 * 
 * @author loudyn
 * 
 */
public class InterfacesModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(AdminAppCreateRequestHandler.class).asEagerSingleton();
		bind(AdminAppDeleteRequestHandler.class).asEagerSingleton();
		bind(AdminAppQueryRequestHandler.class).asEagerSingleton();
		bind(AdminGalleryDeleteRequestHandler.class).asEagerSingleton();
		bind(AdminGalleryQueryRequestHandler.class).asEagerSingleton();
		bind(AdminPhotoDeleteRequestHandler.class).asEagerSingleton();
		bind(AdminPhotoQueryRequestHandler.class).asEagerSingleton();

		bind(GalleryCreateRequestHandler.class).asEagerSingleton();
		bind(GalleryEditRequestHandler.class).asEagerSingleton();
		bind(GalleryDeleteRequestHandler.class).asEagerSingleton();
		bind(GalleryQueryRequestHandler.class).asEagerSingleton();

		bind(PhotoGetRequestHandler.class).asEagerSingleton();
		bind(PhotoCreateRequestHandler.class).asEagerSingleton();
		bind(PhotoQueryRequestHandler.class).asEagerSingleton();
		bind(PhotoDeleteRequestHandler.class).asEagerSingleton();

		bind(TimestampVerifyRequestInterceptor.class).asEagerSingleton();
	}

}
