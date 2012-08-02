package com.kissme.photo.interfaces.photo;

import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.jboss.netty.handler.codec.http.HttpMethod;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.google.inject.Inject;
import com.kissme.photo.application.GalleryService;
import com.kissme.photo.application.PhotoService;
import com.kissme.photo.domain.gallery.Gallery;
import com.kissme.photo.domain.photo.Photo;
import com.kissme.photo.infrastructure.Files;
import com.kissme.photo.infrastructure.Files.FileType;
import com.kissme.photo.infrastructure.Jsons;
import com.kissme.photo.infrastructure.http.Request;
import com.kissme.photo.infrastructure.http.Response;
import com.kissme.photo.infrastructure.http.multipart.MultipartRequest;
import com.kissme.photo.infrastructure.http.multipart.MultipartRequestFile;
import com.kissme.photo.interfaces.AbstractJsonpRequestHandler;
import com.kissme.photo.interfaces.exception.BadRequestException;
import com.kissme.photo.interfaces.exception.ResourceNotFoundException;

/**
 * 
 * @author loudyn
 * 
 */
public class CreatePhotoRequestHandler extends AbstractJsonpRequestHandler {

	private static final ImmutableList<FileType> ACCEPT_FILE_TYPES = ImmutableList.of(FileType.BMP, FileType.GIF, FileType.ICO, FileType.JPG, FileType.PNG);
	private PhotoService photoService;
	private GalleryService galleryService;

	@Inject
	public CreatePhotoRequestHandler(GalleryService galleryService, PhotoService photoService) {
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
	protected String doHandleRequest(Request request, Response response) {

		String galleryId = request.getParameter("gallery");
		if (StringUtils.isBlank(galleryId)) {
			throw new BadRequestException();
		}

		Gallery gallery = galleryService.get(galleryId);
		if (null == gallery) {
			throw new ResourceNotFoundException();
		}

		MultipartRequest mpr = (MultipartRequest) request;
		Iterator<MultipartRequestFile> files = Iterators.filter(mpr.getFileMap().values().iterator(), new Predicate<MultipartRequestFile>() {

			@Override
			public boolean apply(MultipartRequestFile input) {
				try {

					byte[] bytes = input.getBytes();
					if (bytes.length <= 32) {
						return false;
					}
					
					FileType type = Files.guess(bytes);
					return ACCEPT_FILE_TYPES.contains(type);
				} catch (Exception e) {
					return false;
				}
			}
		});

		if (files.hasNext()) {

			try {

				MultipartRequestFile file = files.next();
				Photo entity = new Photo();
				entity.setGallery(gallery).setFilename(file.getOriginalFilename());
				entity.setContent(file.getBytes()).setContentType(file.getContentType()).setLength(file.getSize());
				photoService.save(entity);
				return Jsons.toJsonString(entity);
			} catch (Exception ingore) {}
		}

		return "{}";
	}

}
