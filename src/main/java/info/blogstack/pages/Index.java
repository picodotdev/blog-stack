package info.blogstack.pages;

import info.blogstack.entities.Post;
import info.blogstack.entities.Source;
import info.blogstack.misc.Globals;
import info.blogstack.services.MainService;
import info.blogstack.services.dao.Direction;
import info.blogstack.services.dao.Pagination;
import info.blogstack.services.dao.Sort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public class Index {

	@Property
	private Integer page;

	@Property
	private Source source;

	@Property
	private Post post;
	
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
	
	public List<Post> getPosts() {
		return getPosts(Globals.NUMBER_POSTS_PAGE * page, Globals.NUMBER_POSTS_PAGE * (page + 1));
	}
	
	public List<Post> getFeaturedPosts() {
		return getPosts(0, Globals.NUMBER_POSTS_FEATURED);
	}
	
	public List<Post> getNotFeaturedPosts() {
		return getPosts(5, Globals.NUMBER_POSTS_PAGE * (page + 1));
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
	
	public boolean isEvenOpen() {
		return (i % 2) != 0;
	}
	
	public boolean isEvenClose() {
		return (i % 2) == 0 || i + 1 == Globals.NUMBER_POSTS_FEATURED;
	}
	
	public boolean isData() {
		return i == 0;
	}
	
	public Block getFeatured() {
		return (isFirstPage())?featured:null;
	}

	@Cached
	public Map getTags() {
		Map<String, String> m = new HashMap<>();
		m.put("oddOpen", "<div class=\"row\">");
		m.put("oddClose", "</div>");
		return m;
	}
	
	private List<Post> getPosts(int from, int to) {
		List<Sort> sorts = new ArrayList<>();
		sorts.add(new Sort("date", Direction.DESCENDING));
		Pagination pagination = new Pagination(from, to, sorts);
		return service.getPostDAO().findAll(pagination);
	}
}