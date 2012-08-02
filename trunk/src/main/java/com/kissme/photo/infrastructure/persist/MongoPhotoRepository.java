package com.kissme.photo.infrastructure.persist;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.kissme.photo.domain.gallery.Gallery;
import com.kissme.photo.domain.photo.Photo;
import com.kissme.photo.domain.photo.PhotoRepository;
import com.kissme.photo.infrastructure.Page;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBRef;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

/**
 * 
 * @author loudyn
 * 
 */
public class MongoPhotoRepository extends MongoRepositorySupport<Photo, String> implements PhotoRepository {

	private final GridFS gfs;

	@Inject
	public MongoPhotoRepository(DB db) {
		super(db);
		this.gfs = new GridFS(db, "galleryPhoto");
	}

	@Override
	public Page<Photo> findPageByGallery(String galleryId, Page<Photo> page) {

		page.getParams().put("metadata.gallery.$id", new ObjectId(galleryId));
		DBCursor cursor = getGfsCollection().find(transformQuery(page)).skip(page.getFirst() - 1).limit(page.getPageSize());

		List<Photo> result = Lists.newLinkedList();
		while (cursor.hasNext()) {
			GridFSDBFile file = (GridFSDBFile) cursor.next();
			result.add(gridFileToGalleryPhoto(file, null));
		}

		return page.setTotalCount(cursor.count()).setResult(result);
	}

	private Photo gridFileToGalleryPhoto(GridFSDBFile file, byte[] content) {
		Photo entity = new Photo();
		entity.setContentType(file.getContentType()).setFilename(file.getFilename()).setLastModified(file.getUploadDate());
		entity.setContent(content).setMd5(file.getMD5()).setLength(file.getLength()).setId(file.getId().toString());
		return entity;
	}

	private DBCollection getGfsCollection() {
		GridFS gfs = getGfs();

		try {

			Field field = gfs.getClass().getDeclaredField("_filesCollection");
			field.setAccessible(true);
			return (DBCollection) field.get(gfs);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void delete(String id) {
		getGfs().remove(new ObjectId(id));
	}

	private GridFS getGfs() {
		return this.gfs;
	}

	@Override
	public void save(Photo entity) {

		String md5 = DigestUtils.md5Hex(entity.getContent());
		GridFSDBFile exist = getGfs().findOne(new BasicDBObject("md5", md5));
		if (null != exist) {
			entity.setLastModified(exist.getUploadDate()).setMd5(md5).setId(exist.getId().toString());
			return;
		}

		GridFSInputFile file = getGfs().createFile(entity.getContent());

		String ns = getCollectionName(Gallery.class);
		DBRef ref = new DBRef(getDB(), ns, new ObjectId(entity.getGallery().getId()));
		file.setMetaData(new BasicDBObject(ns, ref));
		file.setContentType(entity.getContentType());
		file.setFilename(entity.getFilename());
		file.save();

		entity.setLastModified(file.getUploadDate()).setMd5(file.getMD5()).setId(file.getId().toString());
		getGfsCollection().ensureIndex(new BasicDBObject("md5", 1), "md5", true);

	}

	@Override
	public Photo get(String id) {

		GridFSDBFile file = getGfs().findOne(new ObjectId(id));
		if (null == file) {
			return null;
		}

		ByteArrayOutputStream out = null;
		try {

			out = new ByteArrayOutputStream();
			file.writeTo(out);

			Photo entity = new Photo();
			entity.setContent(out.toByteArray()).setContentType(file.getContentType()).setLastModified(file.getUploadDate());
			entity.setFilename(file.getFilename()).setMd5(file.getMD5()).setId(id);
			return entity;

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(out);
		}
	}

}
