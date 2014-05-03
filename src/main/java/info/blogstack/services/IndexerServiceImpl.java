package info.blogstack.services;

import info.blogstack.entities.Indexation;
import info.blogstack.entities.Label;
import info.blogstack.entities.Post;
import info.blogstack.entities.Source;
import info.blogstack.misc.AppWhitelist;
import info.blogstack.misc.Globals;
import info.blogstack.misc.Utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.syndication.feed.synd.SyndCategory;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

@SuppressWarnings("rawtypes")
public class IndexerServiceImpl implements IndexerService {

	private static Logger logger = LoggerFactory.getLogger(IndexerServiceImpl.class);

	private MainService service;
	private boolean forceIndex;

	public IndexerServiceImpl(MainService service) {
		this.service = service;
		this.forceIndex = false;
	}

	@Override
	public void setForceIndex(boolean force) {
		this.forceIndex = force;
	}	
	
	@Override
	public List<Post> index() throws Exception {
		List<Source> sources = service.getSourceDAO().findAll();

		logger.info("Indexing {} sources...", sources.size());
		Indexation indexation = new Indexation();
		indexation.setPosts(new ArrayList<Post>());
		indexation.setCreationDate(DateTime.now());
		service.getIndexationDAO().persist(indexation);
		
		List<Post> posts = new ArrayList<>();
		for (Source source : sources) {
			try {
				URL url = new URL(source.getUrl());
				URLConnection conexion = (URLConnection) url.openConnection();
				XmlReader reader = new XmlReader(conexion.getInputStream());
				posts.addAll(index(indexation, source, reader));
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}			
		}

		return posts;
	}
	
	@Override
	public List<Post> importAll() throws Exception {
		List<Source> sources = service.getSourceDAO().findImportPending();

		logger.info("Importing {} sources...", sources.size());
		Indexation indexation = new Indexation();
		indexation.setPosts(new ArrayList<Post>());
		indexation.setCreationDate(DateTime.now());
		service.getIndexationDAO().persist(indexation);
		
		List<Post> posts = new ArrayList<>();
		for (Source source : sources) {
			File f = new File(Globals.IMPORT, String.format("%s.xml", source.getAlias()));
			if (!f.exists()) {
				return Collections.EMPTY_LIST;
			}
			XmlReader reader = new XmlReader(f);

			posts.addAll(index(indexation, source, reader));
		}

		return posts;
	}

	private List<Post> index(Indexation indexation, Source source, XmlReader reader) throws Exception {
		logger.info("Indexing {} source...", source.getName());

		SyndFeed feed = new SyndFeedInput().build(reader);

		return indexPosts(indexation, source, feed.getEntries());
	}
	
	private List<Post> indexPosts(Indexation indexation, Source source, List<SyndEntry> entries) {
		List<Post> posts = new ArrayList<>();
		Iterator it = entries.iterator();
		while (it.hasNext()) {
			SyndEntry entry = (SyndEntry) it.next();
			try {
				Post post = indexPost(indexation, source, entry);
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

	private Post indexPost(Indexation indexation, Source source, SyndEntry entry) {
		if (entry.getLink() == null) {
			return null;
		}

		Post post = service.getPostDAO().findByURL(entry.getLink());

		DateTime now = DateTime.now();
		DateTime publishDate = (entry.getPublishedDate() == null) ? null : new DateTime(entry.getPublishedDate());
		DateTime updateDate = (entry.getUpdatedDate() == null) ? null : new DateTime(entry.getUpdatedDate());

		if (!forceIndex
				&& post != null
				&& (publishDate == null || publishDate.isEqual(post.getPublishDate()))
				&& (updateDate == null || updateDate.isEqual(post.getUpdateDate()))) {
			// La articulo ya estaba indexada y la fecha de actualizacion es anterior
			return null;
		} else if (post == null) {
			post = new Post();
			post.setCreationDate(now);
		}

		Set<Label> labels = indexLabels(entry.getCategories());

		post.setLabels(labels);
		post.setUpdateDate(updateDate);
		post.setPublishDate(publishDate);
		post.setUrl(entry.getLink());
		post.setTitle(entry.getTitle());
		if (StringUtils.isBlank(entry.getAuthor())) {
			post.setAuthor(source.getAutor());
		} else {
			post.setAuthor(entry.getAuthor());
		}

		StringBuffer postContent = new StringBuffer();
		Iterator cit = entry.getContents().iterator();
		while (cit.hasNext()) {
			SyndContent content = (SyndContent) cit.next();
			postContent.append(content.getValue());
		}
		AppWhitelist whitelist = (AppWhitelist) AppWhitelist.relaxed();
		whitelist.addAttribute("script", "src", "^http[s]?://speakerdeck.com/.*$");
		whitelist.addAttribute("script", "src", "^http[s]?://gist.github.com/.*$");
		whitelist.addAttribute("iframe", "src", "^http[s]?://www.youtube.com/embed/.*$");
		whitelist.addAttribute("iframe", "src", "^http[s]?://player.vimeo.com/video/.*$");
		whitelist.addAttribute("iframe", "src", "^http[s]?://rcm-eu.amazon-adsystem.com/.*$");
		String c = Jsoup.clean(postContent.toString(), source.getPageUrl(), whitelist);
		post.setContent(c, service.getSessionFactory().getCurrentSession());
		
		if (post.getId() == null) {
			post.setSource(source);
			source.getPosts().add(post);
			post.setIndexations(new HashSet<Indexation>());
			for (Label l : labels) {
				l.getPosts().add(post);
			}
		}
		
		post.updateHash();
		
		post.getIndexations().add(indexation);
		indexation.getPosts().add(post);
		service.getPostDAO().persist(post);
		
		return post;
	}

	private Set<Label> indexLabels(List<SyndCategory> categories) {
		Set<Label> es = new HashSet<>();
		for (SyndCategory category : categories) {
			String n = Utils.urlize(category.getName());
			String hash = Utils.getHash(new Object[] { n });
			Label l = service.getLabelDAO().findByHash(hash);
			if (l == null) {
				l = new Label(n);
				l.setPosts(new HashSet<Post>());
				l.updateHash();
				service.getLabelDAO().persist(l);
			}
			es.add(l);
		}
		return es;
	}
}