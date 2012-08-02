package com.kissme.photo.infrastructure.http.transport;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;

import com.kissme.photo.infrastructure.http.DefaultRequestDispatcher;
import com.kissme.photo.infrastructure.http.Request;
import com.kissme.photo.infrastructure.http.Response;
import com.kissme.photo.infrastructure.ioc.GuiceIoc;

/**
 * 
 * @author loudyn
 * 
 */
public class NettyRequestTransport extends SimpleChannelUpstreamHandler {

	private final Log log = LogFactory.getLog(NettyRequestTransport.class);
	private DefaultRequestDispatcher requestDispatcher;

	/**
	 * 
	 * @param dispatcher
	 */
	public NettyRequestTransport() {
		requestDispatcher = new DefaultRequestDispatcher(new GuiceIoc());
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent evt) throws Exception {

		Request request = null;
		Response response = null;

		try {

			request = prepareRequest(evt);
			response = prepareResponse();
			requestDispatcher.dispatch(request, response);
		} catch (Exception e) {
			if (null != response) {
				response.sendError(500);
			}
		}

		try {

			prepareWritingResponse(request, response);
			writeResponse(evt, request, response);
		} finally {
			completeRequest(ctx, evt, request, response);
		}
	}

	private Request prepareRequest(MessageEvent evt) {
		return Requests.newRequest((HttpRequest) evt.getMessage());
	}

	private Response prepareResponse() {
		HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
		return Responses.newResponse(response);
	}

	private void prepareWritingResponse(Request request, Response response) {
		if (isBrowserRequest(request)) {
			response.addHeader("Access-Control-Allow-Origin", "*");
		}

		response.setCharset(request.getCharset());
		response.setHeader(HttpHeaders.Names.CONTENT_TYPE, response.getContentType());
		response.setHeader(HttpHeaders.Names.CONTENT_LENGTH, response.getContent().readableBytes());
	}

	private boolean isBrowserRequest(Request request) {
		String userAgent = request.getHeader(HttpHeaders.Names.USER_AGENT);
		return StringUtils.indexOf(userAgent, "Mozilla") != -1;
	}

	private void writeResponse(MessageEvent evt, Request request, Response response) {
		ChannelFuture future = evt.getChannel().write(response);

		if (!isKeepAlive(request, response)) {
			future.addListener(ChannelFutureListener.CLOSE);
		}
	}

	private boolean isKeepAlive(Request request, Response response) {
		return request.isKeepAlive() || response.isKeepAlive();
	}

	private void completeRequest(ChannelHandlerContext ctx, MessageEvent evt, Request request, Response response) {}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		log.error("writing response occur error.", e.getCause());
		e.getChannel().close();
	}

}
