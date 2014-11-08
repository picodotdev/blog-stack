package info.blogstack.cli;

import info.blogstack.misc.Globals;
import info.blogstack.misc.Globals.Environment;
import info.blogstack.persistence.jooq.Keys;
import info.blogstack.persistence.jooq.tables.records.LabelRecord;
import info.blogstack.persistence.jooq.tables.records.PostRecord;
import info.blogstack.persistence.jooq.tables.records.PostsLabelsRecord;
import info.blogstack.persistence.jooq.tables.records.SourceRecord;
import info.blogstack.persistence.records.AppPostRecord;
import info.blogstack.services.GenerateModule;
import info.blogstack.services.MainService;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.resource.FileResourceManager;
import io.undertow.server.handlers.resource.ResourceHandler;
import io.undertow.util.Headers;
import io.undertow.util.MimeMappings;

import java.io.File;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;
import org.apache.tapestry5.modules.TapestryModule;
import org.apache.tapestry5.services.ServletApplicationInitializer;
import org.joda.time.DateTimeZone;
import org.jooq.DSLContext;
import org.lazan.t5.offline.services.TapestryOfflineModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ro.isdc.wro.config.Context;
import ro.isdc.wro.config.jmx.WroConfiguration;

public class Main {

	private static Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) throws Exception {
		logger.info("Starting...");
		DateTimeZone.setDefault(DateTimeZone.UTC);

		Main main = new Main();
		Runtime.getRuntime().addShutdownHook(new Thread(new ShutdownHook(main)));
		main.process(args);
	}

	private DSLContext context;

	private Undertow server;
	private Undertow adminServer;

	public Main() {
	}

	public void process(String[] args) throws Exception {
		processCommandLine(buildCommandLine(args));
	}

	public void shutdown() {
		logger.info("Ending...");

		if (Globals.registry != null) {
			Globals.registry.shutdown();
			Globals.registry = null;
		}

		if (server != null) {
			server.stop();
			server = null;
		}
		if (adminServer != null) {
			adminServer.stop();
			adminServer = null;
		}
	}

	private void initRegistry() {
		if (Globals.registry != null) {
			return;
		}
		logger.info("Starting Tapestry registry...");
		Globals.registry = buildRegistry();
	}

	private void initPersitence() {
		logger.info("Starting persistence...");
		context = Globals.registry.getService(DSLContext.class);
	}

	private Registry buildRegistry() {
		ServletContext sc = new ServletContextImpl("/", "src/main/webapp");

		RegistryBuilder builder = new RegistryBuilder();
		builder.add(TapestryModule.class, TapestryOfflineModule.class, GenerateModule.class);
		builder.add(new SpringModuleDef("applicationContext.xml"));

		Registry registry = builder.build();
		registry.performRegistryStartup();

		ServletApplicationInitializer sai = registry.getService("ServletApplicationInitializer", ServletApplicationInitializer.class);
		sai.initializeApplication(sc);

		return registry;
	}

	private CommandLine buildCommandLine(String[] args) throws ParseException {
		Options options = new Options();
		options.addOption("e", "environment", true, "Execution environment");
		options.addOption("h", "hash", false, "Rehash the content");
		options.addOption("i", "index", false, "Index the new content of all sources");
		options.addOption("ii", "iindex", false, "Reindex al the content of all sources");
		options.addOption("im", "import", false, "Index the content of the pending import sources");
		options.addOption("iim", "iimport", false, "Reindex the content of the import sources");
		options.addOption("g", "generate", false, "Generate the content changed since the last indexation");
		options.addOption("gg", "ggenerate", false, "Regenerate all the content");
		options.addOption("gi", "gindex", false, "Regenerate index pages");
		options.addOption("gl", "glabels", false, "Regenerate label pages");
		options.addOption("gf", "gfeeds", false, "Regenerate feeds");
		options.addOption("ga", "garchive", false, "Regenerate archive pages");
		options.addOption("gs", "gstatics", false, "Regenerate statics");
		options.addOption("gp", "gpages", false, "Regenerate pages");
		options.addOption("gsm", "gsitemap", false, "Regenerate sitemap");
		options.addOption("sh", "share", false, "Share new indexed content");
		options.addOption("p", "preview", false, "Preview the content");
		options.addOption("s", "start", false, "Start the server");
		options.addOption("ss", "stop", false, "Stop the server");
		options.addOption("r", "repository", true, "Repository of the content");

		Parser parser = new BasicParser();
		CommandLine cmd = parser.parse(options, args);

		return cmd;
	}

	private void processCommandLine(CommandLine cmd) throws Exception {
		String environment = cmd.getOptionValue("e");
		boolean hash = cmd.hasOption("h");
		boolean index = cmd.hasOption("i");
		boolean iindex = cmd.hasOption("ii");
		boolean importOption = cmd.hasOption("im");
		boolean iimportOption = cmd.hasOption("iim");
		boolean generate = cmd.hasOption("g");
		boolean ggenerate = cmd.hasOption("gg");
		boolean gindex = cmd.hasOption("gi");
		boolean glabels = cmd.hasOption("gl");
		boolean gfeeds = cmd.hasOption("gf");
		boolean garchive = cmd.hasOption("ga");
		boolean gstatics = cmd.hasOption("gs");
		boolean gpages = cmd.hasOption("gp");
		boolean gsitemap = cmd.hasOption("gsm");
		boolean share = cmd.hasOption("sh");
		boolean preview = cmd.hasOption("p");
		boolean serverOption = cmd.hasOption("s");
		boolean stop = cmd.hasOption("ss");
		String repository = cmd.getOptionValue("r");
		
		Globals.environment = (environment != null) ? Environment.valueOf(environment.toUpperCase()) : Environment.DEVELOPMENT;
		logger.info("Executing in {}...", Globals.environment);

		if (hash) {
			initRegistry();
			initPersitence();

			MainService service = Globals.registry.getService(MainService.class);

			service.getIndexService().hash();
		}

		Collection<PostRecord> posts = new LinkedHashSet<>();
		Collection<LabelRecord> labels = new LinkedHashSet<>();
		if (index || iindex || importOption || iimportOption) {
			initRegistry();
			initPersitence();

			MainService service = Globals.registry.getService(MainService.class);

			logger.info("Indexing...");
			service.getIndexService().setForceIndex(iindex);
			service.getIndexService().setForceImport(iimportOption);
			if (importOption || iimportOption) {
				List<PostRecord> p = service.getIndexService().importSources();
				posts.addAll(p);
			}
			if (index || iindex) {
				List<PostRecord> p = service.getIndexService().index();
				posts.addAll(p);
			}
			for (PostRecord post : posts) {
				for (PostsLabelsRecord pl : post.fetchChildren(Keys.POSTS_LABELS_POST_ID)) {
					labels.add(pl.fetchParent(Keys.POSTS_LABELS_LABEL_ID));					
				}
			}
		}

		if (generate || ggenerate || gindex || glabels || gfeeds || garchive || gstatics || gpages) {
			initRegistry();
			initPersitence();

			MainService service = Globals.registry.getService(MainService.class);
			posts = (ggenerate || gindex || glabels || gfeeds || garchive) ? service.getPostDAO().findAll() : posts;
			labels = (ggenerate || gindex || glabels || gfeeds || garchive) ? service.getLabelDAO().findAll() : labels;
			if (!posts.isEmpty()) {
				logger.info("Generating pages...");
				if (generate || ggenerate || gindex) {
					List<File> pindex = service.getGenerateService().generateIndex();
				}
				if (generate || ggenerate || glabels) {
					List<File> plabels = service.getGenerateService().generateLabels(new ArrayList(labels));
				}
			}

			if (ggenerate || gpages) {
				File faqPage = service.getGenerateService().generatePage("faq", new Object[0], Collections.EMPTY_MAP);
			}

			if (generate || ggenerate) {
				if (!posts.isEmpty()) {
					logger.info("Generating posts...");
					List<File> ps = service.getGenerateService().generatePosts(new ArrayList(posts));

					Set<SourceRecord> sources = new HashSet<>();
					for (PostRecord post : posts) {
						sources.add(post.fetchParent(Keys.POST_SOURCE_ID));
					}
					List<SourceRecord> s = new ArrayList(sources);
					s.add(null);
					List<File> ss = service.getGenerateService().generateLastPosts(s);
				}
			}

			if (!posts.isEmpty()) {
				if (generate|| ggenerate || garchive) {
					logger.info("Generating archive...");
					List<File> as = service.getGenerateService().generateArchive(posts);
				}
			}

			if (!labels.isEmpty()) {
				if (generate || ggenerate || gfeeds) {
					logger.info("Generating feeds...");
					File mainAtom = service.getGenerateService().generateRss();
					for (LabelRecord label : labels) {
						File labelAtom = service.getGenerateService().generateRss(label);
					}
				}
			}

			if (!posts.isEmpty()) {
				if (generate || ggenerate || gsitemap) {
					logger.info("Generating sitemap...");
					File sitemap = service.getGenerateService().generateSitemap();
				}
			}

			if (ggenerate || gstatics) {
				logger.info("Generating statics...");
				WroConfiguration config = new WroConfiguration();
				Context.set(Context.standaloneContext(), config);
				try {
					service.getGenerateService().generateStatics(Globals.STATICS);
				} finally {
					Context.unset();
				}
			}

			logger.info("Generating last updated...");
			service.getGenerateService().generateLastUpdated();
		}
		
		if (share) {
			logger.info("Sharing...");
			MainService service = Globals.registry.getService(MainService.class);
			
			// Share only fresh posts
			Collection<PostRecord> fp = new ArrayList<>();
			for (PostRecord p : posts) {
				AppPostRecord ap = p.into(AppPostRecord.class);
				if (ap.isFresh()) {
					fp.add(ap);
				}
			}
			
			service.getShareService().share(fp);
		}

		if (preview) {
			if (isStarted()) {
				logger.info("Server already started");
				return;
			}

			logger.info("Stating server...");

			String host = getHost();
			int port = getPort();
			int amdinPort = getAdminPort();

			File r = (repository == null) ? Globals.PUBLIC : new File(repository);

			ResourceHandler handler = new ResourceHandler(new FileResourceManager(r, 1024 * 10));
			HttpHandler adminHandler = new HttpHandler() {
				@Override
				public void handleRequest(final HttpServerExchange exchange) throws Exception {
					exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
					exchange.getResponseSender().send("Stoping server...");
					System.exit(0);
				}
			};

			MimeMappings.Builder builder = MimeMappings.builder(true);
			builder.addMapping("atom.xml", "application/atom+xml");
			handler.setMimeMappings(builder.build());
			server = Undertow.builder().addHttpListener(port, host).setHandler(handler).build();
			adminServer = Undertow.builder().addHttpListener(amdinPort, host).setHandler(adminHandler).build();

			server.start();
			adminServer.start();

			logger.info(String.format("Server listening in http://%s:%d/", host, port));
		}

		if (serverOption) {
			logger.info("Stating server...");

			String host = getHost();
			int port = getPort();
			int amdinPort = getAdminPort();

			HttpHandler handler = new HttpHandler() {
				@Override
				public void handleRequest(final HttpServerExchange exchange) throws Exception {
					exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
					exchange.getResponseSender().send("Server running");
					System.exit(0);
				}
			};

			server = Undertow.builder().addHttpListener(port, host).setHandler(handler).build();

			server.start();

			logger.info(String.format("Server listening in http://%s:%d/", host, port));
		}

		if (stop) {
			if (!isStarted()) {
				logger.info("Server already stopped");
				return;
			}

			logger.info("Stopping server...");

			String host = getHost();
			int adminPort = getAdminPort();

			HttpClient cliente = HttpClients.createDefault();
			HttpGet get = new HttpGet(String.format("http://%s:%d/", host, adminPort));
			HttpResponse response1 = cliente.execute(get);

			String response = IOUtils.toString(response1.getEntity().getContent());
			logger.info(String.format("Response: %s", response));
		}
	}

	public static class ShutdownHook implements Runnable {

		private Main main;

		public ShutdownHook(Main main) {
			this.main = main;
		}

		@Override
		public void run() {
			main.shutdown();
		}
	}

	private boolean isStarted() {
		try {
			ServerSocket socket = new ServerSocket(getPort(), 0, InetAddress.getByName(getHost()));
			socket.close();
			return false;
		} catch (Exception e) {
			return true;
		}
	}

	private String getHost() {
		String host = "localhost";
		int port = 4001;
		if (System.getenv("OPENSHIFT_DIY_IP") != null) {
			host = System.getenv("OPENSHIFT_DIY_IP");
		}
		return host;
	}

	private int getPort() {
		int port = 4001;
		if (System.getenv("OPENSHIFT_DIY_PORT") != null) {
			port = Integer.valueOf(System.getenv("OPENSHIFT_DIY_PORT"));
		}
		return port;
	}

	private int getAdminPort() {
		return 4002;
	}
}