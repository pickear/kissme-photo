package com.kissme.photo.infrastructure.persist;

import java.util.Map;

import org.bson.types.ObjectId;

import com.google.inject.Inject;
import com.kissme.photo.domain.app.App;
import com.kissme.photo.domain.gallery.Gallery;
import com.kissme.photo.domain.gallery.GalleryRepository;
import com.kissme.photo.infrastructure.Jsons;
import com.kissme.photo.infrastructure.Page;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.DBRef;

/**
 * 
 * @author loudyn
 * 
 */
public class MongoGalleryRepository extends MongoRepositorySupport<Gallery, String> implements GalleryRepository {

	@Inject
	public MongoGalleryRepository(DB db) {
		super(db);
	}

	@Override
	public Page<Gallery> findPageByApp(String appId, Page<Gallery> page) {
		page.getParams().put("app.$id", new ObjectId(appId));
		return findPage(page);
	}

	@Override
	protected Gallery convertDBObject(DBObject obj) {

		DBRef ref = (DBRef) obj.removeField(getCollectionName(App.class));
		DBObject refObj = ref.fetch();
		App app = Jsons.newfor(transformObjectIds(refObj).toMap(), App.class);

		return Jsons.newfor(transformObjectIds(obj).toMap(), Gallery.class).setApp(app);
	}

	@Override
	protected DBObject convertEntity(Gallery entity) {
		String ns = getCollectionName(App.class);
		DBRef ref = new DBRef(getDB(), ns, new ObjectId(entity.getApp().getId()));
		Map<String, Object> describe = describeAndFilterIds(entity);
		describe.put(ns, ref);
		return new BasicDBObject(describe);
	}

	@Override
	public void save(Gallery gallery) {
		getCollection().ensureIndex(new BasicDBObject("app.$id", "1"), "appRef");
		ObjectId objId = doInsert(gallery);
		gallery.setId(objId.toString());
	}

	@Override
	public void delete(String id) {
		getCollection().remove(new BasicDBObject("_id", new ObjectId(id)));
	}

}
