package com.kissme.photo;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import static org.apache.commons.cli.OptionBuilder.*;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpContentCompressor;
import org.jboss.netty.handler.codec.http.HttpServerCodec;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.kissme.photo.infrastructure.http.transport.NettyRequestTransport;

/**
 * 
 * @author loudyn
 * 
 */
public class Bootstrap {
	private static final Log LOG = LogFactory.getLog(Bootstrap.class);

	@SuppressWarnings("static-access")
	public static void main(String[] args) {

		int port = 9104;
		Option portOpt = withArgName("port").hasArg().withDescription("the port listening, default on 9104").isRequired(false).create("p");
		Option helpOpt = withArgName("help").hasArg(false).withDescription("print the help").isRequired(false).create("h");

		Options options = new Options().addOption(portOpt).addOption(helpOpt);
		CommandLineParser parser = new GnuParser();
		try {

			CommandLine line = parser.parse(options, args);
			if (line.hasOption("h")) {
				printHelp(options);
				return;
			}

			if (line.hasOption("p")) {
				port = Integer.parseInt(line.getOptionValue("p"));
			}
		} catch (Exception e) {
			printHelp(options);
			return;
		}

		final Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(NettyRequestTransport.class).asEagerSingleton();
			}
		});

		ServerBootstrap server = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));

		try {

			server.setPipelineFactory(new ChannelPipelineFactory() {

				@Override
				public ChannelPipeline getPipeline() throws Exception {
					ChannelPipeline pipeline = Channels.pipeline();
					pipeline.addLast("httpServerCodec", new HttpServerCodec());
					pipeline.addLast("httpChunkAggregator", new HttpChunkAggregator(1024 * 1024 * 8));
					pipeline.addLast("httpContentCompressor", new HttpContentCompressor());
					pipeline.addLast("nettyRequestTransport", injector.getInstance(NettyRequestTransport.class));
					return pipeline;
				}
			});

			server.bind(new InetSocketAddress(port));
		} catch (Exception e) {
			LOG.error("Bootstrap occur error.", e);
			System.exit(1);
		}

		final CountDownLatch keepAliveSignal = new CountDownLatch(1);
		Runtime.getRuntime().addShutdownHook(new Thread() {

			@Override
			public void run() {
				keepAliveSignal.countDown();
			}

		});

		Thread keepAliveThread = new Thread() {

			@Override
			public void run() {
				try {

					keepAliveSignal.await();
				} catch (Exception e) {}
			}
		};

		keepAliveThread.start();
	}

	private static void printHelp(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("java -jar photo.jar -p 8888", options);
	}
}
