package com.kissme.photo.domain.photo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.kissme.photo.domain.AbstractDomain;
import com.kissme.photo.domain.app.App;
import com.kissme.photo.domain.gallery.Gallery;

/**
 * 
 * @author loudyn
 * 
 */
public abstract class Photo extends AbstractDomain {

	private static final long serialVersionUID = 1L;

	private Gallery gallery;
	private String filename;
	private String contentType;
	private String md5;

	private long length;
	private Date lastModified;

	@JsonIgnore
	public Gallery getGallery() {
		return gallery;
	}

	public Photo setGallery(Gallery gallery) {
		this.gallery = gallery;
		return this;
	}

	/**
	 * 
	 * @return
	 */
	@JsonIgnore
	public App getApp() {
		return null == getGallery() ? null : getGallery().getApp();
	}

	public String getFilename() {
		return filename;
	}

	public Photo setFilename(String filename) {
		this.filename = filename;
		return this;
	}

	public String getContentType() {
		return contentType;
	}

	public Photo setContentType(String contentType) {
		this.contentType = contentType;
		return this;
	}

	public String getMd5() {
		return md5;
	}

	public Photo setMd5(String md5) {
		this.md5 = md5;
		return this;
	}

	public long getLength() {
		return length;
	}

	public Photo setLength(long length) {
		this.length = length;
		return this;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public Photo setLastModified(Date lastModified) {
		this.lastModified = lastModified;
		return this;
	}

	/**
	 * 
	 * @return
	 */
	public abstract InputStream getInputStream(PhotoThumbConf conf) throws IOException;
	
	/**
	 * 
	 * @param conf
	 * @return
	 * @throws IOException
	 */
	public abstract byte[] getBytes(PhotoThumbConf conf) throws IOException;
}
