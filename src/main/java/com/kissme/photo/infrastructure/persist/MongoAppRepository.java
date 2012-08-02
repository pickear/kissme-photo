package com.kissme.photo.infrastructure.persist;

import org.bson.types.ObjectId;

import com.google.inject.Inject;
import com.kissme.photo.domain.app.App;
import com.kissme.photo.domain.app.AppRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;

/**
 * 
 * @author loudyn
 * 
 */
public class MongoAppRepository extends MongoRepositorySupport<App, String> implements AppRepository {

	@Inject
	public MongoAppRepository(DB db) {
		super(db);
	}

	@Override
	public void save(App entity) {
		getCollection().ensureIndex(new BasicDBObject("keys.appKey", 1), "appKey", true);
		ObjectId id = super.doInsert(entity);
		entity.setId(id.toString());
	}

	@Override
	public App getByAppKey(String appKey) {
		DBObject obj = getCollection().findOne(new BasicDBObject("keys.appKey", appKey));
		return convertDBObject(obj);
	}

	@Override
	public boolean existsApp(App entity) {
		return null != getCollection().findOne(new BasicDBObject("keys.appKey", entity.getAppKey()));
	}

	@Override
	public void update(App entity) {
		// some malicious request may be occur
		super.doUpdate(entity.getId(), entity);
	}

	@Override
	public void delete(String id) {
		getCollection().remove(new BasicDBObject("_id", new ObjectId(id)));
	}

}
