package info.blogstack.services;

import info.blogstack.entities.Indexation;
import info.blogstack.entities.Label;
import info.blogstack.entities.Post;
import info.blogstack.entities.Source;
import info.blogstack.misc.Globals;
import info.blogstack.misc.Utils;
import info.blogstack.services.dao.Direction;
import info.blogstack.services.dao.Pagination;
import info.blogstack.services.dao.Sort;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.internal.services.ArrayEventContext;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.PageRenderRequestParameters;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.lazan.t5.offline.DefaultOfflineRequestContext;
import org.lazan.t5.offline.services.OfflineComponentRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ro.isdc.wro.extensions.processor.css.LessCssProcessor;
import ro.isdc.wro.model.group.processor.Injector;
import ro.isdc.wro.model.group.processor.InjectorBuilder;
import ro.isdc.wro.model.resource.Resource;
import ro.isdc.wro.model.resource.ResourceType;

import com.sun.syndication.feed.synd.SyndCategory;
import com.sun.syndication.feed.synd.SyndCategoryImpl;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.SyndFeedOutput;

public class GenerateServiceImpl implements GenerateService {

	private static Logger logger = LoggerFactory.getLogger(GenerateServiceImpl.class);

	private static DateTimeFormatter LASTUPDATED_DATETIMEFORMAT = DateTimeFormat.forPattern("yyyyMMddHHmmZ");

	private MainService service;
	private File to;
	private List<File> statics;

	private Injector injector;

	public GenerateServiceImpl(MainService service, File to, List<File> statics) {
		this.service = service;
		this.to = to;
		this.statics = statics;
	}

	public File getTo() {
		return to;
	}

	public File getToPage(String page, Object[] context, Map<String, String> params) {
		List l = new ArrayList<>();
		if (!page.equals("index")) {
			l.add(page);
		}
		if (context != null && context.length > 0) {
			l.addAll(Arrays.asList(context));
		}

		File f = new File(StringUtils.join(l, File.separator));
		return new File(f, "index.html");
	}

	public File getToRss() {
		return new File(String.format("feed.atom.xml", "blogstack"));
	}

	public File getToRss(Label label) {
		return new File(String.format("label/%s/feed.atom.xml", label.getName()));
	}

	@Override
	public List<File> generateIndex() throws IOException {
		List<File> files = new ArrayList<>();
		File page = generatePage("index", new Object[0], Collections.EMPTY_MAP);
		files.add(page);
		for (int i = 1; i <= Globals.NUMBER_PAGES_INDEX; ++i) {
			File npage = generatePage("index", new Object[] { "page", i }, Collections.EMPTY_MAP);
			files.add(npage);
		}
		return files;
	}
	
	public List<File> generateLabels(List<Label> labels) throws IOException {
		List<File> files = new ArrayList<>();
		for (Label label : labels) {
			if (!label.getEnabled()) {
				continue;
			}			
			String l = Utils.urlize(label.getName());
			File labelPage = generatePage("label", new Object[] { l }, Collections.EMPTY_MAP);
			Long n = service.getPostDAO().countBy(label);
			Integer np = Math.min(((Double) Math.floor(n / Globals.NUMBER_POSTS_PAGE)).intValue(), Globals.NUMBER_PAGES_LABEL);
			for (int i = 1; i <= np; ++i) {
				File ilabelPage = generatePage("label", new Object[] { l, "page", i }, Collections.EMPTY_MAP);
			}
		}
		return files;
	}

	@Override
	public List<File> generatePosts(List<Post> posts) throws IOException {
		List<File> files = new ArrayList<>();
		for (Post post : posts) {
			if (!post.getVisible()) {
				continue;
			}
			files.add(generatePost(post));
		}
		return files;
	}

	@Override
	public List<File> generateArchive(Collection<Post> posts) throws IOException {
		List<File> as = new ArrayList<>();
		
		if (!posts.isEmpty()) {
			logger.info("Generating main archive...");
			File a = generatePage("archive", new Object[0], Collections.EMPTY_MAP);
			as.add(a);
		}
		
		Map<String ,Object[]> dates = new HashMap<>();
		for (Post post : posts) {
			if (!post.getVisible()) {
				continue;
			}
			String y = String.valueOf(post.getPublishDate().getYear());
			String m = StringUtils.leftPad(String.valueOf(post.getPublishDate().getMonthOfYear()), 2, "0");
			dates.put(String.format("%s-%s", y, m), new Object[] { y, m });
		}

		if (!dates.isEmpty()) {
			logger.info("Generating date archive...");
			for (Object[] date : dates.values()) {
				File da = generatePage("archive", date, Collections.EMPTY_MAP);
				as.add(da);
			}
		}
		return as;
	}

	@Override
	public File generatePost(Post post) throws IOException {
		if (!post.getVisible()) {
			return null;
		}
		
		logger.info("Generating post «{}» ({}, {})...", post.getTitle(), post.getId(), post.getSource().getName());

		Object[] context = Utils.getContext(post);
		File file = new File(String.format("%s/post/%s/index.html", to.getPath(), StringUtils.join(context, "/")));

		file.getParentFile().mkdirs();

		Writer w = new FileWriter(file);
		render("post", context, Collections.EMPTY_MAP, Globals.LOCALE, w);
		w.close();

		return file;
	}

	@Override
	public Collection<File> generateStatics(List<File> files) throws IOException {
		logger.info("Copying statics...");
		File dassets = new File(to, "assets");
		File dimages = new File(dassets, "images");
		File dcss = new File(dassets, "css");
		File dmodules = new File(to, "modules");
		for (File st : statics) {
			File images = new File(st, "images");
			File css = new File(st, "css");
			File assets = new File(st, "assets");
			File modules = new File(st, "modules");
			if (images.exists()) {
				FileUtils.copyDirectory(images, dimages);
			}
			if (css.exists()) {
				FileUtils.copyDirectory(css, dcss);
			}
			if (assets.exists()) {
				FileUtils.copyDirectory(assets, dassets);
			}
			if (modules.exists()) {
				FileUtils.copyDirectory(modules, dmodules);
			}
			FileUtils.copyDirectory(st, to, FileFilterUtils.suffixFileFilter("txt"));
			FileUtils.copyDirectory(st, to, FileFilterUtils.suffixFileFilter("html"));
			FileUtils.copyDirectory(st, to, FileFilterUtils.nameFileFilter("CNAME"));
		}

		logger.info("Processing statics...");
		Iterator<File> it = FileUtils.iterateFiles(dcss, new String[] { "less" }, true);
		LessCssProcessor less = getLessCssProcessor();
		while (it.hasNext()) {
			File f = it.next();

			Reader r = new FileReader(f);
			Writer w = new StringWriter();
			less.process(Resource.create(f.getName(), ResourceType.CSS), r, w);
			r.close();
			w.close();

			r = new StringReader(w.toString());
			w = new FileWriter(new File(f.getParent(), f.getName().replace(".less", ".css")));
			IOUtils.copy(r, w);
			r.close();
			w.close();
		}

		return FileUtils.listFiles(to, null, true);
	}

	@Override
	public List<File> generateLastPosts(List<Source> sources) throws IOException {
		List<File> files = new ArrayList<>();
		for (Source source : sources) {
			String alias = ((source == null) ? "blogstack" : source.getAlias());
			String name = ((source == null) ? "Blog Stack" : source.getName());

			List<Sort> sort = Collections.singletonList(new Sort("date", Direction.DESCENDING));
			List<Post> posts = null;
			Pagination pagination = new Pagination(0, Globals.NUMBER_POSTS_LASTS, sort);
			if (source == null) {
				posts = service.getPostDAO().findAll(pagination);
			} else {
				posts = service.getPostDAO().findAllBySource(source, pagination);
			}

			JSONArray ps = new JSONArray();
			for (Post post : posts) {
				if (!post.getVisible()) {
					continue;
				}
				JSONObject object = new JSONObject();
				object.put("url", service.getPageRenderLinkSource().createPageRenderLinkWithContext("post", Utils.getContext(post)).toString());
				object.put("title", post.getTitle());
				ps.put(object);
			}

			JSONObject object = new JSONObject();
			object.put("name", name);
			object.put("posts", ps);

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			pw.println("define(\"app/sources/" + alias + "\", [], function() {");
			pw.println("	return " + object.toString());
			pw.println("});");

			pw.close();
			sw.close();

			File f = new File(to, "modules/app/sources/" + alias + ".js");
			FileUtils.write(f, sw.toString());

			files.add(f);
		}
		return files;
	}

	@Override
	public File generateLastUpdated() throws IOException {
		DateTime date = DateTime.now();
		Indexation indexation = service.getIndexationDAO().findLast();
		if (indexation != null) {
			date = indexation.getCreationDate();
		}

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		pw.println("define(\"app/lastUpdated\", [], function() {");
		pw.println("	return {");
		pw.println("		date: '" + LASTUPDATED_DATETIMEFORMAT.print(date) + "'");
		pw.println("	}");
		pw.println("});");

		pw.close();
		sw.close();

		File f = new File(to, "modules/app/lastUpdated.js");
		FileUtils.write(f, sw.toString());
		return f;
	}

	@Override
	public File generateRss() throws Exception {
		Pagination pagination = new Pagination(0, Globals.NUMBER_POSTS_FEED, Collections.singletonList(new Sort("date", Direction.DESCENDING)));
		List<Post> posts = service.getPostDAO().findAll(pagination);

		return generateRss(new File(to, getToRss().getPath()), posts);
	}

	@Override
	public File generateRss(Label label) throws Exception {
		if (!label.getEnabled()) {
			return null;
		}
		
		Pagination pagination = new Pagination(0, Globals.NUMBER_POSTS_FEED, Collections.singletonList(new Sort("date", Direction.DESCENDING)));
		List<Post> posts = service.getPostDAO().findAllByLabel(label, pagination);

		return generateRss(new File(to, getToRss(label).getPath()), posts);
	}

	private File generateRss(File file, List<Post> posts) throws Exception {
		logger.info("Generating feed ({})...", file.getPath());

		SyndFeed source = new SyndFeedImpl();

		source.setPublishedDate(new Date());
		source.setAuthor("Blog Stack");
		source.setDescription("Un poco más que un agregador/planeta de bitácoras sobre programación, desarrollo, software libre, gnu/linux, tecnología, ...");
		source.setFeedType("atom_1.0");
		source.setLanguage("es");
		source.setLink("http://www.blogstack.info");
		source.setTitle("Blog Stack");

		// La image aunque se incluya no se muestra
		// SyndImage logo = new SyndImageImpl();
		// logo.setLink("http://www.blogstack.info");
		// logo.setUrl("/assets/images/blogstack.png");
		// logo.setTitle("Blog Stack");
		// logo.setDescription("Un poco más que un agregador de blogs sobre programación, desarrollo, software, software libre, gnu/linux, tecnología, ...");
		// fuente.setImage(logo);

		List<SyndEntry> es = new ArrayList<>();
		for (Post post : posts) {
			if (!post.getVisible()) {
				continue;
			}

			SyndEntry e = new SyndEntryImpl();
			if (post.getUpdateDate() != null) {
				e.setUpdatedDate(post.getUpdateDate().toDate());
			}
			if (post.getPublishDate() != null) {
				e.setPublishedDate(post.getPublishDate().toDate());
			}
			String link = service.getPageRenderLinkSource().createPageRenderLinkWithContext(info.blogstack.pages.Post.class, Utils.getContext(post)).toAbsoluteURI();
			e.setLink(link);
			e.setTitle(post.getTitle());
			e.setAuthor(post.getAuthor());
			List<SyndCategory> cs = new ArrayList<>();
			for (Label etiqueta : post.getLabels()) {
				SyndCategory c = new SyndCategoryImpl();
				c.setName(etiqueta.getName());
				cs.add(c);
			}
			e.setCategories(cs);
			SyndContent c = new SyndContentImpl();
			c.setType("text/html");
			c.setValue(String.format("<p>%s</p><p><a href=\"%s\">continuar leyendo &gt;&gt;</a></p>", post.getContentExcerpt(), link));
			e.setContents(Collections.singletonList(c));

			es.add(e);
		}
		source.setEntries(es);

		file.getParentFile().mkdirs();

		Writer w = new FileWriter(file);
		new SyndFeedOutput().output(source, w);
		w.close();

		return file;
	}

	public File generateSitemap() throws IOException {
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);

		writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		writer.println("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");

		Pagination pagination = new Pagination(0, Globals.NUMBER_POSTS_SITEMAP, Collections.singletonList(new Sort("date", Direction.DESCENDING)));
		List<Post> posts = service.getPostDAO().findAll(pagination);

		DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY-MM-dd'T'hh:mm:ssZZ");

		int i = 0;
		for (Post post : posts) {
			if (!post.getVisible()) {
				continue;
			}
			
			String loc = service.getPageRenderLinkSource().createPageRenderLinkWithContext(info.blogstack.pages.Post.class, Utils.getContext(post)).toAbsoluteURI();
			String lastmod = formatter.print(post.getDate());
			String changefreq = (post.getDate().isAfter(DateTime.now().plusDays(-7))) ? "daily" : "weekly";
			String priority = (i < 50) ? "0.9" : "0.6";
			printSitemapUrl(writer, loc, lastmod, changefreq, priority);
			i += 1;
		}
		writer.println("</urlset>");

		File file = new File(to, "sitemap.xml");
		file.getParentFile().mkdirs();

		Reader r = new StringReader(sw.toString());
		Writer w = new FileWriter(file);
		IOUtils.copy(r, w);
		r.close();
		w.close();

		return file;
	}
	
	@Override
	public File generatePage(String page, Object[] context, Map<String, String> params) throws IOException {
		File file = new File(to, getToPage(page, context, params).getPath());
		logger.info("Generating page «{}» ({}, {})...", page, file, params.toString());

		file.getParentFile().mkdirs();

		Writer w = new FileWriter(file);
		render(page, context, params, Globals.LOCALE, w);
		w.close();

		return file;
	}

	private void render(String page, Object[] context, Map<String, String> params, Locale locale, Writer writer) throws IOException {
		TypeCoercer coercer = Globals.registry.getService(TypeCoercer.class);
		OfflineComponentRenderer renderer = Globals.registry.getService("BlogStackOfflineComponentRenderer", OfflineComponentRenderer.class);

		EventContext activationContext = new ArrayEventContext(coercer, context);
		PageRenderRequestParameters requestParams = new PageRenderRequestParameters(page, activationContext, false);
		DefaultOfflineRequestContext requestContext = new DefaultOfflineRequestContext();
		for (Map.Entry<String, String> param : params.entrySet()) {
			requestContext.setParameter(param.getKey(), param.getValue());
		}
		requestContext.setLocale(locale);

		renderer.renderPage(writer, requestContext, requestParams);
	}

	private void printSitemapUrl(PrintWriter writer, String loc, String lastmod, String changefreq, String priority) {
		writer.println("<url>");
		writer.println(String.format("\t<loc>%s</loc>", loc));
		writer.println(String.format("\t<lastmod>%s</lastmod>", lastmod));
		writer.println(String.format("\t<changefreq>%s</changefreq>", changefreq));
		writer.println(String.format("\t<priority>%s</priority>", priority));
		writer.println("</url>");
	}

	private LessCssProcessor getLessCssProcessor() {
		LessCssProcessor processor = new LessCssProcessor();
		getInjector().inject(processor);
		return processor;
	}

	private Injector getInjector() {
		if (injector == null) {
			injector = new InjectorBuilder().build();
		}
		return injector;
	}
}