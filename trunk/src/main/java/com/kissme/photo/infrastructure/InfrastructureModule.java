package com.kissme.photo.infrastructure;

import com.google.inject.AbstractModule;
import com.kissme.photo.domain.app.AppRepository;
import com.kissme.photo.domain.gallery.GalleryRepository;
import com.kissme.photo.domain.photo.PhotoRepository;
import com.kissme.photo.domain.timestamp.TimestampRepository;
import com.kissme.photo.infrastructure.http.DefaultRequestHandlerChainFactory;
import com.kissme.photo.infrastructure.http.RequestHandlerChainFactory;
import com.kissme.photo.infrastructure.http.exception.RequestExceptionTranslator;
import com.kissme.photo.infrastructure.http.exception.ResponseStatusExceptionTranslator;
import com.kissme.photo.infrastructure.http.mapping.DefaultRequestHandlerMapping;
import com.kissme.photo.infrastructure.http.mapping.RequestHandlerMapping;
import com.kissme.photo.infrastructure.http.multipart.MultipartRequestResolver;
import com.kissme.photo.infrastructure.http.multipart.commons.CommonsMultipartRequestResolver;
import com.kissme.photo.infrastructure.persist.MongoAppRepository;
import com.kissme.photo.infrastructure.persist.MongoGalleryRepository;
import com.kissme.photo.infrastructure.persist.MongoPhotoRepository;
import com.kissme.photo.infrastructure.persist.MongoTimestampRepository;

/**
 * 
 * @author loudyn
 * 
 */
public class InfrastructureModule extends AbstractModule {

	@Override
	protected void configure() {

		// http module
		bind(RequestHandlerMapping.class).to(DefaultRequestHandlerMapping.class).asEagerSingleton();
		bind(RequestExceptionTranslator.class).to(ResponseStatusExceptionTranslator.class).asEagerSingleton();
		bind(MultipartRequestResolver.class).to(CommonsMultipartRequestResolver.class).asEagerSingleton();
		bind(RequestHandlerChainFactory.class).to(DefaultRequestHandlerChainFactory.class).asEagerSingleton();

		// persist module
		bind(AppRepository.class).to(MongoAppRepository.class).asEagerSingleton();
		bind(GalleryRepository.class).to(MongoGalleryRepository.class).asEagerSingleton();
		bind(PhotoRepository.class).to(MongoPhotoRepository.class).asEagerSingleton();
		bind(TimestampRepository.class).to(MongoTimestampRepository.class).asEagerSingleton();
	}
}
