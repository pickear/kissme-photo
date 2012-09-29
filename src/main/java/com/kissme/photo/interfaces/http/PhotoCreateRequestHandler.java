package com.kissme.photo.interfaces.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.jboss.netty.handler.codec.http.HttpMethod;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.kissme.photo.application.app.AppService;
import com.kissme.photo.application.gallery.GalleryService;
import com.kissme.photo.application.photo.PhotoService;
import com.kissme.photo.domain.app.App;
import com.kissme.photo.domain.gallery.Gallery;
import com.kissme.photo.domain.photo.Photo;
import com.kissme.photo.domain.photo.PhotoThumbConf;
import com.kissme.photo.infrastructure.http.Request;
import com.kissme.photo.infrastructure.http.Response;
import com.kissme.photo.infrastructure.http.multipart.MultipartRequest;
import com.kissme.photo.infrastructure.http.multipart.MultipartRequestFile;
import com.kissme.photo.infrastructure.util.FileUtils;
import com.kissme.photo.infrastructure.util.FileUtils.FileType;
import com.kissme.photo.infrastructure.util.JsonUtils;
import com.kissme.photo.interfaces.http.exception.BadRequestException;

/**
 * 
 * @author loudyn
 * 
 */
public class PhotoCreateRequestHandler extends AbstractAppRequiredRequestHandler {

	private static final ImmutableList<FileType> ACCEPT_FILE_TYPES = ImmutableList.of(FileType.BMP, FileType.GIF, FileType.ICO, FileType.JPG, FileType.PNG);
	private PhotoService photoService;
	private GalleryService galleryService;

	@Inject
	public PhotoCreateRequestHandler(AppService appService, GalleryService galleryService, PhotoService photoService) {
		super(appService);
		this.galleryService = galleryService;
		this.photoService = photoService;
	}

	@Override
	public String getMapping() {
		return "/photo/";
	}

	@Override
	public HttpMethod[] getMappingMethods() {
		return new HttpMethod[] { HttpMethod.POST };
	}

	@Override
	protected String doHandleAppRequest(App app, Request request, Response response) {
		String galleryId = request.getParameter("gallery");
		if (StringUtils.isBlank(galleryId)) {
			throw new BadRequestException();
		}

		final Gallery gallery = galleryService.getByAppAndId(app.getId(), galleryId);
		if (null == gallery) {
			throw new BadRequestException();
		}

		MultipartRequest mpr = (MultipartRequest) request;
		Iterator<MultipartRequestFile> files = Iterators.filter(mpr.getFileMap().values().iterator(), new Predicate<MultipartRequestFile>() {

			@Override
			public boolean apply(MultipartRequestFile input) {
				try {

					byte[] bytes = input.getBytes();
					FileType type = FileUtils.guessType(bytes);
					return ACCEPT_FILE_TYPES.contains(type);
				} catch (Exception e) {
					return false;
				}
			}
		});

		PhotoThumbConf conf = JsonUtils.newfor(request.getParameterMap(), PhotoThumbConf.class);
		PhotoCreateResult result = new PhotoCreateResult();

		while (files.hasNext()) {
			try {

				final MultipartRequestFile file = files.next();
				Photo entity = new Photo() {

					private static final long serialVersionUID = 1L;

					@Override
					@JsonIgnore
					public Gallery getGallery() {
						return gallery;
					}

					@Override
					public String getFilename() {
						return file.getOriginalFilename();
					}

					@Override
					public String getContentType() {
						return file.getContentType();
					}

					@Override
					public long getLength() {
						return file.getSize();
					}

					@Override
					public InputStream getInputStream(PhotoThumbConf conf) throws IOException {
						return new ByteArrayInputStream(getBytes(conf));
					}

					@Override
					public byte[] getBytes(PhotoThumbConf conf) throws IOException {
						boolean thumb = false;
						Builder<? extends InputStream> builder = Thumbnails.of(file.getInputStream());

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

						return file.getBytes();
					}

				};

				photoService.save(entity, conf);
				result.oneMoreResult(entity);
			} catch (Exception e) {
				result.oneMoreFailure();
			}
		}

		return JsonUtils.toJsonString(result);
	}
	
	class PhotoCreateResult {
		private int success = 0;
		private int failure = 0;

		private List<Photo> result = Lists.newLinkedList();

		public int getSuccess() {
			return success;
		}

		public int getFailure() {
			return failure;
		}

		public PhotoCreateResult oneMoreFailure() {
			failure++;
			return this;
		}

		public List<Photo> getResult() {
			return result;
		}

		public PhotoCreateResult oneMoreResult(Photo entity) {
			success++;
			result.add(entity);
			return this;
		}
	}

}
