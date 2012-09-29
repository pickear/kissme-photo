package com.kissme.photo.infrastructure.persist;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.kissme.photo.domain.Page;
import com.kissme.photo.domain.gallery.Gallery;
import com.kissme.photo.domain.photo.Photo;
import com.kissme.photo.domain.photo.PhotoRepository;
import com.kissme.photo.domain.photo.PhotoThumbConf;
import com.kissme.photo.infrastructure.util.ExceptionUtils;
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
	public Page<Photo> findPage(Page<Photo> page) {
		DBCursor cursor = getGfsCollection().find(transformQuery(page)).skip(page.getFirst() - 1).limit(page.getPageSize());

		List<Photo> result = Lists.newLinkedList();
		while (cursor.hasNext()) {
			GridFSDBFile file = (GridFSDBFile) cursor.next();
			result.add(new GfsPhotoFileAdapter(file));
		}

		return page.setTotalCount(cursor.count()).setResult(result);
	}

	@Override
	public Page<Photo> findPageByGallery(String galleryId, Page<Photo> page) {
		Preconditions.checkArgument(StringUtils.isNotBlank(galleryId));
		page.getParams().put("metadata.gallery.$id", new ObjectId(galleryId));
		return findPage(page);
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
	public void save(Photo entity, PhotoThumbConf conf) {

		try {

			byte[] content = entity.getBytes(conf);
			String md5 = DigestUtils.md5Hex(content);
			GridFSDBFile exist = getGfs().findOne(new BasicDBObject("md5", md5));
			if (null != exist) {
				entity.setLastModified(exist.getUploadDate()).setMd5(md5).setId(exist.getId().toString());
				return;
			}

			GridFSInputFile file = getGfs().createFile(content);

			String ns = getCollectionName(Gallery.class);
			DBRef ref = new DBRef(getDB(), ns, new ObjectId(entity.getGallery().getId()));
			file.setMetaData(new BasicDBObject(ns, ref));
			file.setContentType(entity.getContentType());
			file.setFilename(entity.getFilename());
			file.save();

			entity.setLastModified(file.getUploadDate()).setMd5(file.getMD5()).setId(file.getId().toString());
			getGfsCollection().ensureIndex(new BasicDBObject("md5", 1), "md5", true);
		} catch (Exception e) {
			throw ExceptionUtils.uncheck(e);
		}

	}

	@Override
	public Photo get(String id) {

		GridFSDBFile file = getGfs().findOne(new ObjectId(id));
		if (null == file) {
			return null;
		}

		return new GfsPhotoFileAdapter(file);
	}
	
	public String getGalleryId(String id){
		GridFSDBFile file = getGfs().findOne(new ObjectId(id));
		if (null == file) {
			return null;
		}
		
		DBRef ref = (DBRef) file.getMetaData().get(getCollectionName(Gallery.class));
		return ref.getId().toString();
	}

	class GfsPhotoFileAdapter extends Photo {
		private static final long serialVersionUID = 1L;
		private GridFSDBFile gfsFile;

		public GfsPhotoFileAdapter(GridFSDBFile gfsFile) {
			this.gfsFile = gfsFile;
		}

		@Override
		public String getId() {
			return gfsFile.getId().toString();
		}

		@Override
		public String getFilename() {
			return gfsFile.getFilename();
		}

		@Override
		public String getContentType() {
			return gfsFile.getContentType();
		}

		@Override
		public String getMd5() {
			return gfsFile.getMD5();
		}

		@Override
		public long getLength() {
			return gfsFile.getLength();
		}

		@Override
		public Date getLastModified() {
			return gfsFile.getUploadDate();
		}

		@Override
		@JsonIgnore
		public InputStream getInputStream(PhotoThumbConf conf) throws IOException {
			return new ByteArrayInputStream(getBytes(conf));
		}

		@Override
		@JsonIgnore
		public byte[] getBytes(PhotoThumbConf conf) throws IOException {
			boolean thumb = false;
			Builder<? extends InputStream> builder = Thumbnails.of(gfsFile.getInputStream());

			if (conf.requiredResize()) {
				builder.size(conf.getWidth(), conf.getHeight());
				thumb = true;
			}

			if (conf.requiredCrop()) {
				builder.sourceRegion(conf.getCropX(), conf.getCropY(), conf.getWidth(), conf.getHeight());
				thumb = true;
			}

			if (conf.requiredRotate()) {
				builder.rotate(conf.getRotate());
				thumb = true;
			}
			if (conf.requiredQuality()) {
				builder.outputQuality(conf.getQuality());
				thumb = true;
			}

			if (thumb) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				builder.toOutputStream(out);
				return out.toByteArray();
			}

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			gfsFile.writeTo(out);
			return out.toByteArray();
		}
	}
}
