package info.blogstack.cli;

import info.blogstack.entities.Label;
import info.blogstack.entities.Post;
import info.blogstack.entities.Source;
import info.blogstack.misc.Globals;
import info.blogstack.services.GeneratorModule;
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
import org.apache.tapestry5.beanvalidator.modules.BeanValidatorModule;
import org.apache.tapestry5.hibernate.modules.HibernateCoreModule;
import org.apache.tapestry5.hibernate.modules.HibernateModule;
import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;
import org.apache.tapestry5.modules.TapestryModule;
import org.apache.tapestry5.services.ServletApplicationInitializer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.joda.time.DateTimeZone;
import org.lazan.t5.offline.services.TapestryOfflineModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

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

	private SessionFactory sessionFactory;
	private SessionHolder sessionHolder;

	private Undertow server;
	private Undertow adminServer;

	public Main() {
	}

	public void process(String[] args) throws Exception {
		processCommandLine(buildCommandLine(args));
	}

	public void shutdown() {
		logger.info("Ending...");

		if (sessionFactory != null && !sessionHolder.isVoid() && sessionHolder.getSession().isOpen()) {
			// Se produce una excepción
			// java.lang.IllegalStateException: No value for key
			// [org.hibernate.internal.SessionFactoryImpl@1afc14d6] bound to thread [Thread-0]
			// TransactionSynchronizationManager.unbindResource(sessionFactory);
			SessionFactoryUtils.closeSession(sessionHolder.getSession());
		}

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

	private void initSessionFactory() {
		if (sessionFactory != null) {
			return;
		}
		logger.info("Starting database...");
		sessionFactory = Globals.registry.getService(SessionFactory.class);
		Session session = sessionFactory.openSession();
		sessionHolder = new SessionHolder(session);
		TransactionSynchronizationManager.bindResource(sessionFactory, sessionHolder);
	}

	private Registry buildRegistry() {
		ServletContext sc = new ServletContextImpl("/", "src/main/webapp");

		RegistryBuilder builder = new RegistryBuilder();
		builder.add(TapestryModule.class, HibernateCoreModule.class, HibernateModule.class, BeanValidatorModule.class, TapestryOfflineModule.class, GeneratorModule.class);
		builder.add(new SpringModuleDef("applicationContext.xml"));

		Registry registry = builder.build();
		registry.performRegistryStartup();

		ServletApplicationInitializer sai = registry.getService("ServletApplicationInitializer", ServletApplicationInitializer.class);
		sai.initializeApplication(sc);

		return registry;
	}

	private CommandLine buildCommandLine(String[] args) throws ParseException {
		Options options = new Options();
		options.addOption("h", "hash", false, "Rehash the content");
		options.addOption("i", "index", false, "Index the new content of all sources");
		options.addOption("ii", "iindex", false, "Reindex al the content of all sources");
		options.addOption("im", "import", false, "Index the content of the pending import sources");
		options.addOption("iim", "iimport", false, "Reindex the content of the import sources");
		options.addOption("g", "generate", false, "Generate the content changed since the last indexation");
		options.addOption("gg", "ggenerate", false, "Regenerate all the content");
		options.addOption("gi", "gindex", false, "Regenerate index pages");
		options.addOption("gl", "glabels", false, "Regenerate label pages");
		options.addOption("gf", "gfeed", false, "Regenerate feeds");
		options.addOption("ga", "garchive", false, "Regenerate archive pages");
		options.addOption("gs", "gstatics", false, "Regenerate statics");
		options.addOption("gp", "gpages", false, "Regenerate pages");
		options.addOption("gsm", "gsitemap", false, "Regenerate sitemap");
		options.addOption("p", "preview", false, "Preview the content");
		options.addOption("s", "start", false, "Start the server");
		options.addOption("ss", "stop", false, "Stop the server");
		options.addOption("r", "repository", true, "Repository of the content");

		Parser parser = new BasicParser();
		CommandLine cmd = parser.parse(options, args);

		return cmd;
	}

	private void processCommandLine(CommandLine cmd) throws Exception {
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
		boolean gpages = cmd.hasOption("go");
		boolean gsitemap = cmd.hasOption("gsm");
		boolean preview = cmd.hasOption("p");
		boolean serverOption = cmd.hasOption("s");
		boolean stop = cmd.hasOption("ss");
		String repository = cmd.getOptionValue("r");

		if (hash) {
			initRegistry();
			initSessionFactory();

			MainService service = Globals.registry.getService(MainService.class);

			service.getIndexerService().hash();
		}

		Collection<Post> posts = new LinkedHashSet<>();
		Collection<Label> labels = new LinkedHashSet<>();
		if (index || iindex || importOption || iimportOption) {
			initRegistry();
			initSessionFactory();

			MainService service = Globals.registry.getService(MainService.class);

			logger.info("Indexing...");
			service.getIndexerService().setForceIndex(iindex);
			service.getIndexerService().setForceImport(iimportOption);
			if (importOption || iimportOption) {
				List<Post> p = service.getIndexerService().importSources();
				posts.addAll(p);
			}
			if (index || iindex) {
				List<Post> p = service.getIndexerService().index();
				posts.addAll(p);
			}
			for (Post post : posts) {
				labels.addAll(post.getLabels());
			}
		}

		if (generate || ggenerate || gindex || glabels || gfeeds || garchive || gstatics || gpages) {
			initRegistry();
			initSessionFactory();

			MainService service = Globals.registry.getService(MainService.class);
			posts = (ggenerate || gindex || glabels || garchive) ? service.getPostDAO().findAll() : posts;
			labels = (ggenerate || gindex || glabels || garchive) ? service.getLabelDAO().findAll() : labels;
			if (!posts.isEmpty()) {
				logger.info("Generating pages...");
				if (ggenerate || gindex) {
					List<File> pindex = service.getPublicGeneratorService().generateIndex();
				}
				if (ggenerate || glabels) {
					List<File> plabels = service.getPublicGeneratorService().generateLabels(new ArrayList(labels));
				}
			}

			if (ggenerate || gpages) {
				File faqPage = service.getPublicGeneratorService().generatePage("faq", new Object[0], Collections.EMPTY_MAP);
			}

			if (ggenerate || generate) {
				if (!posts.isEmpty()) {
					logger.info("Generating posts...");
					List<File> ps = service.getPublicGeneratorService().generatePosts(new ArrayList(posts));

					Set<Source> sources = new HashSet<>();
					for (Post post : posts) {
						sources.add(post.getSource());
					}
					List<Source> s = new ArrayList(sources);
					s.add(null);
					List<File> ss = service.getPublicGeneratorService().generateLastPosts(s);
				}
			}

			if (!posts.isEmpty()) {
				if (ggenerate || garchive) {
					logger.info("Generating archive...");
					List<File> as = service.getPublicGeneratorService().generateArchive(posts);
				}
			}

			if (!labels.isEmpty()) {
				if (ggenerate || gfeeds) {
					logger.info("Generating feeds...");
					File mainAtom = service.getPublicGeneratorService().generateRss();
					for (Label label : labels) {
						File labelAtom = service.getPublicGeneratorService().generateRss(label);
					}
				}
			}

			if (!posts.isEmpty()) {
				if (ggenerate || gsitemap) {
					logger.info("Generating sitemap...");
					File sitemap = service.getPublicGeneratorService().generateSitemap();
				}
			}

			if (ggenerate || gstatics) {
				logger.info("Generating statics...");
				WroConfiguration config = new WroConfiguration();
				Context.set(Context.standaloneContext(), config);
				try {
					service.getPublicGeneratorService().generateStatics(Globals.STATICS);
				} finally {
					Context.unset();
				}
			}

			logger.info("Generating last updated...");
			service.getPublicGeneratorService().generateLastUpdated();
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