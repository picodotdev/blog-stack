package info.blogstack.pages;

import info.blogstack.misc.Utils;
import info.blogstack.services.MainService;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public class Post {

	private Object[] context;

	@Inject
	private MainService service;

	@Property
	private info.blogstack.entities.Post post;
	
	void onActivate(Object[] context) {
		this.context = context;
	}
	
	Object[] onPassivate() {
		return context;
	}

	void setupRender() {
		String hash = Utils.getHash(context);
		post = service.getPostDAO().findByHash(hash);
	}
	
	public String getTitle() {
		return post.getTitle();
	}

	public String getSubtitle() {
		return post.getSource().getName();
	}
}