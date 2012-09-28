package com.kissme.photo.interfaces;

import com.google.inject.AbstractModule;
import com.kissme.photo.interfaces.http.AppCreateRequestHandler;
import com.kissme.photo.interfaces.http.AppDeleteRequestHandler;
import com.kissme.photo.interfaces.http.AppEditRequestHandler;
import com.kissme.photo.interfaces.http.GalleryCreateRequestHandler;
import com.kissme.photo.interfaces.http.GalleryDeleteRequestHandler;
import com.kissme.photo.interfaces.http.GalleryEditRequestHandler;
import com.kissme.photo.interfaces.http.GalleryQueryRequestHandler;
import com.kissme.photo.interfaces.http.PhotoCreateRequestHandler;
import com.kissme.photo.interfaces.http.PhotoDeleteRequestHandler;
import com.kissme.photo.interfaces.http.PhotoGetRequestHandler;
import com.kissme.photo.interfaces.http.PhotoQueryRequestHandler;

/**
 * 
 * @author loudyn
 * 
 */
public class InterfacesModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(AppCreateRequestHandler.class).asEagerSingleton();
		bind(AppEditRequestHandler.class).asEagerSingleton();
		bind(AppDeleteRequestHandler.class).asEagerSingleton();
		
		bind(GalleryCreateRequestHandler.class).asEagerSingleton();
		bind(GalleryEditRequestHandler.class).asEagerSingleton();
		bind(GalleryDeleteRequestHandler.class).asEagerSingleton();
		bind(GalleryQueryRequestHandler.class).asEagerSingleton();

		bind(PhotoGetRequestHandler.class).asEagerSingleton();
		bind(PhotoCreateRequestHandler.class).asEagerSingleton();
		bind(PhotoQueryRequestHandler.class).asEagerSingleton();
		bind(PhotoDeleteRequestHandler.class).asEagerSingleton();

//		bind(TimestampVerifyRequestInterceptor.class).asEagerSingleton();
//		bind(SignatureVerifyRequestInterceptor.class).asEagerSingleton();
	}

}
