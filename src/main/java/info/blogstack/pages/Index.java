package info.blogstack.pages;

import static info.blogstack.persistence.jooq.Tables.POST;
import info.blogstack.misc.Globals;
import info.blogstack.persistence.daos.Pagination;
import info.blogstack.persistence.jooq.tables.records.PostRecord;
import info.blogstack.services.MainService;

import java.util.List;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public class Index {

	private Object[] context;
	
	@Property
	private Integer page;
	
	@Inject
	private MainService service;

	@Property
	private PostRecord post;
	
	@Property
	private Integer i;
	
	void onActivate() {		
	}
	
	void onActivate(Object[] context) {
		this.context = context;
	}
	
	void setupRender() {
		 page = (context.length >= 2) ? (Integer) context[1] : 0;
	}
	
	public List<PostRecord> getFeaturedPosts() {
		return getPosts(0, Globals.NUMBER_POSTS_FEATURED);
	}
	
	public List<PostRecord> getNotFeaturedPosts() {
		return getPosts(Globals.NUMBER_POSTS_FEATURED, Globals.NUMBER_POSTS_PAGE - Globals.NUMBER_POSTS_FEATURED);
	}

	private List<PostRecord> getPosts(int from, int number) {
		Pagination pagination = new Pagination(from, number, POST.DATE.desc(), POST.ID.desc());
		return service.getPostDAO().findAll(pagination);
	}
}