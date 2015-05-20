package info.blogstack.cli;

import info.blogstack.misc.Globals;
import info.blogstack.misc.Globals.Environment;
import info.blogstack.persistence.jooq.Keys;
import info.blogstack.persistence.jooq.tables.records.LabelRecord;
import info.blogstack.persistence.jooq.tables.records.PostRecord;
import info.blogstack.persistence.jooq.tables.records.PostsLabelsRecord;
import info.blogstack.services.GenerateModule;
import info.blogstack.services.MainService;

import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.catalina.startup.Tomcat;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;
import org.apache.tapestry5.modules.TapestryModule;
import org.apache.tapestry5.services.ServletApplicationInitializer;
import org.joda.time.DateTimeZone;
import org.lazan.t5.offline.services.TapestryOfflineModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ro.isdc.wro.config.Context;
import ro.isdc.wro.config.jmx.WroConfiguration;

import com.google.common.io.LineReader;

public class Main {

	private enum Returns {

		DEFAULT(0), DEPLOY(1);

		private int value;

		Returns(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	private static Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) throws Exception {
		logger.info("Starting...");
		DateTimeZone.setDefault(DateTimeZone.UTC);

		Main main = new Main();
		Runtime.getRuntime().addShutdownHook(new Thread(new ShutdownHook(main)));
		Returns returns = main.process(args);
		if (returns != Returns.DEFAULT) {
			System.exit(returns.getValue());
		}
	}

	private Tomcat server;

	public Main() {
	}

	public Returns process(String[] args) throws Exception {
		return processCommandLine(buildCommandLine(args));
	}

	public void shutdown() {
		logger.info("Ending...");

		if (Globals.registry != null) {
			Globals.registry.shutdown();
			Globals.registry = null;
		}

		if (server != null) {
			try {
				server.stop();			
				server = null;
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	private void initRegistry() throws Exception {
		if (Globals.registry != null) {
			return;
		}
		logger.info("Starting Tapestry registry...");
		Globals.registry = buildRegistry();

		MainService service = Globals.registry.getService(MainService.class);
		service.getGenerateService().init();
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
		options.addOption("nl", "newsletter", false, "Share newsletter content");
		options.addOption("p", "preview", false, "Preview the content");
		options.addOption("s", "start", false, "Start the server");
		options.addOption("ss", "stop", false, "Stop the server");
		options.addOption("r", "repository", true, "Repository of the content");

		Parser parser = new BasicParser();
		CommandLine cmd = parser.parse(options, args);

		return cmd;
	}

	private Returns processCommandLine(CommandLine cmd) throws Exception {
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
		boolean newsletter = cmd.hasOption("nl");
		boolean preview = cmd.hasOption("p");
		boolean serverOption = cmd.hasOption("s");
		boolean stop = cmd.hasOption("ss");
		String repository = cmd.getOptionValue("r");

		Globals.environment = (environment != null) ? Environment.valueOf(environment.toUpperCase()) : Environment.DEVELOPMENT;
		logger.info("Executing in {}...", Globals.environment);

		if (hash) {
			initRegistry();

			MainService service = Globals.registry.getService(MainService.class);

			service.getIndexService().hash();
		}

		Collection<PostRecord> posts = new LinkedHashSet<>();
		Collection<LabelRecord> labels = new LinkedHashSet<>();
		if (index || iindex || importOption || iimportOption) {
			initRegistry();

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

		List<File> files = new ArrayList<>();
		if (generate || ggenerate || gindex || glabels || gfeeds || garchive || gstatics || gpages) {
			initRegistry();

			MainService service = Globals.registry.getService(MainService.class);
			posts = (ggenerate || gindex || glabels || gfeeds || garchive) ? service.getPostDAO().findAll() : posts;
			labels = (ggenerate || gindex || glabels || gfeeds || garchive) ? service.getLabelDAO().findAll() : labels;
			if (!posts.isEmpty()) {
				logger.info("Generating pages...");
				if (generate || ggenerate || gindex) {
					files.addAll(service.getGenerateService().generateIndex());
				}
				if (generate || ggenerate || glabels) {
					files.addAll(service.getGenerateService().generateLabels(new ArrayList<LabelRecord>(labels)));
				}
			}

			if (ggenerate || gpages) {
				files.add(service.getGenerateService().generatePage("faq", new Object[0], Collections.<String, String> emptyMap()));
			}

			if (generate || ggenerate) {
				if (!posts.isEmpty()) {
					logger.info("Generating posts...");
					files.addAll(service.getGenerateService().generatePosts(new ArrayList<PostRecord>(posts)));
				}
			}

			if (!posts.isEmpty()) {
				if (generate || ggenerate || garchive) {
					logger.info("Generating archive...");
					files.addAll(service.getGenerateService().generateArchive(posts));
				}
			}

			if (!labels.isEmpty()) {
				if (generate || ggenerate || gfeeds) {
					logger.info("Generating feeds...");
					files.add(service.getGenerateService().generateRss());
					for (LabelRecord label : labels) {
						files.add(service.getGenerateService().generateRss(label));
					}
				}
			}

			if (!posts.isEmpty()) {
				if (generate || ggenerate || gsitemap) {
					logger.info("Generating sitemap...");
					files.add(service.getGenerateService().generateSitemap());
				}
			}

			if (ggenerate || gstatics) {
				logger.info("Generating statics...");
				WroConfiguration config = new WroConfiguration();
				Context.set(Context.standaloneContext(), config);
				try {
					files.addAll(service.getGenerateService().generateStatics(Globals.STATICS));
				} finally {
					Context.unset();
				}
			}

			if (ggenerate || !posts.isEmpty()) {
				logger.info("Generating last updated...");
				files.add(service.getGenerateService().generateLastUpdated());
			}
		}

		if (!files.isEmpty()) {
			logger.info(String.format("Generated %d files", files.size()));
		}

		if (share) {
			initRegistry();

			logger.info("Sharing in twitter...");
			MainService service = Globals.registry.getService(MainService.class);

			Collection<PostRecord> sp = service.getPostDAO().findAllByShared(false);
			service.getShareService().shareTwitter(sp);
		}

		if (newsletter) {
			initRegistry();

			logger.info("Sharing in newsletter...");
			MainService service = Globals.registry.getService(MainService.class);

			Collection<PostRecord> sp = service.getPostDAO().findNewsletter();
			service.getShareService().shareNewsletter(sp);
		}

		Returns ret = Returns.DEFAULT;
		if (!posts.isEmpty() || !files.isEmpty()) {
			ret = Returns.DEPLOY;
		}

		if (preview || serverOption) {
			if (isStarted()) {
				logger.info("Server already started");
				return ret;
			}

			logger.info("Stating server...");

			String host = getHost();
			int port = getPort();
			int adminPort = getAdminPort();
			
			Tomcat tomcat = new Tomcat();
			tomcat.getConnector().setAttribute("address", host);
			tomcat.getConnector().setPort(port);
			tomcat.getServer().setAddress(host);
			tomcat.getServer().setPort(adminPort);
			
			File r = (repository == null) ? Globals.PUBLIC : new File(repository);
			tomcat.addWebapp("", r.getAbsolutePath());			
			
			tomcat.start();			
			logger.info(String.format("Server listening in http://%s:%d/", tomcat.getConnector().getAttribute("address"), tomcat.getConnector().getPort()));
			tomcat.getServer().await();
			System.exit(0);
		}

		if (stop) {
			if (!isStarted()) {
				logger.info("Server already stopped");
				return ret;
			}

			logger.info("Stopping server...");

			String host = getHost();
			int adminPort = getAdminPort();

			Socket socket = new Socket(host, adminPort);
			Reader r = new InputStreamReader(socket.getInputStream());
			LineReader lr = new LineReader(r);
			Writer w = new OutputStreamWriter(socket.getOutputStream());
			w.write("SHUTDOWN");
			w.close();
		}

		return ret;
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