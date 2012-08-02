package com.kissme.photo.infrastructure.persist;

import com.google.inject.Inject;
import com.kissme.photo.domain.timestamp.TimestampRepository;
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

	@Inject
	public MongoTimestampRepository(DB db) {

		if (db.collectionExists(COLLECTION_NAME)) {
			this.collection = db.getCollection(COLLECTION_NAME);
		}

		else {
			this.collection = db.createCollection(COLLECTION_NAME, BasicDBObjectBuilder.start("capped", true).add("size", 7920000).add("max", 180000).get());
		}
	}

	@Override
	public void save(String timestamp) {
		this.collection.save(new BasicDBObject("ts", timestamp));
	}

	@Override
	public boolean existsTimestamp(String timestamp) {
		return null != this.collection.findOne(new BasicDBObject("ts", timestamp));
	}

}
