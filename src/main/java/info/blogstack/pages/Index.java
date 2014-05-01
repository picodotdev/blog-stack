package info.blogstack.pages;

import info.blogstack.entities.Post;
import info.blogstack.entities.Source;
import info.blogstack.misc.Globals;
import info.blogstack.services.MainService;
import info.blogstack.services.dao.Direction;
import info.blogstack.services.dao.Pagination;
import info.blogstack.services.dao.Sort;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public class Index {

	@Property
	private Integer page;

	@Inject
	private MainService service;

	@Property
	private Source source;

	@Property
	private Post post;
	
	void onActivate() {		
	}
	
	void onActivate(String context, Integer page) {
		this.page = page;
	}

	void setupRender() {
		page = (page == null) ? 0 : page;
	}
	
	/**
	 * Método que devuelve las articulos publicadas o actualizadas más recientemente.
	 */
	public List<Post> getPosts() {
		List<Sort> sorts = new ArrayList<>();
		sorts.add(new Sort("date", Direction.DESCENDING));
		Pagination pagination = new Pagination(Globals.NUMBER_POSTS_PAGE * page, Globals.NUMBER_POSTS_PAGE * (page + 1), sorts);
		return service.getPostDAO().findAll(pagination);
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
}