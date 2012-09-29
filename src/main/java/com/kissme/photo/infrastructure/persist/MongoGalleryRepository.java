package com.kissme.photo.infrastructure.persist;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.kissme.photo.domain.Page;
import com.kissme.photo.domain.app.App;
import com.kissme.photo.domain.gallery.Gallery;
import com.kissme.photo.domain.gallery.GalleryRepository;
import com.kissme.photo.infrastructure.util.JsonUtils;
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
		Preconditions.checkArgument(StringUtils.isNotBlank(appId));
		page.getParams().put("app.$id", new ObjectId(appId));
		return findPage(page);
	}

	@Override
	protected Gallery convertDBObject(DBObject obj) {

		DBRef ref = (DBRef) obj.removeField(getCollectionName(App.class));
		DBObject refObj = ref.fetch();
		App app = JsonUtils.newfor(transformObjectIds(refObj).toMap(), App.class);

		return JsonUtils.newfor(transformObjectIds(obj).toMap(), Gallery.class).setApp(app);
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
	public Gallery get(String appId, String id) {
		BasicDBObject obj = new BasicDBObject();
		obj.put("app.$id", new ObjectId(appId));
		obj.put("_id", new ObjectId(id));
		DBObject result = getCollection().findOne(obj);
		return convertDBObject(result);
	}

	@Override
	public void delete(String id) {
		getCollection().remove(new BasicDBObject("_id", new ObjectId(id)));
	}
}
