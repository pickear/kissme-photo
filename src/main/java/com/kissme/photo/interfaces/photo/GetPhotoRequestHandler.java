package com.kissme.photo.interfaces.photo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMethod;

import com.google.inject.Inject;
import com.kissme.photo.application.PhotoService;
import com.kissme.photo.domain.photo.Photo;
import com.kissme.photo.domain.photo.PhotoThumbConf;
import com.kissme.photo.infrastructure.Exceptions;
import com.kissme.photo.infrastructure.Jsons;
import com.kissme.photo.infrastructure.http.Request;
import com.kissme.photo.infrastructure.http.RequestHandler;
import com.kissme.photo.infrastructure.http.Response;

/**
 * 
 * @author loudyn
 * 
 */
public class GetPhotoRequestHandler implements RequestHandler {

	private static final int MAX_AGE_SECONDS = 60 * 60 * 24 * 30;

	private static final SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH);
	private PhotoService galleryPhotoService;

	@Inject
	public GetPhotoRequestHandler(PhotoService galleryPhotoService) {
		this.galleryPhotoService = galleryPhotoService;
	}

	@Override
	public String getMapping() {
		return "/photo/{id}/";
	}

	@Override
	public HttpMethod[] getMappingMethods() {
		return new HttpMethod[] { HttpMethod.GET };
	}

	@Override
	public void handleRequest(Request request, Response response) {

		// e... there is something wrong..
		try {

			String id = request.getPathVariables().get("id");
			Photo entity = galleryPhotoService.get(id);
			PhotoThumbConf conf = Jsons.newfor(request.getParameterMap(), PhotoThumbConf.class);

			response.addHeader(HttpHeaders.Names.CACHE_CONTROL, maxAgeCacheControl());
			response.addHeader(HttpHeaders.Names.EXPIRES, expiresAt(entity));
			response.addHeader(HttpHeaders.Names.LAST_MODIFIED, lastModifiedAt(entity));

			response.setContentType(entity.getContentType());
			response.setContent(ChannelBuffers.copiedBuffer(entity.getBytes(conf)));
		} catch (Exception e) {
			throw Exceptions.uncheck(e);
		}
	}

	private String maxAgeCacheControl() {
		return "max-age=" + MAX_AGE_SECONDS;
	}

	private String expiresAt(Photo entity) {
		Calendar c = Calendar.getInstance(Locale.ENGLISH);
		c.setTime(entity.getLastModified());
		c.add(Calendar.SECOND, MAX_AGE_SECONDS);
		return formatGMTString(c.getTime());
	}

	private String formatGMTString(Date time) {
		return sdf.format(time) + " GMT";
	}

	private String lastModifiedAt(Photo entity) {
		return formatGMTString(entity.getLastModified());
	}

}
