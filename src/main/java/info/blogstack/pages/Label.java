package info.blogstack.pages;

import static info.blogstack.persistence.jooq.Tables.POST;
import info.blogstack.misc.Globals;
import info.blogstack.misc.Utils;
import info.blogstack.persistence.daos.Pagination;
import info.blogstack.persistence.jooq.tables.records.LabelRecord;
import info.blogstack.persistence.jooq.tables.records.PostRecord;
import info.blogstack.persistence.jooq.tables.records.SourceRecord;
import info.blogstack.services.MainService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public class Label {

	private Object[] context;
	
	@Property
	private Integer page;
	
	@Inject
	private MainService service;

	@Property
	private SourceRecord source;

	@Property
	private LabelRecord label;
	
	@Property
	private PostRecord post;
	
	@Property
	private Integer i;
	
	@Property(read = false)
	@Inject
	private Block featured;

	void onActivate(Object[] context) {
		this.context = context;
	}
	
	Object[] onPassivate() {
		return context;
	}

	void setupRender() {
		String hash = Utils.getHash(new Object[] { context[0] } );
		label = service.getLabelDAO().findByHash(hash);

		page = (context.length >= 3) ? (Integer) context[2] : null;
		
		page = (page == null) ? 0 : page;
	}

	/**
	 * Método que devuelve las articulos publicadas o actualizadas más recientemente de una etiqueta.
	 */
	public List<PostRecord> getPosts() {
		return getPosts(Globals.NUMBER_POSTS_PAGE * page, Globals.NUMBER_POSTS_PAGE * (page + 1));
	}

	public List<PostRecord> getFeaturedPosts() {
		return getPosts(0, Globals.NUMBER_POSTS_FEATURED_LABEL);
	}
	
	public List<PostRecord> getNotFeaturedPosts() {
		return getPosts(Globals.NUMBER_POSTS_FEATURED_LABEL, Globals.NUMBER_POSTS_PAGE * (page + 1));
	}
	
	@Cached(watch = "label")
	public Long getPostsCount() {
		return service.getPostDAO().countBy(label);
	}
	
	public boolean isFirstPage() {
		return page == 0;
	}
	
	public boolean isLastPage() {
		return (page + 1 > Globals.NUMBER_PAGES_LABEL || getPostsCount() <= Globals.NUMBER_POSTS_PAGE * (page + 1));
	}
	
	public Object[] getPreviusContext() {
		return (page - 1 <= 0) ? new Object[] { label.getName() } : new Object[] { label.getName(), "page", page - 1 };
	}
	
	public Object[] getNextContext() {
		return (page + 1 > Globals.NUMBER_PAGES_LABEL) ? new Object[] { label.getName() } : new Object[] { label.getName(), "page", page + 1 };
	}
	
	public boolean isOpen() {
		return (i % 2) == 0;
	}
	
	public boolean isClose() {
		return (i % 2) != 0 || i + 1 == getFeaturedPosts().size();
	}
	
	public Block getFeatured() {
		return (isFirstPage())?featured:null;
	}

	@Cached
	public Map getTags() {
		Map<String, String> m = new HashMap<>();
		m.put("open", "<div class=\"row\">");
		m.put("close", "</div>");
		return m;
	}
	
	private List<PostRecord> getPosts(int from, int to) {
		Pagination pagination = new Pagination(from, to, POST.DATE.desc());
		return service.getPostDAO().findAllByLabel(label, pagination);
	}
}