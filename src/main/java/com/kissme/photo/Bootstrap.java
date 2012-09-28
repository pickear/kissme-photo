package com.kissme.photo;

import static org.apache.commons.cli.OptionBuilder.withArgName;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpContentCompressor;
import org.jboss.netty.handler.codec.http.HttpServerCodec;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.kissme.photo.application.ApplicationModule;
import com.kissme.photo.infrastructure.InfrastructureModule;
import com.kissme.photo.infrastructure.http.transport.NettyRequestTransport;
import com.kissme.photo.infrastructure.ioc.GuiceIoc;
import com.kissme.photo.infrastructure.ioc.Ioc;
import com.kissme.photo.infrastructure.util.ExceptionUtils;
import com.kissme.photo.infrastructure.util.JsonUtils;
import com.kissme.photo.interfaces.InterfacesModule;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoOptions;
import com.mongodb.ServerAddress;

/**
 * 
 * @author loudyn
 * 
 */
public class Bootstrap {
	private static final Log LOG = LogFactory.getLog(Bootstrap.class);

	public static class Setting {
		private int port = 9104;
		private int maxThreads = 100;
		private int chunkSize = 1024 * 1024 * 8;

		public int getPort() {
			return port;
		}

		public void port(int port) {
			this.port = port;
		}

		public int getMaxThreads() {
			return maxThreads;
		}

		public void maxThreads(int maxThreads) {
			this.maxThreads = maxThreads;
		}

		public int getChunkSize() {
			return chunkSize;
		}

		public void chunkSize(int chunkSize) {
			this.chunkSize = chunkSize;
		}

	}

	public static class MongoDBBuilder {
		private String address = "127.0.0.1";
		private int port = 27017;
		private String dbname = "photo";
		private String username;
		private String password;

		private boolean autoConnectRetry = true;
		private boolean socketKeepAlive = true;
		private int connectionsPerHost = 100;
		private int connectTimeout = 3000;
		private int maxAutoConnectRetryTime = 3;

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public int getPort() {
			return port;
		}

		public void setPort(int port) {
			this.port = port;
		}

		public String getDbname() {
			return dbname;
		}

		public void setDbname(String dbname) {
			this.dbname = dbname;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public boolean isAutoConnectRetry() {
			return autoConnectRetry;
		}

		public void setAutoConnectRetry(boolean autoConnectRetry) {
			this.autoConnectRetry = autoConnectRetry;
		}

		public boolean isSocketKeepAlive() {
			return socketKeepAlive;
		}

		public void setSocketKeepAlive(boolean socketKeepAlive) {
			this.socketKeepAlive = socketKeepAlive;
		}

		public int getConnectionsPerHost() {
			return connectionsPerHost;
		}

		public void setConnectionsPerHost(int connectionsPerHost) {
			this.connectionsPerHost = connectionsPerHost;
		}

		public int getConnectTimeout() {
			return connectTimeout;
		}

		public void setConnectTimeout(int connectTimeout) {
			this.connectTimeout = connectTimeout;
		}

		public int getMaxAutoConnectRetryTime() {
			return maxAutoConnectRetryTime;
		}

		public void setMaxAutoConnectRetryTime(int maxAutoConnectRetryTime) {
			this.maxAutoConnectRetryTime = maxAutoConnectRetryTime;
		}

		public DB build() {
			MongoOptions options = new MongoOptions();
			options.autoConnectRetry = isAutoConnectRetry();
			options.connectionsPerHost = getConnectionsPerHost();
			options.connectTimeout = getConnectTimeout();
			options.maxAutoConnectRetryTime = getMaxAutoConnectRetryTime();
			options.socketKeepAlive = isSocketKeepAlive();

			try {

				Mongo mongo = new Mongo(new ServerAddress(getAddress(), getPort()), options);
				DB db = mongo.getDB(getDbname());
				if(requiredAuth()){
					doDBAuth(db);
				}
				
				Exception e = db.getStats().getException();
				if(null != e){
					throw e;
				}
				
				return db;
			} catch (Exception e) {
				throw ExceptionUtils.oneThrow("Can't connect to the db! error: " + e.getMessage());
			}
		}

		private void doDBAuth(DB db) {
			if(!db.authenticate(getUsername(), getPassword().toCharArray())){
				throw ExceptionUtils.oneThrow("Can't authenticate the db,please check the username and password");
			}
		}

		private boolean requiredAuth() {
			return StringUtils.isNotBlank(getUsername()) && StringUtils.isNotBlank(getPassword());
		}
	}

	@SuppressWarnings("static-access")
	public static void main(String[] args) {

		final Setting setting = new Setting();
		final MongoDBBuilder builder = new MongoDBBuilder();

		Option helpOpt = withArgName("help").withDescription("print the help").hasArg(false).isRequired(false).create("h");

		Option portOpt = withArgName("port").withDescription("the port listening, default on " + setting.getPort())
											.hasArg()
											.isRequired(false)
											.create("p");

		Option maxThreadsOpt = withArgName("maxThreads").withDescription("max threads to handle,default is " + setting.getMaxThreads())
														.hasArg()
														.isRequired(false)
														.create("t");
		Option chunksizeOpt = withArgName("chunksize").withDescription("max chunk size,default is " + setting.getChunkSize() + " bytes")
														.hasArg()
														.isRequired(false)
														.create("chunksize");

		Option dbconfOpt = withArgName("dbconfpath").withDescription("db configure path,it must be a utf-8 json file")
													.hasArg()
													.isRequired(false)
													.create("dbconfpath");

		Options options = new Options().addOption(portOpt).addOption(helpOpt).addOption(maxThreadsOpt).addOption(chunksizeOpt).addOption(dbconfOpt);
		CommandLineParser parser = new GnuParser();
		try {

			CommandLine line = parser.parse(options, args);
			if (line.hasOption("h")) {
				printHelp(options);
				return;
			}

			if (line.hasOption("p")) {
				setting.port(Integer.parseInt(line.getOptionValue("p")));
			}

			if (line.hasOption("t")) {
				setting.maxThreads(Integer.parseInt(line.getOptionValue("t")));
			}

			if (line.hasOption("dbconfpath")) {
				String dbconfPath = line.getOptionValue("dbconfpath");
				MongoDBBuilder newBuilder = createNewBuilder(dbconfPath);
				BeanUtils.copyProperties(builder, newBuilder);
			}

			if (line.hasOption("chunksize")) {
				setting.chunkSize(Integer.parseInt(line.getOptionValue("chunksize")));
			}

		} catch (Exception e) {
			LOG.error(e.getMessage());
			printHelp(options);
			return;
		}
		
		try {
			
			List<Module> modules = Lists.newArrayList();
			modules.add(new InterfacesModule());
			modules.add(new ApplicationModule());
			modules.add(new InfrastructureModule());
			
			// create the mongodb
			final DB db = builder.build();
			modules.add(new AbstractModule() {
				
				@Override
				protected void configure() {
					bind(DB.class).toInstance(db);
				}
			});
			
			// create the ioc and netty http request transport
			Ioc ioc = new GuiceIoc(modules);
			final NettyRequestTransport transport = new NettyRequestTransport(ioc);
			final ChannelFactory factory = new NioServerSocketChannelFactory(
																				Executors.newFixedThreadPool(setting.getMaxThreads()),
																				Executors.newFixedThreadPool(setting.getMaxThreads())
																		);
			
			ServerBootstrap server = new ServerBootstrap(factory);
			server.setPipelineFactory(new ChannelPipelineFactory() {

				@Override
				public ChannelPipeline getPipeline() throws Exception {
					ChannelPipeline pipeline = Channels.pipeline();
					pipeline.addLast("httpServerCodec", new HttpServerCodec());
					pipeline.addLast("httpChunkAggregator", new HttpChunkAggregator(setting.getChunkSize()));
					pipeline.addLast("httpContentCompressor", new HttpContentCompressor());
					pipeline.addLast("nettyRequestTransport", transport);
					return pipeline;
				}
			});

			server.bind(new InetSocketAddress(setting.getPort()));
		} catch (Exception e) {
			LOG.error(e.getMessage());
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

	private static MongoDBBuilder createNewBuilder(String dbconfPath) {
		try {
			String jsonString = FileUtils.readFileToString(new File(dbconfPath), "utf-8");
			@SuppressWarnings("unchecked")
			Map<String, Object> jsonMap = JsonUtils.fromJsonString(jsonString, Map.class);
			return JsonUtils.newfor(jsonMap, MongoDBBuilder.class);
		} catch (Exception e) {
			throw ExceptionUtils.oneThrow("Can't open the dbconfpath,please check the filepath and make sure it is a utf-8 json file!");
		}
	}

	private static void printHelp(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("java -jar photo.jar -p 8888 -t 128 -chunksize 1024", options);
	}
}
