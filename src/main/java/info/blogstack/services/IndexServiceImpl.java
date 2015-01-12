package info.blogstack.services;

import static info.blogstack.persistence.jooq.Tables.IMPORT_SOURCE;
import static info.blogstack.persistence.jooq.Tables.INDEXATION;
import static info.blogstack.persistence.jooq.Tables.LABEL;
import static info.blogstack.persistence.jooq.Tables.POST;
import static info.blogstack.persistence.jooq.Tables.POSTS_INDEXATIONS;
import static info.blogstack.persistence.jooq.Tables.POSTS_LABELS;
import info.blogstack.misc.AppWhitelist;
import info.blogstack.misc.Globals;
import info.blogstack.misc.Utils;
import info.blogstack.persistence.jooq.Keys;
import info.blogstack.persistence.jooq.tables.records.ImportSourceRecord;
import info.blogstack.persistence.jooq.tables.records.IndexationRecord;
import info.blogstack.persistence.jooq.tables.records.LabelRecord;
import info.blogstack.persistence.jooq.tables.records.PostRecord;
import info.blogstack.persistence.jooq.tables.records.PostsIndexationsRecord;
import info.blogstack.persistence.jooq.tables.records.PostsLabelsRecord;
import info.blogstack.persistence.jooq.tables.records.SourceRecord;
import info.blogstack.persistence.records.AppLabelRecord;
import info.blogstack.persistence.records.AppPostRecord;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.syndication.feed.synd.SyndCategory;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

@SuppressWarnings("rawtypes")
public class IndexServiceImpl implements IndexService {

	private static Logger logger = LoggerFactory.getLogger(IndexServiceImpl.class);

	private MainService service;
	private boolean forceIndex;
	private boolean forceImport;

	public IndexServiceImpl(MainService service) {
		this.service = service;
		this.forceIndex = false;
		this.forceImport = false;
	}

	@Override
	public void setForceIndex(boolean force) {
		this.forceIndex = force;
	}

	@Override
	public void setForceImport(boolean force) {
		this.forceImport = force;
	}

	@Override
	public void hash() throws Exception {
		List<PostRecord> posts = service.getPostDAO().findAll();
		logger.info("Hashsing {} posts...", posts.size());
		for (PostRecord post : posts) {
			AppPostRecord p = post.into(AppPostRecord.class);
			p.updateHash();
			p.store();
		}
	}

	@Override
	public List<PostRecord> index() throws Exception {
		List<SourceRecord> sources = service.getSourceDAO().findAll();

		logger.info("Indexing {} sources...", sources.size());
		IndexationRecord indexation = service.getContext().newRecord(INDEXATION);
		indexation.setCreationdate(DateTime.now());
		indexation.store();

		List<PostRecord> posts = new ArrayList<>();
		for (SourceRecord source : sources) {
			try {
				HttpGet get = new HttpGet(source.getUrl());
				RequestConfig config = RequestConfig.custom().setConnectTimeout(30 * 1000).build();
				HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
				HttpResponse response = client.execute(get);
				HttpEntity entity = response.getEntity();
				posts.addAll(index(indexation, source, new InputStreamReader(entity.getContent())));
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		return posts;
	}

	@Override
	public List<PostRecord> importSources() throws Exception {
		List<SourceRecord> sources = (forceImport) ? service.getSourceDAO().findAll() : service.getSourceDAO().findImportPending();

		// Importar aquellas cuya fuente de información exista
		List<SourceRecord> s = new ArrayList<>();
		for (SourceRecord source : sources) {
			File f = new File(Globals.IMPORT, String.format("%s.xml", source.getAlias()));
			if (!f.exists()) {
				continue;
			}
			s.add(source);
		}
		sources = s;

		logger.info("Importing {} sources...", sources.size());
		List<PostRecord> posts = new ArrayList<>();
		if (sources.isEmpty()) {
			return posts;
		}

		IndexationRecord indexation = service.getContext().newRecord(INDEXATION);
		indexation.setCreationdate(DateTime.now());
		indexation.store();

		for (SourceRecord source : sources) {
			File f = new File(Globals.IMPORT, String.format("%s.xml", source.getAlias()));
			posts.addAll(index(indexation, source, new FileReader(f)));

			ImportSourceRecord importSource = source.fetchParent(Keys.SOURCE_IMPORTSOURCE_ID);
			if (importSource == null) {
				importSource = service.getContext().newRecord(IMPORT_SOURCE);
				importSource.setCreationdate(DateTime.now());
				importSource.store();
				source.setImportsourceId(importSource.getId());
			}
			importSource.setUpdatedate(DateTime.now());
			importSource.store();
		}

		return posts;
	}

	private List<PostRecord> index(IndexationRecord indexation, SourceRecord source, Reader reader) throws IOException, IllegalArgumentException, FeedException  {
		logger.info("Indexing {} source...", source.getName());
		Reader fr = new XmlReader(IOUtils.toInputStream(IOUtils.toString(filterReader(reader))));
		SyndFeed feed = new SyndFeedInput().build(fr);

		@SuppressWarnings("unchecked")
		List<SyndEntry> entries = feed.getEntries();
		return indexPosts(indexation, source, entries);
	}

	private List<PostRecord> indexPosts(IndexationRecord indexation, SourceRecord source, List<SyndEntry> entries) {
		List<PostRecord> posts = new ArrayList<>();
		Iterator it = entries.iterator();
		while (it.hasNext()) {
			SyndEntry entry = (SyndEntry) it.next();
			try {
				PostRecord post = indexPost(indexation, source, entry);
				if (post == null) {
					continue;
				}

				posts.add(post);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		return posts;
	}

	private PostRecord indexPost(IndexationRecord indexation, SourceRecord source, SyndEntry entry) {
		if (entry.getLink() == null) {
			return null;
		}

		// Search by url
		AppPostRecord post = null;
		PostRecord pr = service.getPostDAO().findByURL(entry.getLink());
		if (pr != null) {
			post = pr.into(AppPostRecord.class);
		}

		// Search by hash
		DateTime now = DateTime.now();
		DateTime updateDate = (entry.getUpdatedDate() == null) ? null : new DateTime(entry.getUpdatedDate());
		DateTime publishDate = (entry.getPublishedDate() == null) ? updateDate : new DateTime(entry.getPublishedDate());

		if (post == null) {
			PostRecord p = service.getContext().newRecord(POST);
			p.setUpdatedate(updateDate);
			p.setPublishdate(publishDate);
			p.setTitle(StringEscapeUtils.unescapeHtml4(entry.getTitle()));

			pr = service.getPostDAO().findByHash(Utils.getHash(p, source));
			if (pr != null) {
				post = pr.into(AppPostRecord.class);
			}
			if (post != null && !source.equals(post.fetchParent(Keys.POST_SOURCE_ID))) {
				logger.warn(String.format("Article with same hash and different source (post: %s, source: %s, post source: %s)", post.getId(), source.getName(), post
						.fetchParent(Keys.POST_SOURCE_ID).getName()));
				post = null;
			}
		}

		if (!forceIndex && !forceImport && post != null && (publishDate == null || publishDate.isEqual(post.getPublishdate()))
				&& (updateDate == null || updateDate.isEqual(post.getUpdatedate()))) {
			// La articulo ya estaba indexada y la fecha de actualizacion es anterior
			return null;
		} else if (post == null) {
			logger.info("Indexing {} post...", entry.getTitle());

			post = service.getContext().newRecord(POST).into(AppPostRecord.class);
			post.setCreationdate(now);
			post.setVisible(true);
			post.setFresh(true);
			post.setShared(false);
			post.setSourceId(source.getId());
		} else {
			logger.info("Updating {} post...", entry.getTitle());
		}

		post.setUpdatedate(updateDate);
		post.setPublishdate(publishDate);
		post.setUrl(entry.getLink());
		post.setTitle(StringEscapeUtils.unescapeHtml4(entry.getTitle()));
		if (StringUtils.isBlank(entry.getAuthor())) {
			post.setAuthor(source.getAuthor());
		} else {
			post.setAuthor(entry.getAuthor());
		}

		StringBuffer postContent = new StringBuffer();
		if (entry.getContents().isEmpty()) {
			logger.warn(String.format("Article without content, using description (%s, %s)", source.getName(), entry.getLink()));
			postContent.append(entry.getDescription().getValue());
		} else {
			Iterator cit = entry.getContents().iterator();
			while (cit.hasNext()) {
				SyndContent content = (SyndContent) cit.next();
				postContent.append(content.getValue());
			}
		}

		AppWhitelist whitelist = (AppWhitelist) AppWhitelist.relaxed();
		whitelist.addAttribute("script", "src", "^http[s]?://speakerdeck.com/.*$");
		whitelist.addAttribute("script", "src", "^http[s]?://gist.github.com/.*$");
		whitelist.addAttribute("iframe", "src", "^http[s]?://www.youtube.com/embed/.*$");
		whitelist.addAttribute("iframe", "src", "^http[s]?://player.vimeo.com/video/.*$");
		whitelist.addAttribute("iframe", "src", "^http[s]?://rcm-eu.amazon-adsystem.com/.*$");
		whitelist.addAttribute("embed", "src", "^http[s]?://www.youtube.com/v/.*$");
		String c = Jsoup.clean(postContent.toString(), source.getPageurl(), whitelist);
		post.setContent(c);

		post.updateHash();		
		post.store();

		PostsIndexationsRecord pi = service.getContext().newRecord(POSTS_INDEXATIONS);
		pi.setPostId(post.getId());
		pi.setIndexationId(indexation.getId());
		pi.store();

		if (!post.isFresh()) {
			service.getPostsLabelsDAO().deleteByPost(post);
		}

		@SuppressWarnings("unchecked")
		List<SyndCategory> categories = entry.getCategories();
		Set<LabelRecord> labels = indexLabels(categories);
		for (LabelRecord label : labels) {
			PostsLabelsRecord pl = service.getContext().newRecord(POSTS_LABELS);
			pl.setPostId(post.getId());
			pl.setLabelId(label.getId());
			pl.store();
		}

		if (post.isFresh()) {
			post.setIndexationId(indexation.getId());
		}

		return post;
	}

	private Set<LabelRecord> indexLabels(List<SyndCategory> categories) {
		Set<LabelRecord> es = new HashSet<>();
		for (SyndCategory category : categories) {
			String n = Utils.urlize(category.getName());
			String hash = Utils.getHash(new Object[] { n });
			LabelRecord label = service.getLabelDAO().findByHash(hash);
			if (label == null) {
				AppLabelRecord alabel = service.getContext().newRecord(LABEL).into(AppLabelRecord.class);
				alabel.setName(n);
				alabel.setEnabled(false);
				alabel.setVisible(true);
				alabel.updateHash();
				alabel.store();
				
				label = alabel;
			}
			es.add(label);
		}
		return es;
	}

	private Reader filterReader(Reader reader) throws IOException {
		String s = IOUtils.toString(reader);
		s = s.replaceAll(" async ", " async=\"async\" ");
		return new StringReader(s);
	}
}