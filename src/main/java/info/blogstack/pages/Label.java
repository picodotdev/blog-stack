package info.blogstack.pages;

import static info.blogstack.persistence.jooq.Tables.POST;
import info.blogstack.misc.Globals;
import info.blogstack.misc.Utils;
import info.blogstack.persistence.daos.Pagination;
import info.blogstack.persistence.jooq.tables.records.LabelRecord;
import info.blogstack.persistence.jooq.tables.records.PostRecord;
import info.blogstack.persistence.jooq.tables.records.SourceRecord;
import info.blogstack.services.MainService;

import java.util.List;

import org.apache.tapestry5.Block;
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
		page = (context.length >= 3) ? (Integer) context[2] : 0;
	}
	
	public String getTitle() {
		return String.format("Etiqueta %s", label.getName());
	}

	public List<PostRecord> getPosts() {
		return getPosts(Globals.NUMBER_POSTS_PAGE * page, Globals.NUMBER_POSTS_PAGE);
	}

	public List<PostRecord> getFeaturedPosts() {
		List<PostRecord> posts = getPosts();
		return posts.subList(0, Math.min(posts.size(), Globals.NUMBER_POSTS_FEATURED));
	}
	
	public List<PostRecord> getNotFeaturedPosts() {
		List<PostRecord> posts = getPosts();
		return posts.subList(0, Math.min(posts.size(), Globals.NUMBER_POSTS_PAGE));
	}
	
	private List<PostRecord> getPosts(int from, int to) {
		Pagination pagination = new Pagination(from, to, POST.DATE.desc(), POST.ID.desc());
		return service.getPostDAO().findAllByLabel(label, pagination);
	}
}