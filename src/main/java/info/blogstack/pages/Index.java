package info.blogstack.pages;

import static info.blogstack.persistence.jooq.Tables.POST;
import info.blogstack.misc.Globals;
import info.blogstack.persistence.daos.Pagination;
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

public class Index {

	@Property
	private Integer page;

	@Property
	private SourceRecord source;

	@Property
	private PostRecord post;
	
	@Property(read = false)
	@Inject
	private Block featured;

	@Inject
	private MainService service;
	
	@Property
	private Integer i;
	
	void onActivate() {		
	}
	
	void onActivate(String context, Integer page) {
		this.page = page;
	}

	void setupRender() {
		page = (page == null) ? 0 : page;
	}
	
	public List<PostRecord> getPosts() {
		return getPosts((1 + Globals.NUMBER_POSTS_FEATURED + Globals.NUMBER_POSTS_PAGE) * page, Globals.NUMBER_POSTS_PAGE);
	}
	
	public PostRecord getFirstPost() {
		return getPosts(0, 1).get(0);
	}
	
	public List<PostRecord> getFeaturedPosts() {
		return getPosts(1, Globals.NUMBER_POSTS_FEATURED);
	}
	
	public List<PostRecord> getNotFeaturedPosts() {
		return getPosts(1 + Globals.NUMBER_POSTS_FEATURED, Globals.NUMBER_POSTS_PAGE);
	}

	public boolean isFirstPage() {
		return page == 0;
	}
	
	public boolean isLastPage() {
		return page + 1 > Globals.NUMBER_PAGES_INDEX;
	}
	
	public Object[] getPreviusContext() {
		return (page - 1 <= 0) ? null : new Object[] { "page", page - 1 };
	}
	
	public Object[] getNextContext() {
		return (page + 1 > Globals.NUMBER_PAGES_INDEX) ? null : new Object[] { "page", page + 1 };
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
	public Map<String,String> getTags() {
		Map<String, String> m = new HashMap<>();
		m.put("open", "<div class=\"row\">");
		m.put("close", "</div>");
		return m;
	}
	
	private List<PostRecord> getPosts(int from, int number) {
		Pagination pagination = new Pagination(from, number, POST.DATE.desc());
		return service.getPostDAO().findAll(pagination);
	}
}