package com.kissme.photo.infrastructure.persist;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.kissme.photo.infrastructure.Jsons;
import com.kissme.photo.infrastructure.Page;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * 
 * @author loudyn
 * 
 */
public abstract class MongoRepositorySupport<T, PK extends Serializable> {

	private static ImmutableList<String> IDS = ImmutableList.of("id", "_id");
	private final DB db;
	private Class<T> entityClazz;

	/**
	 * 
	 * @param mongo
	 */
	@SuppressWarnings("unchecked")
	protected MongoRepositorySupport(DB db) {
		this.db = db;
		this.entityClazz = (Class<T>) createEnityClazz();
	}

	private Class<?> createEnityClazz() {
		Type genric = getClass().getGenericSuperclass();
		if (!(genric instanceof ParameterizedType)) {
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genric).getActualTypeArguments();
		if (params.length <= 0) {
			return Object.class;
		}

		return (Class<?>) params[0];
	}

	/**
	 * 
	 * @return
	 */
	protected final DB getDB() {
		return this.db;
	}

	/**
	 * 
	 * @return
	 */
	protected DBCollection getCollection() {
		return getDB().getCollection(StringUtils.uncapitalize(entityClazz.getSimpleName()));
	}

	/**
	 * 
	 * @param clazz
	 * @return
	 */
	protected String getCollectionName(Class<?> clazz) {
		return StringUtils.uncapitalize(clazz.getSimpleName());
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public T get(PK id) {
		DBObject obj = getCollection().findOne(new BasicDBObject("_id", new ObjectId(id.toString())));
		if (null == obj) {
			return null;
		}

		return convertDBObject(obj);
	}

	public Page<T> findPage(Page<T> page) {

		DBObject query = transformQuery(page);
		DBCursor cursor = getCollection().find(query).skip(page.getFirst() - 1).limit(page.getPageSize());

		List<T> result = new ArrayList<T>();
		while (cursor.hasNext()) {
			DBObject obj = cursor.next();
			result.add(convertDBObject(obj));
		}

		return page.setTotalCount(cursor.count()).setResult(result);
	}

	/**
	 * 
	 * @param obj
	 * @return
	 */
	protected T convertDBObject(DBObject obj) {
		return Jsons.newfor(transformObjectIds(obj).toMap(), entityClazz);
	}

	/**
	 * 
	 * @param entity
	 * @return
	 */
	protected DBObject convertEntity(T entity) {
		return new BasicDBObject(describeAndFilterIds(entity));
	}

	protected DBObject transformQuery(Page<T> page) {

		Map<String, Object> params = page.getParams();
		Map<String, Object> filter = Maps.filterValues(params, new Predicate<Object>() {

			@Override
			public boolean apply(Object input) {
				if (isPrimitiveOrNotEmptyString(input)) {
					return true;
				}

				if (isNotNullObjectId(input)) {
					return true;
				}

				if (isNotEmptyMap(input)) {
					return true;
				}
				
				return false;
			}
		});
		
		return new BasicDBObject(filter);
	}

	protected boolean isPrimitiveOrNotEmptyString(Object input) {
		return isPrimitiveOrString(input) && StringUtils.isNotBlank(input.toString());
	}

	/**
	 * 
	 * @param input
	 * @return
	 */
	protected boolean isPrimitiveOrString(Object input) {
		if (null == input) {
			return false;
		}
		if (input.getClass().isPrimitive()) {
			return true;
		}

		if (input instanceof String) {
			return true;
		}

		Class<?> klass = input.getClass();
		return klass == Boolean.class || klass == Integer.class || klass == Long.class
				|| klass == Float.class || klass == Byte.class || klass == Double.class;
	}

	protected boolean isNotNullObjectId(Object input) {
		return null != input && input instanceof ObjectId;
	}

	protected boolean isNotEmptyMap(Object input) {
		return (input instanceof Map) && !((Map<?, ?>) (input)).isEmpty();
	}

	/**
	 * 
	 * @param entity
	 * @return
	 */
	protected ObjectId doInsert(T entity) {

		DBObject obj = convertEntity(entity);
		getCollection().save(obj);
		return getObjectId(obj);
	}

	protected ObjectId getObjectId(DBObject obj) {
		return (ObjectId) obj.get("_id");
	}

	protected String getObjectIdAsString(DBObject obj) {
		return getObjectId(obj).toString();
	}

	protected DBObject transformObjectIds(DBObject obj) {
		obj.put("id", getObjectIdAsString(obj));
		return obj;
	}

	/**
	 * 
	 * @param query
	 * @param entity
	 */
	protected void doUpdate(PK id, T entity) {
		getCollection().update(new BasicDBObject("_id", new ObjectId(id.toString())), convertEntity(entity), false, false);
	}

	@SuppressWarnings("unchecked")
	protected final Map<String, Object> describeAndFilterIds(T entity) {

		Map<String, Object> describe = Jsons.newfor(entity, Map.class);
		return Maps.filterKeys(describe, new Predicate<String>() {

			@Override
			public boolean apply(String input) {
				return !IDS.contains(input);
			}
		});
	}
}
