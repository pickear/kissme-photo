package com.kissme.photo.interfaces;

import com.google.inject.AbstractModule;
import com.kissme.photo.interfaces.app.CreateAppRequestHandler;
import com.kissme.photo.interfaces.app.DeleteAppRequestHandler;
import com.kissme.photo.interfaces.app.EditAppRequestHandler;
import com.kissme.photo.interfaces.gallery.CreateGalleryRequestHandler;
import com.kissme.photo.interfaces.gallery.DeleteGalleryRequestHandler;
import com.kissme.photo.interfaces.gallery.EditGalleryRequestHandler;
import com.kissme.photo.interfaces.gallery.QueryGalleryRequestHandler;
import com.kissme.photo.interfaces.interceptor.SignatureVerifyRequestInterceptor;
import com.kissme.photo.interfaces.interceptor.TimestampVerifyRequestInterceptor;
import com.kissme.photo.interfaces.photo.CreatePhotoRequestHandler;
import com.kissme.photo.interfaces.photo.DeletePhotoRequestHandler;
import com.kissme.photo.interfaces.photo.GetPhotoRequestHandler;
import com.kissme.photo.interfaces.photo.QueryPhotoRequestHandler;

/**
 * 
 * @author loudyn
 * 
 */
public class InterfacesModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(CreateAppRequestHandler.class).asEagerSingleton();
		bind(EditAppRequestHandler.class).asEagerSingleton();
		bind(DeleteAppRequestHandler.class).asEagerSingleton();
		
		bind(CreateGalleryRequestHandler.class).asEagerSingleton();
		bind(EditGalleryRequestHandler.class).asEagerSingleton();
		bind(DeleteGalleryRequestHandler.class).asEagerSingleton();
		bind(QueryGalleryRequestHandler.class).asEagerSingleton();

		bind(GetPhotoRequestHandler.class).asEagerSingleton();
		bind(CreatePhotoRequestHandler.class).asEagerSingleton();
		bind(QueryPhotoRequestHandler.class).asEagerSingleton();
		bind(DeletePhotoRequestHandler.class).asEagerSingleton();

		bind(TimestampVerifyRequestInterceptor.class).asEagerSingleton();
		bind(SignatureVerifyRequestInterceptor.class).asEagerSingleton();
	}

}
