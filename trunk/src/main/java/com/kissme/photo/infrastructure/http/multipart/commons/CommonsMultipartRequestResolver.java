package com.kissme.photo.infrastructure.http.multipart.commons;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.lang.StringUtils;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMethod;

import com.google.common.collect.Maps;
import com.kissme.photo.infrastructure.http.Request;
import com.kissme.photo.infrastructure.http.multipart.MultipartRequest;
import com.kissme.photo.infrastructure.http.multipart.MultipartRequestFile;
import com.kissme.photo.infrastructure.http.multipart.MultipartRequestResolver;

/**
 * 
 * @author loudyn
 * 
 */
public class CommonsMultipartRequestResolver implements MultipartRequestResolver {

	public static final String MULTIPART = "multipart/";

	private FileItemFactory fileItemFactory;

	public CommonsMultipartRequestResolver() {
		this.fileItemFactory = initializeDefaultFileItemFactory();
	}

	/**
	 * 
	 * @param fileItemFactory
	 */
	public CommonsMultipartRequestResolver(FileItemFactory fileItemFactory) {
		this.fileItemFactory = fileItemFactory;
	}

	private FileItemFactory initializeDefaultFileItemFactory() {
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(1024 * 1024 * 1024);
		return factory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kissme.photo.infrastructure.http.multipart.MultipartRequestResolver#isMultipart(com.kissme.photo.infrastructure.http.Request)
	 */
	public boolean isMultipart(Request request) {

		if (null == request) {
			return false;
		}

		HttpMethod method = request.getMethod();
		if (HttpMethod.POST != method) {
			return false;
		}

		String contentType = request.getHeader(HttpHeaders.Names.CONTENT_TYPE);
		return null != contentType && contentType.toLowerCase().startsWith(MULTIPART);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kissme.photo.infrastructure.http.multipart.MultipartRequestResolver#resolveMultipart(com.kissme.photo.infrastructure.http.Request)
	 */
	public MultipartRequest resolveMultipart(final Request request) {
		return new CommonsNettyMultipartHttpRequest(request) {

			@Override
			public void initializeValues() {
				MultipartParseResult result = parseRequest(request);
				setParameterMap(result.getParams());
				setFileMap(result.getMultipartFiles());
			}
		};
	}

	@SuppressWarnings("unchecked")
	protected MultipartParseResult parseRequest(Request request) {
		try {

			String charset = determineCharset(request);
			FileUpload fileUpload = prepareFileUpload(charset);
			List<FileItem> fileItems = fileUpload.parseRequest(new CommonsMulitipartRequestContext(request));
			return parseFileItems(fileItems, charset);
		} catch (Exception e) {
			return null;
		}

	}

	private String determineCharset(Request request) {
		String charset = request.getCharset();
		if (StringUtils.isBlank(charset)) {
			throw new IllegalStateException("None charsetEncoding found on request!");
		}

		return charset;
	}

	/**
	 * 
	 * @param charset
	 * @return
	 */
	protected FileUpload prepareFileUpload(String charset) {
		CommonsRequestFileUpload fileUpload = new CommonsRequestFileUpload(fileItemFactory);
		fileUpload.setFileSizeMax(1024 * 1024 * 8);
		fileUpload.setHeaderEncoding(charset);
		return fileUpload;
	}

	private MultipartParseResult parseFileItems(List<FileItem> fileItems, String charset) {

		Map<String, MultipartRequestFile> multipartFiles = Maps.newHashMap();
		Map<String, String> params = Maps.newHashMap();

		for (FileItem item : fileItems) {

			if (item.isFormField()) {
				String name = item.getFieldName();
				String value = null;

				try {
					value = item.getString(charset);
				} catch (Exception e) {
					value = item.getString();
				}

				params.put(name, value);
			}

			// multipart type
			else {
				CommonsMultipartRequestFile multipart = new CommonsMultipartRequestFile(item);
				multipartFiles.put(item.getFieldName(), multipart);
			}
		}

		return new MultipartParseResult(params, multipartFiles);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kissme.photo.infrastructure.http.multipart.MultipartRequestResolver#cleanupMultipart(com.kissme.photo.infrastructure.http.multipart.MultipartRequest)
	 */
	public void cleanupMultipart(MultipartRequest request) {
		if (null != request) {
			cleanupMultipartFiles(request.getFileMap());
		}
	}

	private void cleanupMultipartFiles(Map<String, MultipartRequestFile> fileMap) {
		for (MultipartRequestFile file : fileMap.values()) {
			if (file instanceof CommonsMultipartRequestFile) {
				CommonsMultipartRequestFile cmf = (CommonsMultipartRequestFile) file;
				cmf.getFileItem().delete();
			}
		}
	}

	final class MultipartParseResult {
		private final Map<String, String> params;
		private final Map<String, MultipartRequestFile> multipartFiles;

		MultipartParseResult(Map<String, String> params, Map<String, MultipartRequestFile> multipartFiles) {
			this.params = params;
			this.multipartFiles = multipartFiles;
		}

		Map<String, String> getParams() {
			return params;
		}

		Map<String, MultipartRequestFile> getMultipartFiles() {
			return multipartFiles;
		}

	}

	abstract class CommonsNettyMultipartHttpRequest extends MultipartRequest {

		private Map<String, MultipartRequestFile> multipartFiles;
		private Map<String, String> params;

		private final Map<String, String> pathVariables = Maps.newConcurrentMap();

		public CommonsNettyMultipartHttpRequest(Request delegate) {
			super(delegate);
			initializeValues();
		}

		@Override
		public Iterator<String> getFileFieldNames() {
			return multipartFiles.keySet().iterator();
		}

		@Override
		public MultipartRequestFile getFile(String fieldName) {
			return getFileMap().get(fieldName);
		}

		@Override
		public Map<String, MultipartRequestFile> getFileMap() {
			return Collections.unmodifiableMap(multipartFiles);
		}

		/**
		 * 
		 * @param files
		 */
		protected void setFileMap(Map<String, MultipartRequestFile> files) {
			this.multipartFiles = files;
		}

		@Override
		public String getParameter(String name) {
			return params.get(name);
		}

		@Override
		public Map<String, String> getParameterMap() {
			return Collections.unmodifiableMap(params);
		}

		/**
		 * 
		 * @param params
		 */
		protected void setParameterMap(Map<String, String> params) {
			this.params = params;
		}

		@Override
		public Map<String, String> getPathVariables() {
			return pathVariables;
		}

		protected abstract void initializeValues();
	}

}
