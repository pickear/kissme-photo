package com.kissme.photo.infrastructure.persist;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.inject.Inject;
import com.kissme.photo.domain.timestamp.TimestampRepository;
import com.kissme.photo.infrastructure.util.ExceptionUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;

/**
 * 
 * @author loudyn
 * 
 */
public class MongoTimestampRepository implements TimestampRepository {
	private final static String COLLECTION_NAME = "timestamp";
	private final DBCollection collection;

	private Cache<String, Boolean> innerCache = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES)
																			.initialCapacity(1024)
																			.maximumSize(1024 * 16)
																			.build();

	@Inject
	public MongoTimestampRepository(DB db) {
		Preconditions.checkNotNull(db);
		DBCollection collection = initCollection(db);
		this.collection = collection;
	}

	private DBCollection initCollection(DB db) {
		if (db.collectionExists(COLLECTION_NAME)) {
			return db.getCollection(COLLECTION_NAME);
		}

		return db.createCollection(COLLECTION_NAME, BasicDBObjectBuilder.start("capped", true).add("size", 7920000).add("max", 180000).get());
	}

	@Override
	public void save(String timestamp) {
		this.collection.save(new BasicDBObject("ts", timestamp));
		this.collection.ensureIndex(new BasicDBObject("ts", timestamp), "ts", true);
		innerCache.put(timestamp, Boolean.TRUE);
	}

	@Override
	public boolean existsTimestamp(final String timestamp) {

		try {

			return innerCache.get(timestamp, new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					return null != MongoTimestampRepository.this.collection.findOne(new BasicDBObject("ts", timestamp));
				}

			});
		} catch (Exception e) {
			throw ExceptionUtils.uncheck(e);
		}

	}

}
