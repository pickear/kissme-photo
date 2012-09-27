package com.kissme.photo.infrastructure.persist;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.kissme.photo.domain.app.AppRepository;
import com.kissme.photo.domain.gallery.GalleryRepository;
import com.kissme.photo.domain.photo.PhotoRepository;
import com.kissme.photo.domain.timestamp.TimestampRepository;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoOptions;
import com.mongodb.ServerAddress;

/**
 * 
 * @author loudyn
 * 
 */
public class DevelopmentPersistModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(DB.class).toProvider(MongoDBProvider.class).asEagerSingleton();
		bind(AppRepository.class).to(MongoAppRepository.class).asEagerSingleton();
		bind(GalleryRepository.class).to(MongoGalleryRepository.class).asEagerSingleton();
		bind(PhotoRepository.class).to(MongoPhotoRepository.class).asEagerSingleton();
		bind(TimestampRepository.class).to(MongoTimestampRepository.class).asEagerSingleton();
	}

	/**
	 * 
	 * @author loudyn
	 * 
	 */
	static class MongoDBProvider implements Provider<DB> {

		@Override
		public DB get() {

			MongoOptions options = new MongoOptions();
			options.autoConnectRetry = true;
			options.connectionsPerHost = 100;
			options.connectTimeout = 3000;
			options.maxAutoConnectRetryTime = 3;
			options.socketKeepAlive = true;

			try {

				Mongo mongo = new Mongo(new ServerAddress("192.168.1.96"), options);
				return mongo.getDB("photos");
			} catch (Exception e) {
				throw new RuntimeException("Create mongo occur error!", e);
			}
		}

	}

}
