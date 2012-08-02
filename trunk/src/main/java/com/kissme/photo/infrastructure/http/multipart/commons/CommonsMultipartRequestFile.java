package com.kissme.photo.infrastructure.http.multipart.commons;

import java.io.File;
import java.io.IOException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang.StringEscapeUtils;

import com.kissme.photo.infrastructure.http.multipart.MultipartRequestFile;

/**
 * 
 * @author loudyn
 * 
 */
public class CommonsMultipartRequestFile implements MultipartRequestFile {
	private final FileItem fileItem;
	private final long size;

	/**
	 * 
	 * @param fileItem
	 */
	public CommonsMultipartRequestFile(FileItem fileItem) {
		this.fileItem = fileItem;
		this.size = this.fileItem.getSize();
	}

	/**
	 * 
	 * @return
	 */
	public final FileItem getFileItem() {
		return fileItem;
	}

	public String getName() {
		return this.fileItem.getFieldName();
	}

	public String getOriginalFilename() {
		String filename = this.fileItem.getName();
		if (filename == null) {
			// Should never happen.
			return "";
		}
		// check for Unix-style path
		int pos = filename.lastIndexOf("/");
		if (pos == -1) {
			// check for Windows-style path
			pos = filename.lastIndexOf("\\");
		}
		if (pos != -1) {
			// any sort of path separator found
			filename = filename.substring(pos + 1);
		}

		return StringEscapeUtils.unescapeHtml(filename);
	}

	public String getContentType() {
		return this.fileItem.getContentType();
	}

	public boolean isEmpty() {
		return (this.size == 0);
	}

	public long getSize() {
		return this.size;
	}

	public byte[] getBytes() throws IOException {
		if (!isAvailable()) {
			throw new IllegalStateException("File has been moved - cannot be read again");
		}
		byte[] bytes = this.fileItem.get();
		return (bytes != null ? bytes : new byte[0]);
	}

	public void transferTo(File dest) throws IOException, IllegalStateException {
		if (!isAvailable()) {
			throw new IllegalStateException("File has already been moved - cannot be transferred again");
		}

		if (dest.exists() && !dest.delete()) {
			throw new IOException("Destination file [" + dest.getAbsolutePath() + "] already exists and could not be deleted");
		}

		try {

			this.fileItem.write(dest);
		} catch (FileUploadException ex) {
			throw new IllegalStateException(ex.getMessage());
		} catch (IOException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new IOException("Could not transfer to file: " + ex.getMessage());
		}
	}

	private boolean isAvailable() {
		// If in memory, it's available.
		if (this.fileItem.isInMemory()) {
			return true;
		}
		// Check actual existence of temporary file.
		if (this.fileItem instanceof DiskFileItem) {
			return ((DiskFileItem) this.fileItem).getStoreLocation().exists();
		}
		// Check whether current file size is different than original one.
		return (this.fileItem.getSize() == this.size);
	}

}
